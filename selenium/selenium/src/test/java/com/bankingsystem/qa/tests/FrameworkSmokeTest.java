package com.bankingsystem.qa.tests;

import org.testng.Assert;
import org.testng.annotations.Test;

public class FrameworkSmokeTest {

    @Test
    public void testFrameworkIsWorking() {

        System.out.println("Banking System Selenium Framework is working.");

        Assert.assertTrue(true);
    }
}
