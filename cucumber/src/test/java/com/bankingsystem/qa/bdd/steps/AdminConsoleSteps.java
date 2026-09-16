package com.bankingsystem.qa.bdd.steps;

import com.bankingsystem.qa.bdd.context.TestContext;
import com.bankingsystem.qa.bdd.pages.AdminCurrentPage;
import com.bankingsystem.qa.bdd.pages.LoginPage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import org.testng.Assert;

import java.util.Locale;
import java.util.Map;

public class AdminConsoleSteps {

    private final TestContext testContext;
    private AdminCurrentPage adminPage;

    public AdminConsoleSteps(TestContext testContext) {

        this.testContext = testContext;
    }

    private AdminCurrentPage requireAdminPage() {

        Assert.assertNotNull(
                adminPage,
                "The Admin console must be opened before using it."
        );

        return adminPage;
    }

    @Given("an authenticated administrator opens the Admin console")
    public void anAuthenticatedAdministratorOpensTheAdminConsole() {

        LoginPage loginPage =
                new LoginPage(
                        testContext.getDriver()
                );

        loginPage.open();

        Assert.assertTrue(
                loginPage.isLoaded(),
                "NovaBank session page should load."
        );

        loginPage.enterAsAdmin();

        Assert.assertTrue(
                loginPage.isApplicationDisplayed(),
                "NovaBank application should be displayed for the administrator."
        );

        Assert.assertTrue(
                loginPage.getLoggedInUserText()
                        .toLowerCase(Locale.ROOT)
                        .contains("admin"),
                "Authenticated session should use the admin role."
        );

        Assert.assertTrue(
                loginPage.isAdminNavigationDisplayed(),
                "Admin navigation should be available to the administrator."
        );

        adminPage =
                new AdminCurrentPage(
                        testContext.getDriver()
                );

        adminPage.open();

        Assert.assertTrue(
                adminPage.isLoaded(),
                "Admin console should load successfully."
        );
    }

    @Then("the Admin console page should be displayed")
    public void theAdminConsolePageShouldBeDisplayed() {

        AdminCurrentPage page =
                requireAdminPage();

        Assert.assertTrue(
                page.isLoaded(),
                "Admin console should be visible."
        );

        Assert.assertEquals(
                page.getPageTitleText(),
                "Admin console",
                "Admin console heading should be displayed."
        );
    }

    @Then("at least three admin summary metrics should be displayed")
    public void atLeastThreeAdminSummaryMetricsShouldBeDisplayed() {

        Assert.assertTrue(
                requireAdminPage().getMetricCount() >= 3,
                "At least three admin summary metrics should be displayed."
        );
    }

    @Then("every admin summary metric should contain a value")
    public void everyAdminSummaryMetricShouldContainAValue() {

        Assert.assertTrue(
                requireAdminPage().allMetricsHaveValues(),
                "Every admin metric should contain a value."
        );
    }

    @Then("all current admin banking metrics should be available")
    public void allCurrentAdminBankingMetricsShouldBeAvailable() {

        Map<String, String> metrics =
                requireAdminPage().getMetrics();

        Assert.assertTrue(
                metrics.containsKey("active users"),
                "Active users metric should be displayed."
        );

        Assert.assertTrue(
                metrics.containsKey("open accounts"),
                "Open accounts metric should be displayed."
        );

        Assert.assertTrue(
                metrics.containsKey("pending reviews"),
                "Pending reviews metric should be displayed."
        );
    }

    @Then("the admin metrics should match the current seeded QA data")
    public void theAdminMetricsShouldMatchTheCurrentSeededQaData() {

        AdminCurrentPage page =
                requireAdminPage();

        Assert.assertEquals(
                page.getMetricValue("active users"),
                "1,284",
                "Active user count should match seeded QA data."
        );

        Assert.assertEquals(
                page.getMetricValue("open accounts"),
                "2,310",
                "Open account count should match seeded QA data."
        );

        Assert.assertEquals(
                page.getMetricValue("pending reviews"),
                "17",
                "Pending review count should match seeded QA data."
        );
    }
}
