package com.bankingsystem.qa.base;

import org.openqa.selenium.WebDriver;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public abstract class BaseTest {

    @BeforeMethod(alwaysRun = true)
    public final void setUp() {

        DriverFactory.initializeDriver();

        afterDriverSetup();
    }


    /**
     * Hook for subclasses that need additional setup
     * after WebDriver has been initialized.
     *
     * Base tests do nothing by default.
     */
    protected void afterDriverSetup() {
        // No-op by default.
    }


    @AfterMethod(alwaysRun = true)
    public final void tearDown() {

        DriverFactory.quitDriver();
    }


    protected WebDriver getDriver() {

        return DriverFactory.getDriver();
    }
}
