package com.bankingsystem.qa.tests;

import com.bankingsystem.qa.base.CustomerTestBase;
import com.bankingsystem.qa.pages.BeneficiariesPage;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

public class BeneficiariesReadOnlyTest extends CustomerTestBase {

    private BeneficiariesPage beneficiariesPage;

    @BeforeMethod(alwaysRun = true)
    public void openBeneficiariesPage() {

        beneficiariesPage =
                dashboardPage.openBeneficiaries();

        Assert.assertTrue(
                beneficiariesPage.isLoaded(),
                "Beneficiaries page should load successfully."
        );
    }


    @Test
    public void beneficiariesPageShouldOpenSuccessfully() {

        Assert.assertEquals(
                beneficiariesPage.getPageTitleText(),
                "Beneficiaries",
                "Page title should be Beneficiaries."
        );
    }


    @Test
    public void addBeneficiaryModalShouldDisplayRequiredFields() {

        beneficiariesPage.openAddBeneficiaryModal();

        Assert.assertTrue(
                beneficiariesPage.isAddBeneficiaryModalDisplayed(),
                "Add Beneficiary modal should be displayed."
        );

        Assert.assertTrue(
                beneficiariesPage.isNameFieldDisplayed(),
                "Beneficiary name field should be displayed."
        );

        Assert.assertTrue(
                beneficiariesPage.isNicknameFieldDisplayed(),
                "Nickname field should be displayed."
        );

        Assert.assertTrue(
                beneficiariesPage.isAccountIdentifierFieldDisplayed(),
                "Account number or IBAN field should be displayed."
        );

        beneficiariesPage.closeModal();
    }


    @Test
    public void addBeneficiaryModalShouldDisplaySupportedOptions() {

        beneficiariesPage.openAddBeneficiaryModal();

        List<String> banks =
                beneficiariesPage.getBankOptions();

        Assert.assertTrue(
                banks.contains("NOVABANK"),
                "NovaBank should be available."
        );

        Assert.assertTrue(
                banks.contains("NILE EXTERNAL BANK"),
                "Nile External Bank should be available."
        );

        Assert.assertTrue(
                banks.contains("CAIRO TEST BANK"),
                "Cairo Test Bank should be available."
        );

        Assert.assertTrue(
                banks.contains("FAILBANK"),
                "FAILBANK should be available for negative testing."
        );


        List<String> currencies =
                beneficiariesPage.getCurrencyOptions();

        Assert.assertTrue(
                currencies.contains("EGP"),
                "EGP should be available."
        );

        Assert.assertTrue(
                currencies.contains("USD"),
                "USD should be available."
        );

        Assert.assertTrue(
                currencies.contains("EUR"),
                "EUR should be available."
        );

        Assert.assertTrue(
                currencies.contains("GBP"),
                "GBP should be available."
        );

        beneficiariesPage.closeModal();
    }


    @Test
    public void beneficiarySearchShouldAcceptInput() {

        String searchValue =
                "QA beneficiary";

        beneficiariesPage.enterSearch(
                searchValue
        );

        Assert.assertEquals(
                beneficiariesPage.getSearchValue(),
                searchValue,
                "Search field should contain the entered value."
        );
    }
}
