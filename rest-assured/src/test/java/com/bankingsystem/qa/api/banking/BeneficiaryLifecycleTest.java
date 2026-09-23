package com.bankingsystem.qa.api.banking;

import com.bankingsystem.qa.api.support.ApiTest;
import com.bankingsystem.qa.api.support.Users;

import io.qameta.allure.Description;
import io.qameta.allure.Story;

import org.testng.annotations.Test;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * BEN-004, BEN-008 — the beneficiary delete contract.
 *
 * Regression cover for BUG-BEN-001. Deleting a beneficiary retires the row
 * rather than removing it, because historical transfers reference it. The
 * defect was that the read endpoint returned retired rows alongside active
 * ones, so a client listing "my beneficiaries" was handed records it could
 * not pay.
 *
 * Every test here creates the beneficiary it works on. Sharing a fixture
 * across a delete test is how one failure becomes five.
 */
@Story("Beneficiary lifecycle")
public class BeneficiaryLifecycleTest extends ApiTest {

    private int createBeneficiary(String name) {
        return as(Users.CUSTOMER)
                .body("""
                        {"name":"%s","bankName":"OTHER BANK",
                         "accountIdentifier":"EG%s","currency":"EGP"}"""
                        .formatted(name, System.nanoTime() % 1_000_000_000L))
                .post("/api/beneficiaries")
                .then()
                .statusCode(201)
                .extract()
                .path("id");
    }

    private void delete(int id) {
        as(Users.CUSTOMER)
                .delete("/api/beneficiaries/" + id)
                .then()
                .statusCode(200);
    }

    @Test(description = "A deleted beneficiary disappears from the list")
    @Description("BUG-BEN-001: the list returned soft-deleted rows")
    public void deletedBeneficiaryIsNotListed() {
        int id = createBeneficiary(uniqueKey("Regression"));

        as(Users.CUSTOMER)
                .get("/api/beneficiaries")
                .then()
                .statusCode(200)
                .body("id", hasItem(id));

        delete(id);

        as(Users.CUSTOMER)
                .get("/api/beneficiaries")
                .then()
                .statusCode(200)
                .body("id", not(hasItem(id)));
    }

    @Test(description = "The list never contains a DELETED status")
    @Description("The property behind BUG-BEN-001, independent of any one record")
    public void listContainsNoDeletedRecords() {
        int id = createBeneficiary(uniqueKey("StatusProbe"));
        delete(id);

        as(Users.CUSTOMER)
                .get("/api/beneficiaries")
                .then()
                .statusCode(200)
                .body("findAll { it.status == 'DELETED' }", empty())
                .body("status", everyItem(not(equalTo("DELETED"))));
    }

    @Test(description = "Retired records remain available behind an explicit flag")
    public void includeDeletedReturnsTheRetiredRecord() {
        int id = createBeneficiary(uniqueKey("Retained"));
        delete(id);

        /*
         * The row must still exist. Removing it would break the transfers that
         * reference it, which is why the delete is soft in the first place.
         */
        String status = as(Users.CUSTOMER)
                .queryParam("includeDeleted", "true")
                .get("/api/beneficiaries")
                .then()
                .statusCode(200)
                .body("id", hasItem(id))
                .extract()
                .path("find { it.id == %d }.status".formatted(id));

        assertEquals(status, "DELETED", "the retired record should keep its status");
    }

    @Test(description = "The default list is the included list minus the retired records")
    public void defaultListIsTheActiveSubset() {
        int id = createBeneficiary(uniqueKey("Subset"));
        delete(id);

        int defaultCount = as(Users.CUSTOMER)
                .get("/api/beneficiaries")
                .then()
                .statusCode(200)
                .extract()
                .path("size()");

        int allCount = as(Users.CUSTOMER)
                .queryParam("includeDeleted", "true")
                .get("/api/beneficiaries")
                .then()
                .statusCode(200)
                .extract()
                .path("size()");

        assertTrue(allCount > defaultCount,
                "including retired records should return more, not the same: "
                        + allCount + " vs " + defaultCount);
    }

    @Test(description = "Deleting twice is idempotent and does not resurrect the record")
    public void secondDeleteIsIdempotent() {
        int id = createBeneficiary(uniqueKey("DoubleDelete"));
        delete(id);

        /*
         * Verified behaviour, not an assumption: the delete handler looks the
         * record up without a status filter, so a repeat returns 200 rather
         * than 404. That is defensible for DELETE, which is meant to be
         * idempotent, and the record must stay retired either way -- which is
         * the part worth asserting.
         */
        as(Users.CUSTOMER)
                .delete("/api/beneficiaries/" + id)
                .then()
                .statusCode(200);

        as(Users.CUSTOMER)
                .get("/api/beneficiaries")
                .then()
                .statusCode(200)
                .body("id", not(hasItem(id)));

        String status = as(Users.CUSTOMER)
                .queryParam("includeDeleted", "true")
                .get("/api/beneficiaries")
                .then()
                .statusCode(200)
                .extract()
                .path("find { it.id == %d }.status".formatted(id));

        assertEquals(status, "DELETED", "a repeated delete must not change the status");
    }

    @Test(description = "A deleted beneficiary cannot be paid")
    @Description("The boundary the list filter sits in front of")
    public void deletedBeneficiaryCannotReceiveATransfer() {
        int id = createBeneficiary(uniqueKey("Unpayable"));
        delete(id);

        /*
         * Hiding the record from the list is a convenience. This is the rule
         * that actually protects the ledger, and it held even while
         * BUG-BEN-001 was open.
         */
        as(Users.CUSTOMER)
                .body("""
                        {"fromAccountId":%d,"beneficiaryId":%d,"amount":10,
                         "memo":"should be refused"}"""
                        .formatted(firstActiveAccountId(Users.CUSTOMER), id))
                .header("Idempotency-Key", uniqueKey("ben-deleted"))
                .post("/api/transfers")
                .then()
                .statusCode(409);
    }
}
