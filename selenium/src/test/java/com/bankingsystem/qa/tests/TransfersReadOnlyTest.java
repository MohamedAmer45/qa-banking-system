package com.bankingsystem.qa.tests;

import com.bankingsystem.qa.base.CustomerTestBase;
import com.bankingsystem.qa.pages.TransfersPage;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

public class TransfersReadOnlyTest extends CustomerTestBase {

    private TransfersPage transfersPage;


    @BeforeMethod(alwaysRun = true)
    public void openTransfersPage() {

        transfersPage =
                dashboardPage.openTransfers();

        Assert.assertTrue(
                transfersPage.isLoaded(),
                "Transfers page should load successfully."
        );
    }


    @Test(
            enabled = false,
            description = "Known defect BUG-AUTH-001: concurrent authenticated requests intermittently return 401."
    )
    public void transfersPageShouldOpenSuccessfully() {

        Assert.assertEquals(
                transfersPage.getPageTitleText(),
                "Transfers",
                "Page title should be Transfers."
        );

        Assert.assertTrue(
                transfersPage.isTransferHistoryDisplayed(),
                "Transfer history section should be displayed."
        );
    }


    @Test(
            enabled = false,
            description = "Known defect BUG-AUTH-001: concurrent authenticated requests intermittently return 401."
    )
    public void newTransferModalShouldDisplayRequiredControls() {

        transfersPage.openNewTransferModal();

        Assert.assertTrue(
                transfersPage.isTransferFormDisplayed(),
                "New Transfer form should be displayed."
        );

        Assert.assertTrue(
                transfersPage.getSourceAccountOptions().size() > 0,
                "At least one active source account should be available."
        );

        Assert.assertTrue(
                transfersPage.isAmountFieldDisplayed(),
                "Amount field should be displayed."
        );

        Assert.assertTrue(
                transfersPage.isScheduleFieldDisplayed(),
                "Schedule field should be displayed."
        );

        Assert.assertTrue(
                transfersPage.isMemoFieldDisplayed(),
                "Memo field should be displayed."
        );

        Assert.assertTrue(
                transfersPage.isSubmitButtonDisplayed(),
                "Review and submit button should be displayed."
        );
    }


    @Test(
            enabled = false,
            description = "Known defect BUG-AUTH-001: concurrent authenticated requests intermittently return 401."
    )
    public void destinationTypeShouldToggleDestinationControls() {

        transfersPage.openNewTransferModal();

        List<String> destinationTypes =
                transfersPage.getDestinationTypeOptions();

        Assert.assertTrue(
                destinationTypes.contains("Beneficiary"),
                "Beneficiary destination type should be available."
        );

        Assert.assertTrue(
                destinationTypes.contains("My account"),
                "Own-account destination type should be available."
        );


        Assert.assertTrue(
                transfersPage.isBeneficiaryFieldDisplayed(),
                "Beneficiary field should be visible by default."
        );

        Assert.assertFalse(
                transfersPage.isOwnAccountFieldDisplayed(),
                "Own-account field should initially be hidden."
        );


        transfersPage.selectDestinationType(
                "own"
        );

        Assert.assertTrue(
                transfersPage.isOwnAccountFieldDisplayed(),
                "Own-account field should be displayed after selecting My account."
        );

        Assert.assertFalse(
                transfersPage.isBeneficiaryFieldDisplayed(),
                "Beneficiary field should be hidden for own-account transfers."
        );

        Assert.assertTrue(
                transfersPage.getOwnAccountOptionCount() > 0,
                "At least one destination account should be available."
        );


        transfersPage.selectDestinationType(
                "beneficiary"
        );

        Assert.assertTrue(
                transfersPage.isBeneficiaryFieldDisplayed(),
                "Beneficiary field should reappear."
        );

        Assert.assertFalse(
                transfersPage.isOwnAccountFieldDisplayed(),
                "Own-account field should be hidden again."
        );
    }


    @Test(
            enabled = false,
            description = "Known defect BUG-AUTH-001: concurrent authenticated requests intermittently return 401."
    )
    public void transferAmountAndMemoConstraintsShouldMatchRequirements() {

        transfersPage.openNewTransferModal();

        Assert.assertEquals(
                transfersPage.getAmountMinimum(),
                "0.01",
                "Minimum transfer amount should be 0.01."
        );

        Assert.assertEquals(
                transfersPage.getAmountStep(),
                "0.01",
                "Transfer amount should support two decimal places."
        );

        Assert.assertEquals(
                transfersPage.getScheduleFieldType(),
                "datetime-local",
                "Schedule field should use datetime-local."
        );

        Assert.assertEquals(
                transfersPage.getMemoMaxLength(),
                "80",
                "Transfer memo should be limited to 80 characters."
        );
    }


    @Test(
            enabled = false,
            description = "Known defect BUG-AUTH-001: concurrent authenticated requests intermittently return 401."
    )
    public void transferFormShouldExposeQaFailureSimulation() {

        transfersPage.openNewTransferModal();

        List<String> qaOptions =
                transfersPage.getQaSimulationOptions();

        Assert.assertTrue(
                qaOptions.contains("Normal"),
                "Normal transfer behavior should be available."
        );

        Assert.assertTrue(
                qaOptions.contains("Force safe failure / rollback"),
                "Safe failure simulation should be available."
        );
    }
}
