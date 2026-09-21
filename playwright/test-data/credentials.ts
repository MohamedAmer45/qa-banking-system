export type SeedUser = {
  email: string;
  password: string;
  role: string;
  firstName: string;
};

/**
 * The seeded identities created by `npm run db:seed` in the application
 * repository. Every one of them has MFA enabled with the same code.
 */
export const credentials = {
  mfaCode: "123456",

  customer: {
    email: "customer@novabank.test",
    password: "Demo123!",
    role: "CUSTOMER",
    firstName: "Mohamed"
  } as SeedUser,

  receiver: {
    email: "receiver@novabank.test",
    password: "Demo123!",
    role: "CUSTOMER",
    firstName: "Nadia"
  } as SeedUser,

  pendingKyc: {
    email: "pending@novabank.test",
    password: "Demo123!",
    role: "CUSTOMER",
    firstName: "Youssef"
  } as SeedUser,

  admin: {
    email: "admin@novabank.test",
    password: "Admin123!",
    role: "ADMIN",
    firstName: "Nora"
  } as SeedUser,

  manager: {
    email: "manager@novabank.test",
    password: "Manager123!",
    role: "MANAGER",
    firstName: "Karim"
  } as SeedUser,

  support: {
    email: "support@novabank.test",
    password: "Support123!",
    role: "SUPPORT",
    firstName: "Maya"
  } as SeedUser,

  auditor: {
    email: "auditor@novabank.test",
    password: "Auditor123!",
    role: "AUDITOR",
    firstName: "Omar"
  } as SeedUser,

  employee: {
    email: "employee@novabank.test",
    password: "Employee123!",
    role: "EMPLOYEE",
    firstName: "Salma"
  } as SeedUser,

  invalid: {
    email: "customer@novabank.test",
    password: "WrongPassword123!",
    role: "CUSTOMER",
    firstName: "-"
  } as SeedUser
};
