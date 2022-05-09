package ru.otus.jdbc.mapper;

import java.lang.reflect.Method;
import java.util.List;

/**
 * Создает SQL - зап
 */
public interface EntitySQLMetaData<T> {
    String getSelectAllSql();

    String getSelectByIdSql();

    String getInsertSql();

    String getUpdateSql();

    List<Method> getInsertParamsGetters();

    List<Method> getUpdateParamsGetters();
}
