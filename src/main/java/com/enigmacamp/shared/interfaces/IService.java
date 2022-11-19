package com.enigmacamp.shared.interfaces;

import java.util.List;

public interface IService<T> {
    T create(T newItem);

    List<T> getAll();

    T getById(Integer id);

    T update(Integer id, T updatedItem);

    Integer remove(Integer id);
}
