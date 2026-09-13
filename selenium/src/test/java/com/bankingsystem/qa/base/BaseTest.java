package com.bankingsystem.qa.base;

import org.openqa.selenium.WebDriver;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public abstract class BaseTest {

    @BeforeMethod
    public void setUp() {

        DriverFactory.initializeDriver();
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {

        DriverFactory.quitDriver();
    }

    protected WebDriver getDriver() {

        return DriverFactory.getDriver();
    }
}
