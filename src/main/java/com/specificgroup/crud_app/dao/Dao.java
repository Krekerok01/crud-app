package com.specificgroup.crud_app.dao;


import com.specificgroup.crud_app.dto.CreateRequest;
import com.specificgroup.crud_app.dto.UpdateRequest;
import com.specificgroup.crud_app.dao.specification.JdbcSpecification;

import java.util.List;

public interface Dao<T> {
    Long create(CreateRequest request);
    List<T> getBySpecification(JdbcSpecification<T> jdbcSpecification);
    List<T> get();
    Long update(UpdateRequest request, Long contactDetailsId);
    boolean deleteByContactDetailId(Long contactDetailsId);

    Long getContactDetailsIdByMainEntityId(Long id);
}