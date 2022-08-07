package com.datasrc.service;

import com.datasrc.model.Client;
import com.datasrc.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@RequiredArgsConstructor
public class ClientService {
    private final ClientRepository clientRepository;
    private final ExecutorService executorService = Executors.newFixedThreadPool(4);

    public Flux<Client> getAll() {
        var future = CompletableFuture
                .supplyAsync(clientRepository::findAll, executorService);
        return Mono.fromFuture(future)
                .flatMapMany(Flux::fromIterable)
                .delayElements(Duration.ofSeconds(2));
    }

    public Mono<Client> get(Long id) {
        var future = CompletableFuture
                .supplyAsync(() -> clientRepository.findById(id).orElseThrow(), executorService);
        return Mono.fromFuture(future)
                .delayElement(Duration.ofSeconds(2));
    }

    public Mono<Client> add(Client client) {
        var future = CompletableFuture
                .supplyAsync(() -> clientRepository.save(client), executorService);
        return Mono.fromFuture(future)
                .delayElement(Duration.ofSeconds(2));
    }
}
