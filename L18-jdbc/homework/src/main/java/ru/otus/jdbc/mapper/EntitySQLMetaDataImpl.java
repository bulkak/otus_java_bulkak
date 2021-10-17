package ru.otus.jdbc.mapper;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class EntitySQLMetaDataImpl<T> implements EntitySQLMetaData<T> {
    private final EntityClassMetaData<T> entityMetadata;

    public EntitySQLMetaDataImpl(EntityClassMetaData<T> entityMetadata) {
        this.entityMetadata = entityMetadata;
    }

    @Override
    public String getSelectAllSql() {
        return "SELECT * FROM " + entityMetadata.getName();
    }

    @Override
    public String getSelectByIdSql() {
        return "SELECT * FROM " + entityMetadata.getName()
                + " WHERE " + entityMetadata.getIdField().getName() + " = ?";
    }

    @Override
    public String getInsertSql() {
        List<Field> fields = entityMetadata.getFieldsWithoutId();
        String[] values = new String[fields.size()];
        Arrays.fill(values, "?");
        return "insert into " + entityMetadata.getName()
                + "("+
                (getCommaSeparatedFields(fields))
                 +") values ("
                + String.join(",", values)
                + ")";
    }

    @Override
    public String getUpdateSql() {
        List<Field> fields = entityMetadata.getFieldsWithoutId();
        return "update " + entityMetadata.getName()
                + " SET "+
                (fields.stream()
                        .map(field -> field.getName() + " = ?")
                        .collect(Collectors.joining(",")))
                +" WHERE " + entityMetadata.getIdField() + " = ?";
    }


    private String getCommaSeparatedFields(List<Field> fields) {
        return fields.stream()
                .map(Field::getName)
                .collect(Collectors.joining(","));
    }


    @Override
    public List<Object> getUpdateParams(Object object) {
        var lst = new ArrayList<>();
        entityMetadata.getFieldsWithoutId().forEach(field -> {
            try {
                lst.add(entityMetadata.getGetterByField(field).invoke(object));
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        });
        try {
            lst.add(entityMetadata.getGetterByField(entityMetadata.getIdField()).invoke(object));
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        return lst;
    }

    @Override
    public List<Object> getInsertParams(Object object) {
        var lst = new ArrayList<>();
        entityMetadata.getFieldsWithoutId().forEach(field -> {
            try {
                lst.add(entityMetadata.getGetterByField(field).invoke(object));
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        });
        return lst;
    }

    @Override
    public T getInstanceFromResultSet(ResultSet rs, ResultSetMetaData md) {
        try {
            List<Object> argList = new ArrayList<>();
            for (int iterator = 0; iterator < md.getColumnCount(); iterator++) {
                String columnName = md.getColumnName(iterator + 1);
                Object columnValue = rs.getObject(iterator + 1);
                entityMetadata.getAllFields().forEach(field -> {
                    if (field.getName().equalsIgnoreCase(columnName)
                            && columnValue != null) {
                        try {
                            // допускаем что все поля - String кроме id чтобы не выдумывать отдельную разметку для полей и не тонуть в универсальности
                            if (entityMetadata.getIdField().getName().equalsIgnoreCase(columnName)) {
                                argList.add(rs.getLong(entityMetadata.getIdField().getName()));
                            } else {
                                argList.add(columnValue.toString());
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
            return entityMetadata.getConstructor().newInstance(argList.toArray(new Object[0]));
        } catch (InstantiationException | InvocationTargetException | IllegalAccessException | SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
