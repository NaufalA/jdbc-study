package com.enigmacamp.shared.interfaces;

import java.util.List;

public interface IService<T> {
    T create(T newItem) throws Exception;

    List<T> getAll() throws Exception;

    T getById(Integer id) throws Exception;

    T update(Integer id, T updatedItem) throws Exception;

    Integer remove(Integer id) throws Exception;
}
