package com.bankingsystem.qa.bdd.steps;

import com.bankingsystem.qa.bdd.context.TestContext;
import com.bankingsystem.qa.bdd.pages.AccountsCurrentPage;
import com.bankingsystem.qa.bdd.pages.CustomerNavigationPage;
import com.bankingsystem.qa.bdd.pages.DashboardCurrentPage;
import com.bankingsystem.qa.bdd.pages.LoginPage;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import org.testng.Assert;

import java.util.List;
import java.util.Locale;

public final class CustomerOverviewSteps {

    private final TestContext context;

    public CustomerOverviewSteps(TestContext context) {
        this.context = context;
    }

    @Given("a customer is authenticated in NovaBank")
    public void customerIsAuthenticated() {

        LoginPage loginPage =
                new LoginPage(
                        context.getDriver()
                ).open();

        loginPage.enterAsCustomer();

        Assert.assertTrue(
                loginPage.getLoggedInUserText()
                        .toLowerCase(Locale.ROOT)
                        .contains("customer"),
                "Customer session should be authenticated."
        );

        DashboardCurrentPage dashboardPage =
                new DashboardCurrentPage(
                        context.getDriver()
                );

        Assert.assertTrue(
                dashboardPage.isLoaded(),
                "Customer dashboard should load."
        );

        context.setLoginPage(loginPage);
        context.setDashboardPage(dashboardPage);
    }

    @Then("the dashboard heading should be {string}")
    public void dashboardHeadingShouldBe(
            String expectedHeading
    ) {

        Assert.assertEquals(
                context.getDashboardPage()
                        .getPageTitleText(),
                expectedHeading,
                "Dashboard heading should match."
        );
    }

    @Then("the total balance should display a currency value")
    public void totalBalanceShouldDisplayCurrency() {

        String totalBalance =
                context.getDashboardPage()
                        .getTotalBalance();

        Assert.assertFalse(
                totalBalance.isBlank(),
                "Total balance should not be blank."
        );

        Assert.assertTrue(
                totalBalance.contains("$"),
                "Total balance should contain a currency symbol."
        );
    }

    @Then("the dashboard should contain these metrics:")
    public void dashboardShouldContainMetrics(
            DataTable dataTable
    ) {

        DashboardCurrentPage dashboard =
                context.getDashboardPage();

        for (String metric : dataTable.asList()) {
            Assert.assertTrue(
                    dashboard.containsMetric(metric),
                    "Dashboard should contain metric: " +
                    metric
            );
        }
    }

    @Then("every dashboard metric should display a currency value")
    public void everyMetricShouldDisplayCurrency() {

        DashboardCurrentPage dashboard =
                context.getDashboardPage();

        Assert.assertTrue(
                dashboard.getMetricCount() >= 3,
                "At least three dashboard metrics should be displayed."
        );

        Assert.assertTrue(
                dashboard.allMetricValuesContainCurrency(),
                "Every dashboard metric should display currency."
        );
    }

    @Then("the recent transactions table should be displayed")
    public void recentTransactionsTableShouldBeDisplayed() {

        Assert.assertTrue(
                context.getDashboardPage()
                        .isRecentTransactionsTableDisplayed(),
                "Recent transactions table should be displayed."
        );
    }

    @Then("at least one recent transaction should be listed")
    public void recentTransactionShouldBeListed() {

        Assert.assertTrue(
                context.getDashboardPage()
                        .getRecentTransactionCount() > 0,
                "At least one recent transaction should be listed."
        );
    }

    @Then("every recent transaction should contain complete currency data")
    public void recentTransactionsShouldContainCompleteData() {

        DashboardCurrentPage dashboard =
                context.getDashboardPage();

        Assert.assertTrue(
                dashboard.allRecentTransactionsHaveContent(),
                "Every recent transaction should contain information."
        );

        Assert.assertTrue(
                dashboard.getRecentStatuses()
                        .stream()
                        .allMatch(status ->
                                status != null &&
                                !status.isBlank()
                        ),
                "Every recent transaction should contain a status."
        );

        Assert.assertTrue(
                dashboard.getRecentAmounts()
                        .stream()
                        .allMatch(amount ->
                                amount != null &&
                                !amount.isBlank() &&
                                amount.contains("$")
                        ),
                "Every recent transaction should contain a currency amount."
        );
    }

    @When("the customer opens the account overview")
    public void customerOpensAccountOverview() {

        CustomerNavigationPage navigation =
                new CustomerNavigationPage(
                        context.getDriver()
                );

        AccountsCurrentPage accountsPage =
                navigation.openAccounts();

        Assert.assertTrue(
                accountsPage.isLoaded(),
                "Accounts page should load."
        );

        context.setAccountsPage(accountsPage);
    }

    @Then("the accounts heading should be {string}")
    public void accountsHeadingShouldBe(
            String expectedHeading
    ) {

        Assert.assertEquals(
                context.getAccountsPage()
                        .getPageTitleText(),
                expectedHeading,
                "Accounts heading should match."
        );
    }

    @Then("at least one customer account should be displayed")
    public void customerAccountShouldBeDisplayed() {

        Assert.assertTrue(
                context.getAccountsPage()
                        .getAccountCount() > 0,
                "At least one account should be displayed."
        );
    }

    @Then("the account overview should contain these account types:")
    public void accountOverviewShouldContainTypes(
            DataTable dataTable
    ) {

        AccountsCurrentPage accountsPage =
                context.getAccountsPage();

        for (String accountType : dataTable.asList()) {
            Assert.assertTrue(
                    accountsPage.containsAccountType(
                            accountType
                    ),
                    "Accounts page should contain: " +
                    accountType
            );
        }
    }

    @Then("every account should display a currency balance")
    public void everyAccountShouldDisplayCurrencyBalance() {

        AccountsCurrentPage accountsPage =
                context.getAccountsPage();

        List<String> balances =
                accountsPage.getAccountBalances();

        Assert.assertEquals(
                balances.size(),
                accountsPage.getAccountCount(),
                "Every account should display a balance."
        );

        Assert.assertTrue(
                accountsPage.allBalancesContainCurrency(),
                "Every account balance should display currency."
        );
    }

    @Then("every account identifier should be masked")
    public void everyAccountIdentifierShouldBeMasked() {

        AccountsCurrentPage accountsPage =
                context.getAccountsPage();

        Assert.assertEquals(
                accountsPage.getAccountNumbers().size(),
                accountsPage.getAccountCount(),
                "Every account should display an identifier."
        );

        Assert.assertTrue(
                accountsPage.allAccountNumbersAreMasked(),
                "Every account identifier should be masked."
        );
    }
}