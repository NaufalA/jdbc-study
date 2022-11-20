package com.enigmacamp.service;

import com.enigmacamp.model.Transaction;
import com.enigmacamp.repository.TransactionRepository;
import com.enigmacamp.shared.interfaces.IService;

import java.util.List;

public class TransactionService implements IService<Transaction> {
    TransactionRepository repository;

    public TransactionService(TransactionRepository repository) {
        this.repository = repository;
    }

    @Override
    public Transaction create(Transaction newItem) throws Exception {
        return repository.insert(newItem);
    }

    @Override
    public List<Transaction> getAll() throws Exception {
        return repository.findAll();
    }

    @Override
    public Transaction getById(Integer id) throws Exception {
        return repository.findById(id);
    }

    @Override
    public Transaction update(Integer id, Transaction updatedItem) throws Exception {
        throw new Exception("Unimplemented");
    }

    @Override
    public Integer remove(Integer id) throws Exception {
        return repository.delete(id);
    }
}
