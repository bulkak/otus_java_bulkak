package ru.otus.jdbc.mapper;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class EntitySQLMetaDataImpl<T> implements EntitySQLMetaData<T> {
    private final EntityClassMetaData<T> entityMetadata;
    private final String selectSQL;
    private final String insertSQL;
    private final String updateSQL;
    private final String selectByIdSQL;

    public EntitySQLMetaDataImpl(EntityClassMetaData<T> entityMetadata) {
        this.entityMetadata = entityMetadata;
        this.selectSQL = generateSelectAllSql();
        this.insertSQL = generateInsertSql();
        this.updateSQL  = generateUpdateSql();
        this.selectByIdSQL = generateSelectByIdSql();
    }

    @Override
    public String getSelectAllSql() {
        return selectSQL;
    }

    @Override
    public String getSelectByIdSql() {
        return selectByIdSQL;
    }

    @Override
    public String getInsertSql() {
        return insertSQL;
    }

    @Override
    public String getUpdateSql() {
        return updateSQL;
    }

    private String generateSelectAllSql() {
        return "SELECT * FROM " + entityMetadata.getName();
    }

    private String generateSelectByIdSql() {
        return "SELECT * FROM " + entityMetadata.getName()
                + " WHERE " + entityMetadata.getIdField().getName() + " = ?";
    }

    private String generateInsertSql() {
        List<Field> fields = entityMetadata.getFieldsWithoutId();
        String[] values = new String[fields.size()];
        Arrays.fill(values, "?");
        return "INSERT INTO " + entityMetadata.getName()
                + "("+
                (getCommaSeparatedFields(fields))
                 +") values ("
                + String.join(",", values)
                + ")";
    }

    private String generateUpdateSql() {
        List<Field> fields = entityMetadata.getFieldsWithoutId();
        return "UPDATE " + entityMetadata.getName()
                + " SET "+
                (fields.stream()
                        .map(field -> field.getName() + " = ?")
                        .collect(Collectors.joining(",")))
                +" WHERE " + entityMetadata.getIdField().getName() + " = ?";
    }

    private String getCommaSeparatedFields(List<Field> fields) {
        return fields.stream()
                .map(Field::getName)
                .collect(Collectors.joining(","));
    }

    public List<Object> getUpdateParams(Object object) {
        var lst = new ArrayList<>();
        entityMetadata.getFieldsWithoutId().forEach(field -> {
            try {
                lst.add(entityMetadata.getGetterByField(field).invoke(object));
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new DataTemplateJdbcException(e);
            }
        });
        try {
            lst.add(entityMetadata.getGetterByField(entityMetadata.getIdField()).invoke(object));
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new DataTemplateJdbcException(e);
        }

        return lst;
    }

    public List<Object> getInsertParams(Object object) {
        var lst = new ArrayList<>();
        entityMetadata.getFieldsWithoutId().forEach(field -> {
            try {
                lst.add(entityMetadata.getGetterByField(field).invoke(object));
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new DataTemplateJdbcException(e);
            }
        });
        return lst;
    }
}
