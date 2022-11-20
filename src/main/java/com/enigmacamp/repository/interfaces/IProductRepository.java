package com.enigmacamp.repository.interfaces;

import com.enigmacamp.shared.interfaces.IRepository;

import java.sql.SQLException;

public interface IProductRepository<T> extends IRepository<T> {
    T update(Integer id, T updatedItem) throws SQLException;
}
