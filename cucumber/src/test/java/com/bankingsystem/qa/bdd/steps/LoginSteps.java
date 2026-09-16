package com.bankingsystem.qa.bdd.steps;

import com.bankingsystem.qa.bdd.context.TestContext;
import com.bankingsystem.qa.bdd.pages.LoginPage;

import io.cucumber.java.en.Then;

import org.testng.Assert;

public final class LoginSteps {

    private final TestContext context;

    public LoginSteps(TestContext context) {

        this.context = context;
    }

    @Then("the customer demo option should be available")
    public void customerDemoOptionShouldBeAvailable() {

        LoginPage loginPage = context.getLoginPage();

        Assert.assertTrue(
                loginPage.isCustomerOptionAvailable(),
                "Customer demo option should be visible."
        );
    }

    @Then("the admin demo option should be available")
    public void adminDemoOptionShouldBeAvailable() {

        LoginPage loginPage = context.getLoginPage();

        Assert.assertTrue(
                loginPage.isAdminOptionAvailable(),
                "Admin demo option should be visible."
        );
    }
}