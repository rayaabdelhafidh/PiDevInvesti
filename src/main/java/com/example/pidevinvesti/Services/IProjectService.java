package com.example.pidevinvesti.Services;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface IProjectService <T,ID>{
    T save(T entity);

    T add (T entity);
    public BigDecimal calculateTotalInvestment(int projectId) ;

    Optional<T> findById(ID id);

    T update (ID id, T entity);
    List<T> findAll();


    void deleteById(ID id);

    void delete(T entity);
}
