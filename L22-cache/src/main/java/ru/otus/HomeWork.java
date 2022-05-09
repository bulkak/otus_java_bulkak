package ru.otus;

import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.cachehw.HwListenerImpl;
import ru.otus.cachehw.MyCache;
import ru.otus.core.repository.executor.DbExecutorImpl;
import ru.otus.core.sessionmanager.TransactionRunnerJdbc;
import ru.otus.crm.datasource.DriverManagerDataSource;
import ru.otus.crm.model.Client;
import ru.otus.crm.service.DbServiceClientImpl;
import ru.otus.jdbc.mapper.*;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Optional;

/**
 * VM options: -Xmx16m -Xms16m -Xlog:gc=debug
 */
public class HomeWork {
    private static final String URL = "jdbc:postgresql://localhost:5430/demoDB";
    private static final String USER = "usr";
    private static final String PASSWORD = "pwd";

    private static final Logger log = LoggerFactory.getLogger(HomeWork.class);

    public static void main(String[] args) throws NoSuchMethodException {

        var dataSource = new DriverManagerDataSource(URL, USER, PASSWORD);
        flywayMigrations(dataSource);
        var transactionRunner = new TransactionRunnerJdbc(dataSource);
        var dbExecutor = new DbExecutorImpl();

        EntityClassMetaData<Client> entityClassMetaDataClient = new EntityClassMetaDataImpl<>(Client.class);
        EntitySQLMetaData<Client> entitySQLMetaDataClient = new EntitySQLMetaDataImpl<>(entityClassMetaDataClient);
        InstanceFabric<Client> instanceFabricClient = new InstanceFabricImpl<>(entityClassMetaDataClient);
        var dataTemplateClient = new DataTemplateJdbc<>(dbExecutor, entitySQLMetaDataClient, instanceFabricClient);

        var dbServiceClient = new DbServiceClientImpl(transactionRunner, dataTemplateClient);

        for (var i = 1; i <= 500; i++) {
            dbServiceClient.saveClient(new Client(String.format("client_number_%s", i)));
        }

        //without cache
        var memoryBoom = new ArrayList<Optional<Client>>();
        long startTime = System.currentTimeMillis();
        for (var i = 1; i <= 100000; i++) {
            memoryBoom.add(dbServiceClient.getClient((long) (Math.random() * 500)));
        }
        long delta_no_cache = System.currentTimeMillis() - startTime;
        memoryBoom = null;

        //with cache
        var cache = new MyCache<String, Optional<Client>>();
        cache.addListener(new HwListenerImpl<>());

        var dbServiceCachedClient = new DbServiceClientImpl(transactionRunner, dataTemplateClient, cache);

        memoryBoom = new ArrayList<Optional<Client>>();
        startTime = System.currentTimeMillis();
        for (var i = 1; i <= 100000; i++) {
            memoryBoom.add(dbServiceCachedClient.getClient((long) (Math.random() * 500)));
        }
        long delta_cache = System.currentTimeMillis() - startTime;
        memoryBoom = null;
        //spend msec NO cache:90513, sec:90
        System.out.println("spend msec NO cache:" + delta_no_cache + ", sec:" + (delta_no_cache / 1000));
        //spend msec WITH cache:1386, sec:1 (it's alive!!)
        System.out.println("spend msec WITH cache:" + delta_cache + ", sec:" + (delta_cache / 1000));
        // with more objects gc clear cache weakMap, log:
        // Size of cache: 176
        //...
        //Size of cache: 24
        //Size of cache: 135
        //Size of cache: 184
        //Size of cache: 58
    }

    private static void flywayMigrations(DataSource dataSource) {
        log.info("db migration started...");
        var flyway = Flyway.configure()
                .dataSource(dataSource)
                .locations("classpath:/db/migration")
                .load();
        flyway.migrate();
        log.info("db migration finished.");
        log.info("***");
    }
}
