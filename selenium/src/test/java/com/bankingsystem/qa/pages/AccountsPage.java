package com.bankingsystem.qa.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class AccountsPage extends BasePage {

    public AccountsPage(WebDriver driver) {
        super(driver);
    }

    public AccountsPage open() {
        openView("accounts");
        return this;
    }

    public List<WebElement> cards() {
        return findAll("account-card");
    }

    public int count() {
        return cards().size();
    }

    /**
     * The integer minor-unit balance the ledger holds, read from the element's
     * data attribute rather than parsed out of a localised currency string.
     */
    public long balanceMinor(int index) {
        return Long.parseLong(
                findAll("account-balance").get(index).getAttribute("data-balance-minor")
        );
    }

    public boolean allCardsShowTypeAndStatus() {
        return cards().stream().allMatch(card -> {
            String text = card.getText();
            return (text.contains("CURRENT") || text.contains("SAVINGS"))
                    && (text.contains("ACTIVE") || text.contains("FROZEN")
                    || text.contains("DORMANT") || text.contains("CLOSED"));
        });
    }
}
