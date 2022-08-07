package com.datasrc.controller;

import lombok.RequiredArgsConstructor;
import com.datasrc.model.ClientDto;
import com.datasrc.service.ClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;


//  http://localhost:8082/data/5
@RestController
@RequiredArgsConstructor
public class ClientController {
    private static final Logger log = LoggerFactory.getLogger(ClientController.class);
    private final ClientService clientService;

    @GetMapping(value = "/api/clients", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<ClientDto> getAll() {
        return clientService.getAll();
    }
}
