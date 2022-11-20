package com.enigmacamp.service;

import com.enigmacamp.model.Product;
import com.enigmacamp.repository.ProductRepository;
import com.enigmacamp.shared.interfaces.IService;

import java.util.List;

public class ProductService implements IService<Product> {

    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    @Override
    public Product create(Product newItem) throws Exception {
        return repository.insert(newItem);
    }

    @Override
    public List<Product> getAll() throws Exception {
        return repository.findAll();
    }

    @Override
    public Product getById(Integer id) throws Exception {
        return repository.findById(id);
    }

    @Override
    public Product update(Integer id, Product updatedItem) throws Exception {
        return repository.update(id, updatedItem);
    }

    @Override
    public Integer remove(Integer id) throws Exception {
        return repository.delete(id);
    }
}
