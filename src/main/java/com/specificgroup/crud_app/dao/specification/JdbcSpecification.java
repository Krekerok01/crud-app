package com.specificgroup.crud_app.dao.specification;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;


public interface JdbcSpecification<T> {

    List<T> searchFilter(Connection connection, String sql) throws SQLException;
}
