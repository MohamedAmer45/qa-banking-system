package com.bankingsystem.qa.tests;

import com.bankingsystem.qa.base.BaseTest;

import org.testng.Assert;
import org.testng.annotations.Test;

public class DriverSetupTest extends BaseTest {

    @Test
    public void shouldStartBrowserSuccessfully() {

        Assert.assertNotNull(
                getDriver(),
                "WebDriver should be initialized."
        );

        System.out.println(
                "Browser started successfully."
        );
    }
}
