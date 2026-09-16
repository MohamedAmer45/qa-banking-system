# NovaBank Cucumber BDD Framework

BDD automation for selected NovaBank business workflows.

## Stack

- Java 21
- Maven
- Cucumber-JVM
- Selenium WebDriver
- TestNG
- PicoContainer
- Allure
- Cucumber HTML, JSON, and JUnit reports

## Run all scenarios

```powershell
mvn clean test
```

## Run smoke scenarios

```powershell
mvn test "-Dcucumber.filter.tags=@smoke"
```

## Run headlessly

```powershell
mvn test -Dheadless=true
```

## Run in another browser

```powershell
mvn test -Dbrowser=edge
mvn test -Dbrowser=firefox
```

## Configuration priority

1. Maven/system property
2. Environment variable
3. `config.properties`

Examples:

- `base.url` becomes environment variable `BASE_URL`
- `page.load.timeout.seconds` becomes `PAGE_LOAD_TIMEOUT_SECONDS`

## Reports

After execution:

- HTML: `target/cucumber-reports/cucumber.html`
- JSON: `target/cucumber-reports/cucumber.json`
- JUnit XML: `target/cucumber-reports/cucumber.xml`
- Allure results: `target/allure-results`

## Current Coverage

The framework covers every module currently testable through the deployed NovaBank interface:

- Customer and administrator authentication
- Dashboard and account overview
- Customer navigation
- Transfers and transaction history
- Bill payments
- Card management
- Loan applications
- Notifications and customer profile
- Administrator console
- Role-based authorization
- Session security and logout protection

MFA, beneficiary management, account creation, extended account controls, dedicated statements, and persistent database-backed behavior remain blocked because they are unavailable in the current deployment.

Unavailable behavior is not represented by false passing scenarios.

Each scenario receives an isolated browser session. Failed scenarios include a screenshot when the browser remains available.
