package com.bankingsystem.qa.api.support;

/**
 * The identities created by {@code npm run db:seed} in the application
 * repository. Every one has MFA enabled with the same code.
 */
public final class Users {

    public static final String MFA_CODE = "123456";

    public record User(String email, String password, String role) {
    }

    public static final User CUSTOMER =
            new User("customer@novabank.test", "Demo123!", "CUSTOMER");

    public static final User RECEIVER =
            new User("receiver@novabank.test", "Demo123!", "CUSTOMER");

    public static final User PENDING_KYC =
            new User("pending@novabank.test", "Demo123!", "CUSTOMER");

    public static final User ADMIN =
            new User("admin@novabank.test", "Admin123!", "ADMIN");

    public static final User MANAGER =
            new User("manager@novabank.test", "Manager123!", "MANAGER");

    public static final User SUPPORT =
            new User("support@novabank.test", "Support123!", "SUPPORT");

    public static final User AUDITOR =
            new User("auditor@novabank.test", "Auditor123!", "AUDITOR");

    public static final User EMPLOYEE =
            new User("employee@novabank.test", "Employee123!", "EMPLOYEE");

    private Users() {
    }
}
