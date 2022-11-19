package com.enigmacamp.model;

import com.enigmacamp.shared.classes.BaseModel;

public class ProductPrice extends BaseModel {
    public Float price;

    public ProductPrice(Float price) {
        this.price = price;
    }

    public ProductPrice(Integer id, Float price) {
        super(id);
        this.price = price;
    }
}
