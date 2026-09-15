"use strict";

const http = require("http");
const fs = require("fs");
const path = require("path");
const crypto = require("crypto");

const PORT =
  Number(process.env.PORT || 10000);

const INDEX_FILE =
  path.join(
    __dirname,
    "index.html"
  );


function sendJson(
  response,
  status,
  body
) {

  const payload =
    body === null
      ? ""
      : JSON.stringify(body);

  response.writeHead(
    status,
    {
      "content-type":
        "application/json; charset=utf-8",

      "cache-control":
        "no-store",

      "access-control-allow-origin":
        "*",

      "access-control-allow-headers":
        "Content-Type, Authorization, X-Test-Id",

      "access-control-allow-methods":
        "GET,POST,PATCH,OPTIONS"
    }
  );

  response.end(payload);
}


function sendHtml(
  response
) {

  const html =
    fs.readFileSync(
      INDEX_FILE,
      "utf8"
    );

  response.writeHead(
    200,
    {
      "content-type":
        "text/html; charset=utf-8",

      "cache-control":
        "no-store"
    }
  );

  response.end(html);
}


function readJson(
  request
) {

  return new Promise(
    (resolve, reject) => {

      let body = "";

      request.on(
        "data",
        chunk => {

          body += chunk;

          if (
            body.length >
            1_000_000
          ) {

            reject(
              new Error(
                "REQUEST_TOO_LARGE"
              )
            );

            request.destroy();

          }

        }
      );

      request.on(
        "end",
        () => {

          if (!body) {

            resolve({});

            return;

          }

          try {

            resolve(
              JSON.parse(body)
            );

          }
          catch {

            reject(
              new Error(
                "INVALID_JSON"
              )
            );

          }

        }
      );

      request.on(
        "error",
        reject
      );

    }
  );

}


function getToken(
  request
) {

  const authorization =
    request.headers.authorization ||
    "";

  if (
    !authorization.startsWith(
      "Bearer "
    )
  ) {

    return "";

  }

  return authorization.slice(7);
}


function roleFromToken(
  token
) {

  if (
    token ===
    "novabank-customer-token"
  ) {

    return "customer";

  }

  if (
    token ===
    "novabank-admin-token"
  ) {

    return "admin";

  }

  return null;
}


function requireAuthentication(
  request,
  response
) {

  const role =
    roleFromToken(
      getToken(request)
    );

  if (!role) {

    sendJson(
      response,
      401,
      {
        error:
          "UNAUTHORIZED"
      }
    );

    return null;

  }

  return role;
}


const users = {

  customer: {
    name:
      "Alex Morgan",

    email:
      "alex.morgan@novabank.test",

    role:
      "customer"
  },

  admin: {
    name:
      "Jordan Admin",

    email:
      "admin@novabank.test",

    role:
      "admin"
  }

};


const server =
  http.createServer(
    async (
      request,
      response
    ) => {

      try {

        const url =
          new URL(
            request.url,
            `http://${request.headers.host || "localhost"}`
          );

        const pathname =
          url.pathname;


        if (
          request.method ===
          "OPTIONS"
        ) {

          response.writeHead(
            204,
            {
              "access-control-allow-origin":
                "*",

              "access-control-allow-headers":
                "Content-Type, Authorization, X-Test-Id",

              "access-control-allow-methods":
                "GET,POST,PATCH,OPTIONS"
            }
          );

          response.end();

          return;

        }


        if (
          pathname ===
          "/__proxy-health"
        ) {

          sendJson(
            response,
            200,
            {
              ok: true,
              service:
                "novabank-qa"
            }
          );

          return;

        }


        if (
          pathname ===
            "/api/session" &&
          request.method ===
            "POST"
        ) {

          const body =
            await readJson(
              request
            );

          const role =
            body.role;

          if (
            role !== "customer" &&
            role !== "admin"
          ) {

            sendJson(
              response,
              400,
              {
                error:
                  "INVALID_ROLE"
              }
            );

            return;

          }

          sendJson(
            response,
            200,
            {
              token:
                role === "admin"
                  ? "novabank-admin-token"
                  : "novabank-customer-token",

              user:
                users[role]
            }
          );

          return;

        }


        if (
          pathname ===
            "/api/logout" &&
          request.method ===
            "POST"
        ) {

          const role =
            requireAuthentication(
              request,
              response
            );

          if (!role) {
            return;
          }

          response.writeHead(
            204,
            {
              "cache-control":
                "no-store"
            }
          );

          response.end();

          return;

        }


        if (
          pathname ===
            "/api/transfers" &&
          request.method ===
            "POST"
        ) {

          const role =
            requireAuthentication(
              request,
              response
            );

          if (!role) {
            return;
          }

          const body =
            await readJson(
              request
            );

          const amount =
            Number(
              body.amount
            );

          if (
            !Number.isFinite(amount) ||
            amount <= 0 ||
            amount > 10000
          ) {

            sendJson(
              response,
              400,
              {
                error:
                  "INVALID_TRANSFER"
              }
            );

            return;

          }

          sendJson(
            response,
            201,
            {
              id:
                `trf_${crypto.randomUUID()}`,

              status:
                "completed"
            }
          );

          return;

        }


        if (
          pathname ===
            "/api/bills/pay" &&
          request.method ===
            "POST"
        ) {

          const role =
            requireAuthentication(
              request,
              response
            );

          if (!role) {
            return;
          }

          const body =
            await readJson(
              request
            );

          const amount =
            Number(
              body.amount
            );

          if (
            !Number.isFinite(amount) ||
            amount <= 0
          ) {

            sendJson(
              response,
              400,
              {
                error:
                  "INVALID_BILL_AMOUNT"
              }
            );

            return;

          }

          sendJson(
            response,
            201,
            {
              id:
                `bill_${crypto.randomUUID()}`,

              status:
                "completed"
            }
          );

          return;

        }


        if (
          pathname ===
            "/api/cards" &&
          request.method ===
            "PATCH"
        ) {

          const role =
            requireAuthentication(
              request,
              response
            );

          if (!role) {
            return;
          }

          const body =
            await readJson(
              request
            );

          if (
            !body.id ||
            ![
              "active",
              "frozen"
            ].includes(
              body.status
            )
          ) {

            sendJson(
              response,
              400,
              {
                error:
                  "INVALID_CARD_UPDATE"
              }
            );

            return;

          }

          sendJson(
            response,
            200,
            {
              id:
                body.id,

              status:
                body.status
            }
          );

          return;

        }


        if (
          pathname ===
            "/api/loans/apply" &&
          request.method ===
            "POST"
        ) {

          const role =
            requireAuthentication(
              request,
              response
            );

          if (!role) {
            return;
          }

          const body =
            await readJson(
              request
            );

          const amount =
            Number(
              body.amount
            );

          const termMonths =
            Number(
              body.termMonths
            );

          if (
            !Number.isFinite(amount) ||
            amount < 1000 ||
            amount > 50000 ||
            ![
              12,
              24,
              36
            ].includes(
              termMonths
            )
          ) {

            sendJson(
              response,
              400,
              {
                error:
                  "INVALID_LOAN_APPLICATION"
              }
            );

            return;

          }

          sendJson(
            response,
            201,
            {
              id:
                `loan_${crypto.randomUUID()}`,

              status:
                "under_review"
            }
          );

          return;

        }


        if (
          pathname ===
            "/api/admin/summary" &&
          request.method ===
            "GET"
        ) {

          const role =
            requireAuthentication(
              request,
              response
            );

          if (!role) {
            return;
          }

          if (
            role !==
            "admin"
          ) {

            sendJson(
              response,
              403,
              {
                error:
                  "FORBIDDEN"
              }
            );

            return;

          }

          sendJson(
            response,
            200,
            {
              active_users:
                1284,

              open_accounts:
                2310,

              pending_reviews:
                17
            }
          );

          return;

        }


        if (
          pathname.startsWith(
            "/api/"
          )
        ) {

          sendJson(
            response,
            404,
            {
              error:
                "NOT_FOUND"
            }
          );

          return;

        }


        sendHtml(
          response
        );

      }
      catch (
        error
      ) {

        console.error(
          error
        );

        if (
          !response.headersSent
        ) {

          sendJson(
            response,
            500,
            {
              error:
                "INTERNAL_SERVER_ERROR"
            }
          );

        }

      }

    }
  );


server.listen(
  PORT,
  "0.0.0.0",
  () => {

    console.log(
      `NovaBank QA listening on port ${PORT}`
    );

  }
);
