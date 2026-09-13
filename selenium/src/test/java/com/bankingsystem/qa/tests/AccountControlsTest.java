package com.bankingsystem.qa.tests;

import com.bankingsystem.qa.base.CustomerTestBase;
import com.bankingsystem.qa.pages.AccountsPage;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

public class AccountControlsTest extends CustomerTestBase {

    private AccountsPage accountsPage;

    @BeforeMethod(alwaysRun = true)
    public void openAccountsPage() {

        accountsPage =
                dashboardPage.openAccounts();

        Assert.assertTrue(
                accountsPage.isLoaded(),
                "Accounts page should load successfully."
        );
    }


    @Test
    public void accountControlTableShouldMatchDisplayedAccounts() {

        Assert.assertEquals(
                accountsPage.getAccountControlRowCount(),
                accountsPage.getAccountCount(),
                "Each displayed account should have one control-table row."
        );
    }


    @Test
    public void activeAccountsShouldExposeFreezeControl() {

        int activeAccounts =
                accountsPage.getActiveAccountCount();

        int freezeButtons =
                accountsPage.getFreezeButtonCount();

        Assert.assertTrue(
                activeAccounts > 0,
                "Seeded customer should have at least one active account."
        );

        Assert.assertEquals(
                freezeButtons,
                activeAccounts,
                "Every active account should expose a Freeze control."
        );
    }


    @Test
    public void accountControlDataShouldBeDisplayed() {

        List<String> statuses =
                accountsPage.getAccountStatuses();

        List<String> limits =
                accountsPage.getDailyLimits();

        Assert.assertEquals(
                statuses.size(),
                accountsPage.getAccountCount(),
                "Every account should display a status."
        );

        Assert.assertEquals(
                limits.size(),
                accountsPage.getAccountCount(),
                "Every account should display a daily limit."
        );


        for (String status : statuses) {

            Assert.assertFalse(
                    status.isBlank(),
                    "Account status should not be empty."
            );
        }


        for (String limit : limits) {

            Assert.assertFalse(
                    limit.isBlank(),
                    "Daily transfer limit should not be empty."
            );
        }
    }


    @Test
    public void accountIdentifiersShouldBeMasked() {

        List<String> metadata =
                accountsPage.getAccountMetadata();

        for (String value : metadata) {

            Assert.assertTrue(
                    value.contains(
                            "\u2022\u2022\u2022\u2022"
                    ),
                    "Account identifier should be masked in the UI. Value: "
                            + value
            );
        }
    }
}
