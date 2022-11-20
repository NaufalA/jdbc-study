package com.enigmacamp.model;

import com.enigmacamp.shared.classes.BaseModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Transaction extends BaseModel {
    public Date transaction_date;
    public List<TransactionDetail> transactionDetails;

    public Transaction() {
        this.transactionDetails = new ArrayList<>();
    }

    public Transaction(Date transaction_date) {
        this.transaction_date = transaction_date;
        this.transactionDetails = new ArrayList<>();
    }

    public Transaction(Date transaction_date, List<TransactionDetail> transactionDetails) {
        this.transaction_date = transaction_date;
        this.transactionDetails = transactionDetails;
    }

    public Transaction(Integer id, Date transaction_date) {
        super(id);
        this.transaction_date = transaction_date;
        this.transactionDetails = new ArrayList<>();
    }

    @Override
    public String toString() {
        return " | " + transaction_date;
    }
}
