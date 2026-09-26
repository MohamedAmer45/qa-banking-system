# Banking System - Selenium Automation

UI automation framework for the Banking System Testing Project.

## Technology Stack

* Java 21
* Selenium WebDriver
* TestNG
* Maven
* Page Object Model
* Allure Reports

## Planned Coverage

* Authentication
* Registration
* Customer dashboard
* Accounts
* Beneficiaries
* Internal transfers
* External transfers
* Scheduled transfers
* Transaction history
* Statements
* Cards
* Loans
* Bill payments
* Profile and settings
* Security functionality
* Session management
* Validation testing
* Boundary testing
* Negative testing
* Role-based functionality
* Cross-browser testing

## Project Structure

```text
selenium/
│
├── pom.xml
├── README.md
├── screenshots/
│
└── src/
    └── test/
        ├── java/
        │   └── com/
        │       └── bankingsystem/
        │           └── qa/
        │               ├── base/
        │               ├── listeners/
        │               ├── pages/
        │               ├── tests/
        │               │   └── FrameworkSmokeTest.java
        │               └── utils/
        │
        └── resources/
            └── config.properties
```

## Package Responsibilities

### base

Contains shared test setup and teardown logic.

Planned classes include:

* BaseTest
* DriverFactory

### pages

Contains Page Object Model classes representing pages and major UI components in the banking application.

Examples:

* LoginPage
* RegistrationPage
* DashboardPage
* AccountsPage
* TransferPage
* BeneficiariesPage
* CardsPage
* LoansPage
* BillPaymentsPage
* ProfilePage

### tests

Contains Selenium automated test classes organized by banking functionality.

Examples:

* LoginTests
* RegistrationTests
* AccountTests
* TransferTests
* BeneficiaryTests
* TransactionTests
* CardTests
* LoanTests
* SecurityTests

### utils

Contains reusable framework utilities.

Examples:

* ConfigReader
* ScreenshotUtils
* WaitUtils
* TestDataUtils

### listeners

Contains TestNG listeners used for reporting, screenshots, logging, and test lifecycle handling.

### resources

Contains configuration files and test resources.

Current configuration:

```properties
browser=chrome
headless=false
explicit.wait.seconds=10
page.load.timeout.seconds=30
```

## Automation Design

The framework will follow the Page Object Model pattern.

Tests will contain the test logic and assertions, while page classes will contain:

* Locators
* Page interactions
* Navigation actions
* Reusable UI operations

Shared browser setup and teardown will be handled through the base framework.

## Browser Management

Selenium Manager will be used for automatic browser driver management.

This avoids manually downloading and configuring drivers such as:

* ChromeDriver
* GeckoDriver
* EdgeDriver

## Supported Browsers

The framework is planned to support:

* Google Chrome
* Microsoft Edge
* Mozilla Firefox

Browser selection will be controlled using configuration or Maven parameters.

## Test Coverage Strategy

The Selenium suite will cover:

### Functional Testing

Verify that banking features work according to their requirements and business rules.

### Positive Testing

Verify valid customer workflows.

Examples:

* Successful login
* Successful money transfer
* Adding a valid beneficiary
* Paying a valid bill

### Negative Testing

Verify application behavior when invalid data or actions are submitted.

Examples:

* Invalid login credentials
* Invalid account numbers
* Transfers exceeding available balance
* Invalid beneficiary details

### Boundary Testing

Test values around defined limits.

Examples:

* Minimum and maximum transfer amounts
* Field length limits
* Daily transaction limits

### Validation Testing

Verify client-side and server-side input validation.

### Security-Oriented UI Testing

Validate applicable frontend security behavior such as:

* Session expiration
* Unauthorized navigation
* Sensitive information masking
* Logout behavior
* Role-based access restrictions

### Cross-Browser Testing

Execute critical banking workflows across supported browsers.

### Regression Testing

Automate critical workflows so they can be repeatedly executed after application changes.

## Running the Tests

Navigate to the Selenium directory:

```bash
cd selenium
```

Run all tests:

```bash
mvn clean test
```

That targets the deployed application at `https://novabank-banking-system.vercel.app`,
so no local server or database is needed. To run against localhost instead:

```bash
mvn clean test -Dbase.url=http://localhost:3000
```

The deployed environment has one shared database that is never reset, so
balance assertions here are relative rather than absolute (`LIM-005` in
`docs/known-issues-and-limitations.md`).

## Expected Framework Validation

The initial framework smoke test should produce:

```text
Tests run: 1, Failures: 0, Errors: 0, Skipped: 0

BUILD SUCCESS
```

## Reporting

Allure Reports will be integrated into the framework.

The final reporting implementation will include:

* Test status
* Test execution duration
* Failure details
* Screenshots on failure
* Test descriptions
* Test severity
* Feature grouping
* User story grouping

## Screenshots

Failed Selenium tests will automatically capture screenshots.

Screenshots will be stored in:

```text
selenium/screenshots/
```

## CI/CD

The Selenium suite is integrated with GitHub Actions through `.github/workflows/selenium.yml`.

The workflow:

* Headless browser execution
* Automated regression testing
* Java 21 and Maven dependency caching
* QA-environment availability checks
* Push, pull-request, and manual execution
* Surefire and Allure artifact upload with 14-day retention
* Cancellation of superseded runs for the same Git reference

Jenkins integration remains planned.

## Relationship With Manual Tests

The Selenium automation suite will be based on the manual test scenarios and test cases created during Step 2 of the Banking System Testing Project.

Automation will prioritize:

* Critical business flows
* High-risk functionality
* Frequently executed regression tests
* Data-driven scenarios
* Stable functionality
* Repetitive tests

Not every manual test case will necessarily be automated.

Exploratory, usability, visual, and scenarios requiring human judgment may remain manual.

## Selenium Framework Development Sequence

The framework has been developed incrementally in the following order. GitHub Actions integration is complete; Jenkins remains planned:

1. Maven and dependency setup
2. Framework smoke test
3. ConfigReader
4. DriverFactory
5. BaseTest
6. Selenium Manager configuration
7. Browser configuration
8. Page Object Model
9. Authentication automation
10. Banking module automation
11. Wait utilities
12. Screenshot handling
13. TestNG listeners
14. Data-driven testing
15. Cross-browser testing
16. Parallel execution
17. Allure reporting
18. Regression suites
19. GitHub Actions integration (complete)
20. Jenkins integration (planned)

## Project Goal

The goal of this Selenium framework is to demonstrate a maintainable, scalable, and professional UI automation solution for a fully functional banking application.

The framework forms part of a larger Banking System QA portfolio covering:

* Manual testing
* Selenium
* Cypress
* Playwright
* API testing
* Database testing
* Performance testing
* BDD
* CI/CD
* Automated reporting

<!-- NOVABANK-SELENIUM-MIGRATION-START -->

## Rebuilt NovaBank UI Migration

The Selenium suite has been synchronized with the currently deployed NovaBank application.

### Previous Authentication Flow

`Email/password -> MFA -> Dashboard`

### Current QA Authentication Flow

`Session selection -> Customer/Admin -> Dashboard`

### Migrated Selenium Coverage

- Session page
- Customer authentication
- Admin authentication
- Logout
- Session security
- Dashboard
- Accounts
- Transactions
- Transfers
- Transfer boundaries
- Bills
- Bill validation
- Cards
- Card freeze/unfreeze
- Loans
- Loan boundaries
- Notifications
- Profile
- Admin console
- Role authorization

### Blocked Legacy Coverage

The following existing automation remains retained but blocked:

- MFA
- Beneficiaries
- Account creation
- Extended account controls
- Dedicated statements

These tests remain because their underlying banking requirements remain valid.

### State Isolation

Current business state is stored in `localStorage`.

Authentication is stored in `sessionStorage`.

Tests that change balances, cards, transactions, loan applications, or notifications should restore or isolate state.

### Application

`https://novabank-qa-proxy.onrender.com`

<!-- NOVABANK-SELENIUM-MIGRATION-END -->