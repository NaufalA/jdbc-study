package com.enigmacamp.model;

import com.enigmacamp.shared.classes.BaseModel;

public class Product extends BaseModel {
    public String productName;
    public ProductPrice productPrice;

    public Product() {
    }

    public Product(String productName) {
        this.productName = productName;
    }

    public Product(Integer id, String productName) {
        super(id);
        this.productName = productName;
    }

    public Product(String productName, ProductPrice productPrice) {
        this.productName = productName;
        this.productPrice = productPrice;
    }

    @Override
    public String toString() {
        String output = productName;
        if (productPrice != null) {
            output += " | " + productPrice.price;
        }

        return output;
    }
}
