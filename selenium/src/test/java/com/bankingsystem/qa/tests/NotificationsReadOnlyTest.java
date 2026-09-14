package com.bankingsystem.qa.tests;

import com.bankingsystem.qa.base.CustomerTestBase;
import com.bankingsystem.qa.pages.NotificationsCurrentPage;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

public class NotificationsReadOnlyTest extends CustomerTestBase {

    private NotificationsCurrentPage openNotificationsPage() {

        dashboardPage.openNotifications();

        NotificationsCurrentPage notificationsPage =
                new NotificationsCurrentPage(
                        getDriver()
                );

        Assert.assertTrue(
                notificationsPage.isLoaded(),
                "Notifications page should load successfully."
        );

        return notificationsPage;
    }

    @Test(
            groups = {"smoke", "notifications"},
            description = "Notifications page loads successfully"
    )
    public void notificationsPageShouldLoadSuccessfully() {

        NotificationsCurrentPage notificationsPage =
                openNotificationsPage();

        Assert.assertEquals(
                notificationsPage.getPageTitleText(),
                "Notifications",
                "Notifications heading should be displayed."
        );
    }

    @Test(
            groups = {"smoke", "notifications"},
            description = "Customer notifications are displayed"
    )
    public void notificationsShouldBeDisplayed() {

        NotificationsCurrentPage notificationsPage =
                openNotificationsPage();

        Assert.assertTrue(
                notificationsPage.getNotificationCount() > 0,
                "At least one notification should be displayed."
        );
    }

    @Test(
            groups = {"regression", "notifications"},
            description = "Every notification contains content"
    )
    public void everyNotificationShouldContainContent() {

        NotificationsCurrentPage notificationsPage =
                openNotificationsPage();

        Assert.assertTrue(
                notificationsPage.allNotificationsHaveContent(),
                "Every notification should contain readable content."
        );
    }

    @Test(
            groups = {"regression", "notifications"},
            description = "Monthly statement notification is available"
    )
    public void monthlyStatementNotificationShouldBeDisplayed() {

        NotificationsCurrentPage notificationsPage =
                openNotificationsPage();

        Assert.assertTrue(
                notificationsPage.containsText(
                        "statement"
                ),
                "A statement notification should be displayed."
        );
    }

    @Test(
            groups = {"regression", "notifications"},
            description = "Card activity notification is available"
    )
    public void cardActivityNotificationShouldBeDisplayed() {

        NotificationsCurrentPage notificationsPage =
                openNotificationsPage();

        Assert.assertTrue(
                notificationsPage.containsText(
                        "card"
                ),
                "A card activity notification should be displayed."
        );
    }

    @Test(
            groups = {"regression", "notifications"},
            description = "Notification list remains internally consistent"
    )
    public void notificationListShouldRemainConsistent() {

        NotificationsCurrentPage notificationsPage =
                openNotificationsPage();

        List<String> notifications =
                notificationsPage.getNotifications();

        Assert.assertEquals(
                notifications.size(),
                notificationsPage.getNotificationCount(),
                "Notification count should match displayed notification records."
        );
    }
}