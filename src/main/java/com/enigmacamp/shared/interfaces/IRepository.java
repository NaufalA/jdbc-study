package com.enigmacamp.shared.interfaces;

import java.util.List;

public interface IRepository<T> {
    T insert(T newItem);

    List<T> findAll();

    T findById(Integer id);

    T update(Integer id, T updatedItem);

    Integer delete(Integer id);
}
