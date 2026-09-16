package com.bankingsystem.qa.bdd.steps;

import com.bankingsystem.qa.bdd.context.TestContext;
import com.bankingsystem.qa.bdd.pages.LoginPage;

import io.cucumber.java.en.Given;

import org.testng.Assert;

public final class NavigationSteps {

    private final TestContext context;

    public NavigationSteps(TestContext context) {

        this.context = context;
    }

    @Given("the NovaBank entry page is open")
    public void openNovaBankEntryPage() {

        LoginPage loginPage =
                new LoginPage(context.getDriver()).open();

        Assert.assertTrue(
                loginPage.isLoaded(),
                "NovaBank entry page should load."
        );

        context.setLoginPage(loginPage);
    }
}