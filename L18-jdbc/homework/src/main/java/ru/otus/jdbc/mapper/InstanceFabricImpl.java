package ru.otus.jdbc.mapper;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InstanceFabricImpl<T> implements InstanceFabric<T> {
    private final EntityClassMetaData<T> entityMetadata;

    public InstanceFabricImpl(EntityClassMetaData<T> entityMetadata) {
        this.entityMetadata = entityMetadata;
    }

    @Override
    public T getInstanceFromResultSet(ResultSet rs, ResultSetMetaData md) {
        try {
            List<Object> argList = new ArrayList<>();
            for (int iterator = 1; iterator <= md.getColumnCount(); iterator++) {
                String columnName = md.getColumnName(iterator);
                Object columnValue = rs.getObject(iterator);
                for (Field field : entityMetadata.getAllFields()) {
                    if (field.getName().equalsIgnoreCase(columnName) && columnValue != null) {
                        argList.add(columnValue);
                    }
                }
            }
            return entityMetadata.getConstructor().newInstance(argList.toArray(new Object[0]));
        } catch (InstantiationException | InvocationTargetException | IllegalAccessException | SQLException e) {
            throw new DataTemplateJdbcException(e);
        }
    }
}
