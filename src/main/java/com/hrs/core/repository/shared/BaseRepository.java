package com.hrs.core.repository.shared;

import java.io.Serializable;
import java.util.List;

public interface BaseRepository<T, ID extends Serializable> {
    T findById(Long id);

    void persist(T t);

    void persistAndFlush(T t);

    List<T> findAll();

    T getSingleResult(String param, String value);

    void softDelete(Long id);
    void hardDelete(Long id);

}
