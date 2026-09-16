package com.bankingsystem.qa.bdd.steps;

import com.bankingsystem.qa.bdd.context.TestContext;
import com.bankingsystem.qa.bdd.pages.NotificationsCurrentPage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import org.testng.Assert;

import java.util.List;

public class NotificationSteps {

    private final TestContext testContext;
    private NotificationsCurrentPage notificationsPage;

    public NotificationSteps(TestContext testContext) {

        this.testContext = testContext;
    }

    private NotificationsCurrentPage requireNotificationsPage() {

        Assert.assertNotNull(
                notificationsPage,
                "The Notifications page must be opened before using it."
        );

        return notificationsPage;
    }

    @Given("the customer opens the Notifications page")
    public void theCustomerOpensTheNotificationsPage() {

        notificationsPage =
                new NotificationsCurrentPage(
                        testContext.getDriver()
                );

        notificationsPage.open();

        Assert.assertTrue(
                notificationsPage.isLoaded(),
                "Notifications page should load successfully."
        );
    }

    @Then("the Notifications page should be displayed")
    public void theNotificationsPageShouldBeDisplayed() {

        NotificationsCurrentPage page =
                requireNotificationsPage();

        Assert.assertTrue(
                page.isLoaded(),
                "Notifications page should be visible."
        );

        Assert.assertEquals(
                page.getPageTitleText(),
                "Notifications",
                "Notifications heading should be displayed."
        );
    }

    @Then("at least one customer notification should be displayed")
    public void atLeastOneCustomerNotificationShouldBeDisplayed() {

        Assert.assertTrue(
                requireNotificationsPage()
                        .getNotificationCount() > 0,
                "At least one notification should be displayed."
        );
    }

    @Then("every displayed notification should contain readable content")
    public void everyDisplayedNotificationShouldContainReadableContent() {

        Assert.assertTrue(
                requireNotificationsPage()
                        .allNotificationsHaveContent(),
                "Every notification should contain readable content."
        );
    }

    @Then("the displayed notification count should match the notification list")
    public void theDisplayedNotificationCountShouldMatchTheNotificationList() {

        NotificationsCurrentPage page =
                requireNotificationsPage();

        List<String> notifications =
                page.getNotifications();

        Assert.assertEquals(
                notifications.size(),
                page.getNotificationCount(),
                "Notification count should match displayed notification records."
        );
    }

    @Then("a notification containing {string} should be displayed")
    public void aNotificationContainingShouldBeDisplayed(
            String expectedText
    ) {

        Assert.assertTrue(
                requireNotificationsPage()
                        .containsText(expectedText),
                "A notification containing '" +
                        expectedText +
                        "' should be displayed."
        );
    }
}
