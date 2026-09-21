import { expect, Locator, Page } from "@playwright/test";
import { BasePage } from "./BasePage";
import { credentials, SeedUser } from "../test-data/credentials";

/**
 * Sign-in is a two-step handshake: credentials return an MFA challenge, and
 * the challenge is exchanged for a session. Both steps are always required —
 * every seeded user has MFA enabled.
 */
export class AuthPage extends BasePage {
  readonly loginForm: Locator;
  readonly emailInput: Locator;
  readonly passwordInput: Locator;
  readonly loginSubmit: Locator;

  readonly mfaForm: Locator;
  readonly mfaCodeInput: Locator;
  readonly mfaSubmit: Locator;
  readonly mfaCancel: Locator;

  readonly registerTab: Locator;
  readonly loginTab: Locator;
  readonly forgotPasswordLink: Locator;

  readonly userChip: Locator;
  readonly userRole: Locator;
  readonly logoutButton: Locator;

  constructor(page: Page) {
    super(page);

    this.loginForm = this.testId("login-form");
    this.emailInput = this.testId("login-email");
    this.passwordInput = this.testId("login-password");
    this.loginSubmit = this.testId("login-submit");

    this.mfaForm = this.testId("mfa-form");
    this.mfaCodeInput = this.testId("mfa-code");
    this.mfaSubmit = this.testId("mfa-submit");
    this.mfaCancel = this.testId("mfa-cancel");

    this.loginTab = this.testId("tab-login");
    this.registerTab = this.testId("tab-register");
    this.forgotPasswordLink = this.testId("forgot-password");

    this.userChip = this.testId("user-chip");
    this.userRole = this.testId("user-role");
    this.logoutButton = this.testId("logout");
  }

  async open(): Promise<void> {
    await this.navigate("/");
    await expect(this.loginForm).toBeVisible();
  }

  async expectLoginScreen(): Promise<void> {
    await expect(this.loginForm).toBeVisible();
    await expect(this.emailInput).toBeVisible();
    await expect(this.passwordInput).toBeVisible();
    await expect(this.loginSubmit).toBeEnabled();
  }

  /** Submit credentials only. Leaves the session on the MFA challenge. */
  async submitCredentials(email: string, password: string): Promise<void> {
    await this.emailInput.fill(email);
    await this.passwordInput.fill(password);

    const response = this.page.waitForResponse(
      r => r.url().includes("/api/auth/login") && r.request().method() === "POST"
    );

    await this.loginSubmit.click();
    await response;
  }

  async expectMfaChallenge(): Promise<void> {
    await expect(this.mfaForm).toBeVisible();
    await expect(this.mfaCodeInput).toBeVisible();
  }

  async submitMfa(code: string): Promise<void> {
    await this.mfaCodeInput.fill(code);

    const response = this.page.waitForResponse(
      r => r.url().includes("/api/auth/mfa") && r.request().method() === "POST"
    );

    await this.mfaSubmit.click();
    await response;
  }

  /** Full handshake through to an authenticated shell. */
  async loginAs(user: SeedUser): Promise<void> {
    await this.submitCredentials(user.email, user.password);
    await this.expectMfaChallenge();
    await this.submitMfa(credentials.mfaCode);

    await expect(this.userChip).toBeVisible();
    await expect(this.userRole).toHaveText(user.role);

    // The landing view renders after sign-in resolves; wait for it to settle
    // so a following navigation is not overwritten by the initial render.
    await this.waitForViewReady();
  }

  async loginAsCustomer(): Promise<void> {
    await this.loginAs(credentials.customer);
  }

  async loginAsAdmin(): Promise<void> {
    await this.loginAs(credentials.admin);
  }

  async expectLoginRejected(message: string | RegExp): Promise<void> {
    await this.expectToast(message);
    await expect(this.loginForm).toBeVisible();
    await expect(this.userChip).toBeHidden();
  }

  async logout(): Promise<void> {
    await this.logoutButton.click();
    await expect(this.loginForm).toBeVisible();
    await expect(this.userChip).toBeHidden();
  }

  /** The session token the application persists for reload survival. */
  async storedToken(): Promise<string | null> {
    return this.page.evaluate(() => localStorage.getItem("novabank_token"));
  }

  async expectAuthenticatedAfterReload(role: string): Promise<void> {
    await this.reload();
    await expect(this.userRole).toHaveText(role);
    await expect(this.loginForm).toBeHidden();
  }
}
