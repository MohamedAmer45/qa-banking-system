package com.bankingsystem.qa.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class DashboardPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By heading =
            By.cssSelector("#dashboard h1");

    private final By loggedInUser =
            By.id("who");

    private final By totalBalance =
            By.id("total");

    private final By accountsNavigation =
            By.cssSelector("#nav button[data-s='accounts']");

    private final By transfersNavigation =
            By.cssSelector("#nav button[data-s='transfers']");

    private final By transactionsNavigation =
            By.cssSelector("#nav button[data-s='transactions']");

    private final By billsNavigation =
            By.cssSelector("#nav button[data-s='bills']");

    private final By cardsNavigation =
            By.cssSelector("#nav button[data-s='cards']");

    private final By loansNavigation =
            By.cssSelector("#nav button[data-s='loans']");

    private final By notificationsNavigation =
            By.cssSelector("#nav button[data-s='notifications']");

    private final By profileNavigation =
            By.cssSelector("#nav button[data-s='profile']");

    private final By logoutButton =
            By.id("logout");

    private final By adminNavigation =
            By.id("adminNav");

    public DashboardPage(WebDriver driver) {

        this.driver = driver;

        this.wait = new WebDriverWait(
                driver,
                Duration.ofSeconds(10)
        );
    }

    public boolean isLoaded() {

        try {

            String headingText =
                    wait.until(
                            ExpectedConditions.visibilityOfElementLocated(
                                    heading
                            )
                    ).getText();

            return "Dashboard".equals(headingText);

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

    public String getLoggedInUserText() {

        return wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        loggedInUser
                )
        ).getText();
    }

    public String getTotalBalance() {

        return wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        totalBalance
                )
        ).getText();
    }

    public boolean isAdminNavigationDisplayed() {

        try {

            return driver.findElement(adminNavigation)
                    .isDisplayed();

        } catch (Exception exception) {

            return false;
        }
    }

    private void openSection(
            By navigation,
            String sectionId
    ) {

        wait.until(
                ExpectedConditions.elementToBeClickable(
                        navigation
                )
        ).click();

        wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector(
                                "#" + sectionId + ".section.on"
                        )
                )
        );
    }


    // ========================================================
    // ACCOUNTS
    // ========================================================

    public AccountsPage openAccounts() {

        openSection(
                accountsNavigation,
                "accounts"
        );

        return new AccountsPage(driver);
    }


    // ========================================================
    // TRANSFERS
    // ========================================================

    public TransfersPage openTransfers() {

        openSection(
                transfersNavigation,
                "transfers"
        );

        return new TransfersPage(driver);
    }


    // ========================================================
    // TRANSACTIONS
    // ========================================================

    public TransactionsPage openTransactions() {

        openSection(
                transactionsNavigation,
                "transactions"
        );

        return new TransactionsPage(driver);
    }


    // ========================================================
    // BILLS
    // ========================================================

    public BillsPage openBills() {

        openSection(
                billsNavigation,
                "bills"
        );

        return new BillsPage(driver);
    }


    // ========================================================
    // CARDS
    // ========================================================

    public CardsPage openCards() {

        openSection(
                cardsNavigation,
                "cards"
        );

        return new CardsPage(driver);
    }


    // ========================================================
    // BENEFICIARIES
    //
    // The rebuilt application currently does not expose a
    // separate Beneficiaries section.
    //
    // This method is intentionally retained so the existing
    // Selenium framework continues to compile.
    // The beneficiary tests will be updated/restored later.
    // ========================================================

    public BeneficiariesPage openBeneficiaries() {

        return new BeneficiariesPage(driver);
    }


    // ========================================================
    // STATEMENTS
    //
    // The rebuilt application currently exposes Transactions
    // but not the previous dedicated Statements module.
    //
    // Keep this method for compatibility with existing tests.
    // ========================================================

    public StatementsPage openStatements() {

        return new StatementsPage(driver);
    }


    // ========================================================
    // CURRENT APPLICATION NAVIGATION
    // ========================================================

    public void openLoans() {

        openSection(
                loansNavigation,
                "loans"
        );
    }

    public void openNotifications() {

        openSection(
                notificationsNavigation,
                "notifications"
        );
    }

    public void openProfile() {

        openSection(
                profileNavigation,
                "profile"
        );
    }


    // ========================================================
    // LOGOUT
    // ========================================================

    public void logout() {

        wait.until(
                ExpectedConditions.elementToBeClickable(
                        logoutButton
                )
        ).click();

        wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.id("login")
                )
        );
    }
}