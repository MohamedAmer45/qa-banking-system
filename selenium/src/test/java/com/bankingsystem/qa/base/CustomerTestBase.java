package com.bankingsystem.qa.base;

import com.bankingsystem.qa.pages.LoginPage;

/**
 * Base for tests that need an authenticated customer. Signs in once per test
 * method, after the driver is ready.
 */
public abstract class CustomerTestBase extends BaseTest {

    protected LoginPage loginPage;

    @Override
    protected void afterDriverSetup() {
        loginPage = new LoginPage(getDriver()).open();
        loginPage.loginAsCustomer();
    }
}
