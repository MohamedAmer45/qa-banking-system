export type DemoRole =
  | "customer"
  | "admin";

export interface TransferData {
  recipient: string;
  amount: number;
  description?: string;
}

export interface BeneficiaryData {
  name: string;
  accountNumber: string;
  bank?: string;
}

export interface BillPaymentData {
  biller: string;
  referenceNumber: string;
  amount: number;
}
