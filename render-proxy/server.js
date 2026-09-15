"use strict";

const http = require("http");
const https = require("https");
const { URL } = require("url");

const PORT =
  Number(process.env.PORT || 10000);

const UPSTREAM =
  new URL(
    process.env.UPSTREAM_URL ||
    "https://novabank-banking-system.vercel.app"
  );

const BYPASS_SECRET =
  process.env.VERCEL_AUTOMATION_BYPASS_SECRET || "";


function health(
  response
) {

  response.writeHead(
    200,
    {
      "content-type":
        "application/json; charset=utf-8",

      "cache-control":
        "no-store"
    }
  );

  response.end(
    JSON.stringify({
      ok: true,
      service: "novabank-qa-proxy"
    })
  );

}


function proxy(
  clientRequest,
  clientResponse
) {

  const target =
    new URL(
      clientRequest.url,
      UPSTREAM
    );


  const headers = {
    ...clientRequest.headers,

    host:
      UPSTREAM.host
  };


  /*
   * Authorize the server-side proxy with Vercel.
   *
   * The Playwright browser never needs to talk
   * directly to Vercel.
   */
  if (BYPASS_SECRET) {

    headers[
      "x-vercel-protection-bypass"
    ] =
      BYPASS_SECRET;

  }


  delete headers.connection;
  delete headers["proxy-connection"];
  delete headers["keep-alive"];
  delete headers.te;
  delete headers.trailer;
  delete headers.upgrade;


  const upstreamRequest =
    https.request(
      {
        protocol:
          UPSTREAM.protocol,

        hostname:
          UPSTREAM.hostname,

        port:
          UPSTREAM.port || 443,

        method:
          clientRequest.method,

        path:
          target.pathname +
          target.search,

        headers
      },
      upstreamResponse => {

        const responseHeaders = {
          ...upstreamResponse.headers
        };


        /*
         * Do not expose Vercel-domain cookies
         * to the Render QA hostname.
         */
        delete responseHeaders[
          "set-cookie"
        ];


        /*
         * Rewrite any Vercel absolute redirect
         * so the browser remains on Render.
         */
        if (
          responseHeaders.location
        ) {

          try {

            const location =
              new URL(
                responseHeaders.location,
                UPSTREAM
              );


            if (
              location.origin ===
              UPSTREAM.origin
            ) {

              responseHeaders.location =
                location.pathname +
                location.search +
                location.hash;

            }

          }
          catch {

            // Leave the original Location unchanged.

          }

        }


        clientResponse.writeHead(
          upstreamResponse.statusCode || 502,
          responseHeaders
        );


        upstreamResponse.pipe(
          clientResponse
        );

      }
    );


  upstreamRequest.on(
    "error",
    error => {

      console.error(
        "Upstream request failed:",
        error.message
      );


      if (
        !clientResponse.headersSent
      ) {

        clientResponse.writeHead(
          502,
          {
            "content-type":
              "application/json; charset=utf-8",

            "cache-control":
              "no-store"
          }
        );

      }


      clientResponse.end(
        JSON.stringify({
          error:
            "UPSTREAM_UNAVAILABLE"
        })
      );

    }
  );


  clientRequest.on(
    "error",
    () => {

      upstreamRequest.destroy();

    }
  );


  clientRequest.pipe(
    upstreamRequest
  );

}


const server =
  http.createServer(
    (
      request,
      response
    ) => {

      if (
        request.url ===
        "/__proxy-health"
      ) {

        health(response);

        return;

      }


      proxy(
        request,
        response
      );

    }
  );


server.listen(
  PORT,
  "0.0.0.0",
  () => {

    console.log(
      `NovaBank QA proxy listening on ${PORT}`
    );

    console.log(
      `Upstream: ${UPSTREAM.origin}`
    );

  }
);
