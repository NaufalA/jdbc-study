package com.enigmacamp.shared.interfaces;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface IRepository<T> {
    T insert(T newItem) throws SQLException;

    List<T> findAll() throws SQLException;

    T findById(Integer id) throws SQLException;

    T update(Integer id, T updatedItem) throws SQLException;

    Integer delete(Integer id) throws SQLException;

    T parse(ResultSet rs) throws SQLException;
}
