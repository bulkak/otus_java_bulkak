package com.datasrc.service;

import com.datasrc.controller.ClientController;
import com.datasrc.model.Client;
import com.datasrc.model.ClientDto;
import com.datasrc.model.Phone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.http.MediaType;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientService {
    private static final Logger log = LoggerFactory.getLogger(ClientController.class);
    private final WebClient webClient;

    public ClientService(WebClient.Builder builder) {
        webClient = builder
                .baseUrl("http://localhost:8282")
                .build();
    }

    public Flux<ClientDto> getAll() {
        log.info("Requested all clients");
        return webClient.get()
                .uri("/api/clients")
                .accept(MediaType.APPLICATION_NDJSON)
                .retrieve()
                .bodyToFlux(Client.class)
                .doOnNext(client -> log.info("Received client {}", client))
                //.map(client -> enrichWithPhones(client, allPhones))
                //.map(client -> enrichWithAddress(client, allAddresses))
                .map(this::makeDto)
                .doOnNext(client -> log.info("Mapped client {}", client));
    }

    private ClientDto makeDto(Client client) {
        return ClientDto.builder()
                .id(client.getId())
                .name(client.getName())
                .address(client.getAddress() != null ? client.getAddress().getStreet() : null)
                .phones(
                        client.getPhones() != null
                                ? client.getPhones()
                                .stream()
                                .map(Phone::getNumber)
                                .collect(Collectors.toList())
                                : List.of()
                )
                .build();
    }
}
