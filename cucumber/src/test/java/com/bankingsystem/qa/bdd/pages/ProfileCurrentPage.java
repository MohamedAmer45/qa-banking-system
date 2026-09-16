package com.bankingsystem.qa.bdd.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class ProfileCurrentPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By profileNavigation =
            By.cssSelector("#nav button[data-s='profile']");

    private final By section =
            By.cssSelector("#profile.section.on");

    private final By heading =
            By.cssSelector("#profile h1");

    private final By profileCard =
            By.id("profileCard");

    private final By customerName =
            By.cssSelector("#profileCard h2");

    private final By email =
            By.cssSelector("#profileCard p:not(.muted)");

    private final By role =
            By.cssSelector("#profileCard p.muted");

    public ProfileCurrentPage(WebDriver driver) {

        this.driver = driver;

        this.wait = new WebDriverWait(
                driver,
                Duration.ofSeconds(10)
        );
    }

    public boolean isLoaded() {

        try {

            wait.until(
                    ExpectedConditions.visibilityOfElementLocated(
                            section
                    )
            );

            wait.until(
                    ExpectedConditions.visibilityOfElementLocated(
                            profileCard
                    )
            );

            return "Profile".equals(
                    wait.until(
                            ExpectedConditions.visibilityOfElementLocated(
                                    heading
                            )
                    ).getText()
            );

        } catch (Exception exception) {

            return false;
        }
    }

    public String getPageTitleText() {

        return wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        heading
                )
        ).getText();
    }

    public String getCustomerName() {

        return wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        customerName
                )
        ).getText();
    }

    public String getEmail() {

        return wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        email
                )
        ).getText();
    }

    public String getRole() {

        return wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        role
                )
        ).getText();
    }

    public void open() {

        wait.until(
                ExpectedConditions.elementToBeClickable(
                        profileNavigation
                )
        ).click();

        wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        section
                )
        );
    }
}