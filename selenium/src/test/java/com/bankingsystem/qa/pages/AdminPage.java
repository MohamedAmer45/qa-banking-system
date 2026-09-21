package com.bankingsystem.qa.pages;

import org.openqa.selenium.WebDriver;

public class AdminPage extends BasePage {

    public AdminPage(WebDriver driver) {
        super(driver);
    }

    public AdminPage openDashboard() {
        openView("admin-dashboard");
        return this;
    }

    public AdminPage openCustomers() {
        openView("admin-customers");
        find("customer-table");
        return this;
    }

    public AdminPage openAudit() {
        openView("admin-audit");
        find("audit-table");
        return this;
    }

    public String customerTableText() {
        return find("customer-table").getText();
    }

    public String auditTableText() {
        return find("audit-table").getText();
    }

    /** Open user administration without expecting it to render data. */
    public AdminPage openUsersExpectingDenial() {
        openView("admin-users");
        return this;
    }

    public String viewText() {
        return find("view").getText();
    }
}
