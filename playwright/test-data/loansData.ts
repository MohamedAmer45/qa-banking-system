export const loansData = {

  currentLoan: {

    type:
      "Personal Loan",

    balance:
      "$6,450.00",

    apr:
      "7.9%",

    nextPayment:
      "$320.00",

    dueDate:
      "2026-10-05"

  },


  limits: {

    minimum:
      1000,

    maximum:
      50000,

    belowMinimum:
      999,

    aboveMaximum:
      50001,

    zero:
      0,

    negative:
      -1000

  },


  terms: {

    labels: [
      "12 months",
      "24 months",
      "36 months"
    ],

    values: [
      "12",
      "24",
      "36"
    ]

  }

};
