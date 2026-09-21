package com.bankingsystem.qa.tests;

import com.bankingsystem.qa.base.BaseTest;
import com.bankingsystem.qa.pages.LoginPage;
import com.bankingsystem.qa.utils.ConfigReader;

import org.testng.annotations.Test;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

/** Proves the driver, configuration and target environment are all usable. */
public class FrameworkSmokeTest extends BaseTest {

    @Test(description = "The driver starts and the configured environment responds")
    public void environmentIsReachable() {
        assertNotNull(getDriver(), "WebDriver should be initialised");

        LoginPage login = new LoginPage(getDriver()).open();

        assertTrue(login.isLoginFormDisplayed(),
                "the configured base URL should serve the sign-in screen: "
                        + ConfigReader.get("base.url"));
    }
}
