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