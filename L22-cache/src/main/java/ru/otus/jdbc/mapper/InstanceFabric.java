package ru.otus.jdbc.mapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

public interface InstanceFabric<T> {
    T getInstanceFromResultSet(ResultSet rs, ResultSetMetaData md);
}
