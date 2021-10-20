package ru.otus.jdbc.mapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public interface InstanceFabric<T> {
    T getInstanceFromResultSet(ResultSet rs, ResultSetMetaData md);
}
