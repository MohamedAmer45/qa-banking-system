package com.bankingsystem.qa.tests;

import com.bankingsystem.qa.base.CustomerTestBase;
import com.bankingsystem.qa.pages.AccountsPage;
import com.bankingsystem.qa.pages.TransfersPage;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * Money-movement coverage.
 *
 * Assertions are expressed as deltas against a balance read at the start of
 * each test, never as absolute totals: the suite shares a database with the
 * other frameworks, and only a freshly seeded environment would make fixed
 * expected values stable.
 */
public class TransferTest extends CustomerTestBase {

    @Test(description = "The transfer form offers the customer's active accounts")
    public void formOffersSourceAccounts() {
        TransfersPage transfers = new TransfersPage(getDriver()).open().openForm();

        assertTrue(transfers.sourceAccountCount() >= 2,
                "expected at least two selectable source accounts");
    }

    @Test(description = "A valid transfer debits the source account")
    public void validTransferDebitsSource() {
        AccountsPage accounts = new AccountsPage(getDriver()).open();
        long before = accounts.balanceMinor(0);

        new TransfersPage(getDriver())
                .open()
                .openForm()
                .selectSource(0)
                .selectBeneficiary(0)
                .enterAmount("100")
                .submit();

        accounts.open();
        long after = accounts.balanceMinor(0);

        // 100.00 major units is 10000 minor. An external beneficiary also
        // attracts a fee, so the debit is at least the transfer amount.
        assertTrue(before - after >= 10_000L,
                "expected a debit of at least 10000 minor units, saw " + (before - after));
    }

    @Test(description = "A transfer beyond the available balance is rejected and moves nothing")
    public void insufficientFundsMovesNothing() {
        AccountsPage accounts = new AccountsPage(getDriver()).open();
        long before = accounts.balanceMinor(0);

        TransfersPage transfers = new TransfersPage(getDriver())
                .open()
                .openForm()
                .selectSource(0)
                .selectBeneficiary(0)
                .enterAmount("99999999");

        transfers.submit();

        assertTrue(transfers.toastText().toLowerCase().contains("reject"),
                "expected a rejection toast, saw: " + transfers.toastText());

        accounts.open();
        assertEquals(accounts.balanceMinor(0), before,
                "a rejected transfer must not move money");
    }

    @Test(description = "Zero is rejected by input validation before submission")
    public void zeroAmountIsInvalid() {
        TransfersPage transfers = new TransfersPage(getDriver()).open().openForm();
        assertFalse(transfers.isAmountValid("0"));
    }

    @Test(description = "A negative amount is rejected by input validation")
    public void negativeAmountIsInvalid() {
        TransfersPage transfers = new TransfersPage(getDriver()).open().openForm();
        assertFalse(transfers.isAmountValid("-1"));
    }

    @Test(description = "The minimum permitted amount is accepted by input validation")
    public void minimumAmountIsValid() {
        TransfersPage transfers = new TransfersPage(getDriver()).open().openForm();
        assertTrue(transfers.isAmountValid("0.01"));
    }
}
