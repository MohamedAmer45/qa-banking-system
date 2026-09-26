/*
 * NovaBank QA — full regression on Jenkins.
 *
 * GitHub Actions runs each suite in its own job, each with a private
 * PostgreSQL service container. Jenkins here runs them against ONE application
 * instance, which changes what is safe to do in parallel; see the comment above
 * the suite stages.
 *
 * Agent requirements. This pipeline deliberately does not use a `tools` block:
 * those refer to tool installations configured on the Jenkins controller by
 * name, so a Jenkinsfile that names them fails on any Jenkins that happens to
 * call them something else. The Preflight stage checks for what it needs and
 * says plainly what is missing instead.
 *
 *   node >= 22, npm, java 21, mvn, git, curl, and a PostgreSQL reachable at
 *   the URL in the NOVABANK_DATABASE_URL credential.
 *
 * Plugins, beyond what Declarative Pipeline itself needs:
 *
 *   JUnit                 the `junit` step, for per-test results
 *   Credentials Binding   the `credentials()` helper
 *   Timestamper           the `timestamps()` option
 *
 * Credentials: a Secret text credential with the id NOVABANK_DATABASE_URL,
 * holding the same connection string the application uses.
 */
pipeline {
  agent any

  options {
    timestamps()
    timeout(time: 90, unit: 'MINUTES')
    buildDiscarder(logRotator(numToKeepStr: '20', artifactNumToKeepStr: '10'))
    disableConcurrentBuilds()
  }

  parameters {
    string(
      name: 'BASE_URL',
      defaultValue: '',
      description: 'Target an existing deployment instead of starting one. ' +
                   'Leave empty to start NovaBank on this agent, which is the ' +
                   'only mode where the database is reset and the results are ' +
                   'repeatable.'
    )
    booleanParam(
      name: 'RUN_PERFORMANCE',
      defaultValue: false,
      description: 'Include the JMeter shapes. Off by default: they take ' +
                   'several minutes and the concurrency plan drains the ' +
                   'seeded accounts it runs against.'
    )
    string(
      name: 'APP_REF',
      defaultValue: 'main',
      description: 'Ref of novabank-banking-system to test against.'
    )
  }

  environment {
    APP_DIR      = '.novabank-app'
    APP_REPO     = 'https://github.com/MohamedAmer45/novabank-banking-system.git'
    APP_PORT     = '3000'
    /*
     * The same connection string the application uses, so the database suite
     * and the application can never end up on different databases. Supplied as
     * a Jenkins credential rather than committed, which is also why the
     * database suite has no default for it anywhere in this repository.
     */
    DATABASE_URL = credentials('NOVABANK_DATABASE_URL')
    DATABASE_SSL = 'false'
    QA_MODE      = 'true'
  }

  stages {
    stage('Preflight') {
      steps {
        sh '''
          set -e
          missing=""
          for tool in node npm java mvn git curl; do
            command -v "$tool" >/dev/null 2>&1 || missing="$missing $tool"
          done

          if [ -n "$missing" ]; then
            echo "This agent is missing:$missing"
            echo "See the header of the Jenkinsfile for what the pipeline needs."
            exit 1
          fi

          echo "node $(node --version), java $(java -version 2>&1 | head -1), $(mvn -v | head -1)"
        '''
      }
    }

    stage('Start NovaBank') {
      when { expression { return !params.BASE_URL?.trim() } }
      steps {
        sh '''
          set -e
          rm -rf "$APP_DIR"
          git clone --depth 1 --branch "$APP_REF" "$APP_REPO" "$APP_DIR"

          cd "$APP_DIR"
          npm ci
          node scripts/migrate.js --reset
          node scripts/seed.js

          nohup node server.js > "$WORKSPACE/novabank-server.log" 2>&1 &
          echo $! > "$WORKSPACE/novabank.pid"

          for i in $(seq 1 30); do
            if curl -fsS "http://127.0.0.1:${APP_PORT}/api/health" >/dev/null; then
              echo "NovaBank is healthy."
              exit 0
            fi
            sleep 1
          done

          echo "NovaBank did not become healthy."
          cat "$WORKSPACE/novabank-server.log" || true
          exit 1
        '''
      }
    }

    stage('Resolve target') {
      steps {
        script {
          env.TARGET_URL = params.BASE_URL?.trim()
            ? params.BASE_URL.trim()
            : "http://127.0.0.1:${env.APP_PORT}"
          echo "Suites will run against ${env.TARGET_URL}"
        }
      }
    }

    /*
     * Sequential on purpose.
     *
     * Every suite below moves money through the same seeded accounts, and here
     * they share one application and one database. Run in parallel they would
     * observe each other's balances and fail on arithmetic none of them
     * controlled — the order-dependence recorded as LIM-005.
     *
     * GitHub Actions parallelises them safely only because each workflow gets
     * its own application and its own throwaway database. Reproducing that here
     * means an instance per suite, which is a bigger change than it looks.
     */
    stage('API — REST Assured') {
      steps {
        dir('rest-assured') {
          sh 'mvn --batch-mode --no-transfer-progress clean test -Dapi.base.url="$TARGET_URL"'
        }
      }
    }

    stage('Database — JDBC') {
      steps {
        dir('database-testing') {
          sh 'mvn --batch-mode --no-transfer-progress clean test -Dapi.base.url="$TARGET_URL"'
        }
      }
    }

    stage('Postman — Newman') {
      steps {
        dir('postman') {
          sh '''
            npm ci
            npx newman run NovaBank.postman_collection.json \
              --env-var baseUrl="$TARGET_URL" \
              --reporters cli,junit \
              --reporter-junit-export results/newman-junit.xml
          '''
        }
      }
    }

    stage('UI — Playwright') {
      steps {
        dir('playwright') {
          sh '''
            npm ci
            npx playwright install --with-deps chromium
            BASE_URL="$TARGET_URL" npx playwright test --project=chromium
          '''
        }
      }
    }

    stage('UI — Cypress') {
      steps {
        dir('cypress') {
          sh '''
            npm ci
            CYPRESS_BASE_URL="$TARGET_URL" npx cypress run --browser electron
          '''
        }
      }
    }

    stage('UI — Selenium') {
      steps {
        dir('selenium') {
          sh '''
            mvn --batch-mode --no-transfer-progress clean test \
              -Dheadless=true -Dbrowser=chrome -Dbase.url="$TARGET_URL"
          '''
        }
      }
    }

    stage('BDD — Cucumber') {
      steps {
        dir('cucumber') {
          sh '''
            mvn --batch-mode --no-transfer-progress clean test \
              -Dheadless=true -Dbrowser=chrome -Dbase.url="$TARGET_URL"
          '''
        }
      }
    }

    stage('Performance — JMeter') {
      when { expression { return params.RUN_PERFORMANCE } }
      steps {
        dir('jmeter') {
          sh '''
            set -e
            if [ -z "${JMETER_HOME:-}" ] && ! command -v jmeter >/dev/null 2>&1; then
              echo "JMeter not on this agent. Set JMETER_HOME or install it."
              exit 1
            fi

            HOST=$(echo "$TARGET_URL" | sed -E 's|https?://([^:/]+).*|\\1|')
            PORT=$(echo "$TARGET_URL" | sed -nE 's|https?://[^:/]+:([0-9]+).*|\\1|p')
            PROTOCOL=$(echo "$TARGET_URL" | sed -E 's|(https?)://.*|\\1|')

            export JMETER_HOST="$HOST"
            export JMETER_PORT="${PORT:-3000}"
            export JMETER_PROTOCOL="$PROTOCOL"

            bash run.sh load 20 60
            bash run.sh concurrency 30 50
          '''
        }
      }
    }

    stage('Ledger reconciliation') {
      steps {
        sh '''
          if [ -d "$APP_DIR" ]; then
            cd "$APP_DIR" && npm run db:check
          else
            echo "Targeting an external deployment; skipping the ledger check, "
            echo "which needs the application's own database tooling."
          fi
        '''
      }
    }
  }

  post {
    always {
      // Every suite emits JUnit XML so results are per-test rather than
      // per-stage. Playwright gained a junit reporter for this.
      junit(
        allowEmptyResults: true,
        testResults: [
          'rest-assured/target/surefire-reports/*.xml',
          'database-testing/target/surefire-reports/*.xml',
          'selenium/target/surefire-reports/*.xml',
          'cucumber/target/surefire-reports/*.xml',
          'playwright/results/junit/*.xml',
          'cypress/cypress/results/junit/*.xml',
          'postman/results/newman-junit.xml'
        ].join(',')
      )

      archiveArtifacts(
        allowEmptyArchive: true,
        artifacts: [
          'novabank-server.log',
          'playwright/playwright-report/**',
          'cypress/cypress/results/**',
          'postman/results/**',
          'jmeter/results/**'
        ].join(',')
      )

      sh '''
        if [ -f "$WORKSPACE/novabank.pid" ]; then
          kill "$(cat "$WORKSPACE/novabank.pid")" 2>/dev/null || true
          rm -f "$WORKSPACE/novabank.pid"
        fi
      '''
    }

    failure {
      sh 'tail -100 "$WORKSPACE/novabank-server.log" || true'
    }

    cleanup {
      sh 'rm -rf "$APP_DIR"'
    }
  }
}
