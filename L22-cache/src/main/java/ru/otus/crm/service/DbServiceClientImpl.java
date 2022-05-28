package ru.otus.crm.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.cachehw.MyCache;
import ru.otus.core.repository.DataTemplate;
import ru.otus.crm.model.Client;
import ru.otus.core.sessionmanager.TransactionRunner;

import java.util.List;
import java.util.Optional;

public class DbServiceClientImpl implements DBServiceClient {
    private static final Logger log = LoggerFactory.getLogger(DbServiceClientImpl.class);

    private final DataTemplate<Client> clientDataTemplate;
    private final TransactionRunner transactionRunner;
    private MyCache<String, Optional<Client>> cache = null;

    public DbServiceClientImpl(TransactionRunner transactionRunner, DataTemplate<Client> clientDataTemplate) {
        this.transactionRunner = transactionRunner;
        this.clientDataTemplate = clientDataTemplate;
    }

    public DbServiceClientImpl(TransactionRunner transactionRunner, DataTemplate<Client> clientDataTemplate, MyCache<String, Optional<Client>> cache) {
        this.transactionRunner = transactionRunner;
        this.clientDataTemplate = clientDataTemplate;
        this.cache = cache;
    }

    @Override
    public Client saveClient(Client client) {
        return transactionRunner.doInTransaction(connection -> {
            if (client.getId() == null) {
                var clientId = clientDataTemplate.insert(connection, client);
                var createdClient = new Client(clientId, client.getName());
                log.info("created client: {}", createdClient);
                return createdClient;
            }
            clientDataTemplate.update(connection, client);
            removeFromCache(client.getId());
            log.info("updated client: {}", client);
            return client;
        });
    }

    @Override
    public Optional<Client> getClient(long id) {
        return transactionRunner.doInTransaction(connection -> {
            var resultClient = getFromCache(id);
            if (resultClient.isEmpty()) {
                resultClient = clientDataTemplate.findById(connection, id);
                log.info("client: {}", resultClient);
                resultClient.ifPresent(client -> putToCache(client.getId(), client));
            };
            return resultClient;
        });
    }

    @Override
    public List<Client> findAll() {
        return transactionRunner.doInTransaction(connection -> {
            var clientList = clientDataTemplate.findAll(connection);
            log.info("clientList:{}", clientList);
            return clientList;
        });
    }

    private Optional<Client> getFromCache(Long id) {
        if (cache == null) {
            return Optional.empty();
        }
        var data = cache.get(getCacheKeyFromId(id));
        return data != null ? data : Optional.empty();
    }

    private void putToCache(Long id, Client client) {
        if (cache != null) {
            cache.put(getCacheKeyFromId(id), Optional.of(client));
        }
    }

    private void removeFromCache(Long id) {
        if (cache != null) {
            cache.remove(getCacheKeyFromId(id));
        }
    }

    private String getCacheKeyFromId(Long id)
    {
        return "key_" + id.toString();
    }
}