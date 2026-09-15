export interface UserCredentials {
  username: string;
  password: string;
}

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
