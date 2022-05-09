package ru.otus.crm.service;

import com.sun.istack.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.repository.DataTemplate;
import ru.otus.crm.model.Client;
import ru.otus.core.sessionmanager.TransactionManager;
import ru.otus.crm.model.Phone;
import ru.otus.crm.model.dto.ClientDto;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class DbServiceClientImpl implements DBServiceClient {
    private static final Logger log = LoggerFactory.getLogger(DbServiceClientImpl.class);

    private final DataTemplate<Client> clientDataTemplate;
    private final TransactionManager transactionManager;

    public DbServiceClientImpl(TransactionManager transactionManager, DataTemplate<Client> clientDataTemplate) {
        this.transactionManager = transactionManager;
        this.clientDataTemplate = clientDataTemplate;
    }

    @Override
    public Client saveClient(Client client) {
        return transactionManager.doInTransaction(session -> {
            var clientCloned = client.clone();
            if (client.getId() == null) {
                clientDataTemplate.insert(session, clientCloned);
                log.info("created client: {}", clientCloned);
                return clientCloned;
            }
            clientDataTemplate.update(session, clientCloned);
            log.info("updated client: {}", clientCloned);
            return clientCloned;
        });
    }

    @Override
    public Optional<Client> getClient(long id) {
        return transactionManager.doInReadOnlyTransaction(session -> {
            var clientOptional = clientDataTemplate.findById(session, id);
            log.info("client: {}", clientOptional);
            return clientOptional;
        });
    }

    public ClientDto getClientDto(long id) {
        return transactionManager.doInReadOnlyTransaction(session -> {
            var clientOptional = clientDataTemplate.findById(session, id);
            return makeDtoFromClient(clientOptional.orElse(null));
        });
    }

    @Override
    public List<Client> findAll() {
        return transactionManager.doInReadOnlyTransaction(session -> {
            var clientList = clientDataTemplate.findAll(session);
            log.info("clientList:{}", clientList);
            return clientList;
       });
    }

    @Override
    public List<ClientDto> findAllDto() {
        return transactionManager.doInReadOnlyTransaction(session -> {
            var clientList = clientDataTemplate.findAll(session);
            log.info("clientList:{}", clientList);
            return clientList.stream().map(this::makeDtoFromClient).collect(Collectors.toList());
        });
    }

    private ClientDto makeDtoFromClient(@Nullable Client clientOptional)
    {
        var clientDto = new ClientDto();
        if (clientOptional != null) {
            clientDto.id = clientOptional.getId().toString();
            clientDto.name = clientOptional.getName();
            clientDto.address = clientOptional.getAddress().getStreet();
            clientDto.phones = clientOptional.getPhones().stream().map(Phone::getNumber).collect(Collectors.toList());
        }
        return clientDto;
    }
}
