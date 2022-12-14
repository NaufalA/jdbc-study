package com.enigmacamp.model;

import com.enigmacamp.shared.classes.BaseModel;

public class ProductPrice extends BaseModel {
    public Float price;

    public Product product;

    public ProductPrice(Float price) {
        this.price = price;
    }

    public ProductPrice(Integer id, Float price) {
        super(id);
        this.price = price;
    }

    public ProductPrice(Float price, Product product) {
        this.price = price;
        this.product = product;
    }
}
