package com.accountmanagement.service;

import com.accountmanagement.exception.DataNotFoundException;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort.Direction;

import java.util.List;

public interface AbstractService<T, ID> {
    T findById(ID id) throws DataNotFoundException;
    boolean exists(Example<T> example);
    List<T> findAll(Example<T> example, String orderBy, Direction direction);
    Page<T> findAll(int page, int size, Example<T> example, String orderBy, Direction direction);
    T save(T entity);
    void delete(ID id);
}

