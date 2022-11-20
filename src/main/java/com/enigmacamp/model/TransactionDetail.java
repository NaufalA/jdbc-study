package com.enigmacamp.model;

import com.enigmacamp.shared.classes.BaseModel;

public class TransactionDetail extends BaseModel {
    public Float quantity;
    public Integer productPriceId;
    public ProductPrice productPrice;

    public TransactionDetail(Float quantity) {
        this.quantity = quantity;
    }

    public TransactionDetail(Float quantity, Integer productPriceId) {
        this.quantity = quantity;
        this.productPriceId = productPriceId;
    }

    public TransactionDetail(Float quantity, ProductPrice productPrice) {
        this.quantity = quantity;
        this.productPrice = productPrice;
    }

    public TransactionDetail(Integer id, Float quantity, ProductPrice productPrice) {
        super(id);
        this.quantity = quantity;
        this.productPrice = productPrice;
    }

    public TransactionDetail(Integer id, Float quantity, Integer productPriceId) {
        super(id);
        this.quantity = quantity;
        this.productPriceId = productPriceId;
    }
}
