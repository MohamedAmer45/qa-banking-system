package com.bankingsystem.qa.bdd.hooks;

import com.bankingsystem.qa.bdd.context.TestContext;
import com.bankingsystem.qa.bdd.driver.DriverFactory;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;

public final class Hooks {

    private final TestContext context;

    public Hooks(TestContext context) {

        this.context = context;
    }

    @Before(order = 0)
    public void startBrowser() {

        DriverFactory.initializeDriver();
        context.setDriver(DriverFactory.getDriver());
    }

    @After(order = 100)
    public void stopBrowser(Scenario scenario) {

        try {
            attachScreenshotWhenFailed(scenario);
        } finally {
            DriverFactory.quitDriver();
            context.clear();
        }
    }

    private void attachScreenshotWhenFailed(Scenario scenario) {

        if (!scenario.isFailed() || !context.hasDriver()) {
            return;
        }

        try {
            WebDriver driver = context.getDriver();

            if (driver instanceof TakesScreenshot screenshotDriver) {
                byte[] screenshot =
                        screenshotDriver.getScreenshotAs(OutputType.BYTES);

                scenario.attach(
                        screenshot,
                        "image/png",
                        "Failure - " + scenario.getName()
                );
            }
        } catch (WebDriverException ignored) {
            // Preserve the original scenario failure.
        }
    }
}