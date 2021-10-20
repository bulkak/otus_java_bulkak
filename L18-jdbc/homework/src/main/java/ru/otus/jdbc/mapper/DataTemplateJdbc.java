package ru.otus.jdbc.mapper;

import ru.otus.core.repository.DataTemplate;
import ru.otus.core.repository.DataTemplateException;
import ru.otus.core.repository.executor.DbExecutor;

import java.sql.Connection;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Сохратяет объект в базу, читает объект из базы
 */
public class DataTemplateJdbc<T> implements DataTemplate<T> {

    private final DbExecutor dbExecutor;
    private final EntitySQLMetaData<T> entitySQLMetaData;
    private final InstanceFabric<T> instanceFabric;

    public DataTemplateJdbc(DbExecutor dbExecutor, EntitySQLMetaData<T> entitySQLMetaData, InstanceFabric<T> instanceFabric) {
        this.dbExecutor = dbExecutor;
        this.entitySQLMetaData = entitySQLMetaData;
        this.instanceFabric = instanceFabric;
    }

    @Override
    public Optional<T> findById(Connection connection, long id) {
        return dbExecutor.executeSelect(
            connection,
            entitySQLMetaData.getSelectByIdSql(),
            List.of(id),
            rs -> {
                try {
                    if (rs.next()) {
                        ResultSetMetaData md = rs.getMetaData();
                        return instanceFabric.getInstanceFromResultSet(rs, md);
                    }
                    return null;
                } catch (SQLException e) {
                    throw new DataTemplateJdbcException(e);
                }
            }
        );
    }

    @Override
    public List<T> findAll(Connection connection) {
        return dbExecutor.executeSelect(
            connection,
            entitySQLMetaData.getSelectAllSql(),
            Collections.emptyList(),
            rs -> {
                var objList = new ArrayList<T>();
                try {
                    ResultSetMetaData md = rs.getMetaData();
                    while (rs.next()) {
                        objList.add(instanceFabric.getInstanceFromResultSet(rs, md));
                    }
                    return objList;
                } catch (SQLException e) {
                    throw new DataTemplateJdbcException(e);
                }
            }
        ).orElseThrow(() -> new RuntimeException("Unexpected error"));
    }

    @Override
    public long insert(Connection connection, T client) {
        try {
            return dbExecutor.executeStatement(
                    connection,
                    entitySQLMetaData.getInsertSql(),
                    entitySQLMetaData.getInsertParams(client)
            );
        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }

    @Override
    public void update(Connection connection, T client) {
        try {
            dbExecutor.executeStatement(
                    connection,
                    entitySQLMetaData.getUpdateSql(),
                    entitySQLMetaData.getUpdateParams(client)
            );
        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }
}
