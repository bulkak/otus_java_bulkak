package ru.otus.jdbc.mapper;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class EntitySQLMetaDataImpl<T> implements EntitySQLMetaData<T> {
    private final EntityClassMetaData<T> entityMetadata;
    private final String selectSQL;
    private final String insertSQL;
    private final String updateSQL;
    private final String selectByIdSQL;
    private final List<Method> insertParamsGetters;
    private final List<Method> updateParamsGetters;

    public EntitySQLMetaDataImpl(EntityClassMetaData<T> entityMetadata) {
        this.entityMetadata = entityMetadata;
        this.selectSQL = generateSelectAllSql();
        this.insertSQL = generateInsertSql();
        this.updateSQL  = generateUpdateSql();
        this.selectByIdSQL = generateSelectByIdSql();
        this.insertParamsGetters = prepareInsertGetters();
        this.updateParamsGetters = prepareUpdateGetters();
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

    private List<Method> prepareUpdateGetters() {
        var lst = new LinkedList<Method>();
        entityMetadata.getFieldsWithoutId().forEach(field -> {
            lst.add(entityMetadata.getGetterByField(field));
        });
        lst.add(entityMetadata.getGetterByField(entityMetadata.getIdField()));
        return lst;
    }

    private List<Method> prepareInsertGetters() {
        var lst = new LinkedList<Method>();
        entityMetadata.getFieldsWithoutId().forEach(field -> {
            lst.add(entityMetadata.getGetterByField(field));
        });
        return lst;
    }

    public List<Method> getInsertParamsGetters()
    {
        return insertParamsGetters;
    }

    public List<Method> getUpdateParamsGetters()
    {
        return updateParamsGetters;
    }
}
