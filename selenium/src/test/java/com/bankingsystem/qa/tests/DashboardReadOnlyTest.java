package com.bankingsystem.qa.tests;

import com.bankingsystem.qa.base.CustomerTestBase;
import com.bankingsystem.qa.pages.DashboardCurrentPage;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

public class DashboardReadOnlyTest extends CustomerTestBase {

    private DashboardCurrentPage dashboard() {

        DashboardCurrentPage dashboard =
                new DashboardCurrentPage(
                        getDriver()
                );

        Assert.assertTrue(
                dashboard.isLoaded(),
                "Dashboard should load successfully."
        );

        return dashboard;
    }

    @Test(
            groups = {"smoke", "dashboard"},
            description = "Dashboard loads successfully"
    )
    public void dashboardShouldLoadSuccessfully() {

        DashboardCurrentPage dashboard =
                dashboard();

        Assert.assertEquals(
                dashboard.getPageTitleText(),
                "Dashboard",
                "Dashboard heading should be displayed."
        );
    }

    @Test(
            groups = {"smoke", "dashboard"},
            description = "Total balance is displayed"
    )
    public void totalBalanceShouldBeDisplayed() {

        DashboardCurrentPage dashboard =
                dashboard();

        String totalBalance =
                dashboard.getTotalBalance();

        Assert.assertFalse(
                totalBalance.isBlank(),
                "Total balance should not be blank."
        );

        Assert.assertTrue(
                totalBalance.contains("$"),
                "Total balance should contain a currency symbol."
        );
    }

    @Test(
            groups = {"regression", "dashboard"},
            description = "Dashboard contains checking savings and loan metrics"
    )
    public void expectedDashboardMetricsShouldBeDisplayed() {

        DashboardCurrentPage dashboard =
                dashboard();

        Assert.assertTrue(
                dashboard.containsMetric(
                        "Checking"
                ),
                "Checking metric should be displayed."
        );

        Assert.assertTrue(
                dashboard.containsMetric(
                        "Savings"
                ),
                "Savings metric should be displayed."
        );

        Assert.assertTrue(
                dashboard.containsMetric(
                        "Loan balance"
                ),
                "Loan balance metric should be displayed."
        );
    }

    @Test(
            groups = {"regression", "dashboard"},
            description = "Dashboard metric values display currency"
    )
    public void dashboardMetricValuesShouldDisplayCurrency() {

        DashboardCurrentPage dashboard =
                dashboard();

        Assert.assertTrue(
                dashboard.getMetricCount() >= 3,
                "At least three account/loan metrics should be displayed."
        );

        Assert.assertTrue(
                dashboard.allMetricValuesContainCurrency(),
                "Dashboard metric values should display currency."
        );
    }

    @Test(
            groups = {"smoke", "dashboard", "transactions"},
            description = "Recent transactions are displayed on dashboard"
    )
    public void recentTransactionsShouldBeDisplayed() {

        DashboardCurrentPage dashboard =
                dashboard();

        Assert.assertTrue(
                dashboard.isRecentTransactionsTableDisplayed(),
                "Recent transactions table should be displayed."
        );

        Assert.assertTrue(
                dashboard.getRecentTransactionCount() > 0,
                "At least one recent transaction should be displayed."
        );
    }

    @Test(
            groups = {"regression", "dashboard", "transactions"},
            description = "Recent transaction records contain expected data"
    )
    public void recentTransactionsShouldContainExpectedData() {

        DashboardCurrentPage dashboard =
                dashboard();

        Assert.assertTrue(
                dashboard.allRecentTransactionsHaveContent(),
                "Every recent transaction should contain transaction information."
        );

        List<String> amounts =
                dashboard.getRecentAmounts();

        Assert.assertTrue(
                amounts.stream()
                        .allMatch(amount ->
                                amount.contains("$")
                        ),
                "Every recent transaction should display a currency amount."
        );
    }

    @Test(
            groups = {"regression", "dashboard"},
            description = "Dashboard total matches deterministic account balances"
    )
    public void dashboardTotalShouldMatchSeededBalances() {

        DashboardCurrentPage dashboard =
                dashboard();

        Assert.assertEquals(
                dashboard.getTotalBalance(),
                "$45,340.75",
                "Initial dashboard total should match Checking + Savings balances."
        );
    }
}