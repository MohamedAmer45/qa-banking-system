export type SeedUser = {
  email: string;
  password: string;
  role: string;
};

/** Identities created by `npm run db:seed` in the application repository. */
export const MFA_CODE = "123456";

export const users = {
  customer: { email: "customer@novabank.test", password: "Demo123!", role: "CUSTOMER" },
  receiver: { email: "receiver@novabank.test", password: "Demo123!", role: "CUSTOMER" },
  pendingKyc: { email: "pending@novabank.test", password: "Demo123!", role: "CUSTOMER" },
  admin: { email: "admin@novabank.test", password: "Admin123!", role: "ADMIN" },
  manager: { email: "manager@novabank.test", password: "Manager123!", role: "MANAGER" },
  support: { email: "support@novabank.test", password: "Support123!", role: "SUPPORT" }
} satisfies Record<string, SeedUser>;
