package com.specificgroup.crud_app.dao;


import com.specificgroup.crud_app.dto.CreateRequest;
import com.specificgroup.crud_app.util.JdbcSpecification;

import java.util.List;

public interface Dao<T> {
    Long create(CreateRequest request);
    List<T> get(JdbcSpecification<T> jdbcSpecification);
    Long update();
    boolean delete(long id);
}