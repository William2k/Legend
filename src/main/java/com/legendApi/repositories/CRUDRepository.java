package com.legendApi.repositories;

import java.util.List;

public interface CRUDRepository<T> {
    List<T> getAll();
    T getById(long id);
    long add(T entity);
    void update(T entity);
    void delete(long id);
}
