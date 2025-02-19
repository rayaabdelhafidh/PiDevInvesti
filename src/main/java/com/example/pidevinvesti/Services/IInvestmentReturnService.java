package com.example.pidevinvesti.Services;

import java.util.List;
import java.util.Optional;

public interface IInvestmentReturnService <T,ID>{
    T save(T entity);

    T add (T entity);

    Optional<T> findById(ID id);

    T update (ID id, T entity);
    List<T> findAll();


    void deleteById(ID id);


    void delete(T entity);
}
