package com.datasrc.controller;

import com.datasrc.model.Client;
import com.datasrc.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class ClientController {
    private static final Logger log = LoggerFactory.getLogger(ClientController.class);
    private final ClientService clientService;

    @GetMapping(value = "/api/clients", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<Client> getAll() {
        return clientService.getAll();
    }

    @GetMapping(value = "/api/clients/{id}", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Mono<Client> get(@PathVariable Long id) {
        return clientService.get(id);
    }

    @PostMapping(value = "/api/clients", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Mono<Client> add(@RequestBody Client client) {
        return clientService.add(client);
    }
}
