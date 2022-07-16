package ru.otus.protobuf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import ru.otus.protobuf.generated.RemoteCounterServiceGrpc;
import ru.otus.protobuf.generated.CounterServerLimits;
import ru.otus.protobuf.generated.CounterServerResponse;

import java.util.Optional;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingDeque;

public class GRPCClient {

    private static final Logger log = LoggerFactory.getLogger(GRPCClient.class);
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 8190;
    final static BlockingDeque<Long> currentValueQueue;

    static {
        currentValueQueue = new LinkedBlockingDeque<>();
    }

    public static void main(String[] args) throws InterruptedException {
        var channel = ManagedChannelBuilder.forAddress(SERVER_HOST, SERVER_PORT)
                .usePlaintext()
                .build();
        System.out.println("\n\n\nLet's ROCK!!!\n\n");
        var latch = new CountDownLatch(1);
        var newStub = RemoteCounterServiceGrpc.newStub(channel);
        newStub.startCounter(
            CounterServerLimits.newBuilder().setFirstValue(0).setLastValue(30).build(),
            new StreamObserver<>() {
                @Override
                public void onNext(CounterServerResponse um) {
                    currentValueQueue.add(um.getCurrentValue());
                }

                @Override
                public void onError(Throwable t) {
                    System.err.println(t.getMessage());
                    currentValueQueue.clear();
                }

                @Override
                public void onCompleted() {
                    System.out.println("\n\nThat's ALL!");
                    currentValueQueue.clear();
                    latch.countDown();
                }
            }
        );
        GRPCClient clientCounter = new GRPCClient();
        // можно было и синхронно, но гулять так гулять
        var conterThead = new Thread(clientCounter::startFori);
        conterThead.start();

        latch.await();

        conterThead.join();
        channel.shutdown();
    }

    private void startFori() {
        var currentValue = 0L;
        for (int i = 0; i <= 50; i++) {
            long currentFromServer = Optional.ofNullable(currentValueQueue.pollLast()).orElse(0L);
            currentValueQueue.clear();
            currentValue = currentValue + currentFromServer + 1L;
            if (currentFromServer > 0) {
                log.info(String.format("value from server:%s", currentFromServer));
            }
            log.info(String.format("currentValue:%s", currentValue));
            sleep();
        }
    }

    private static void sleep() {
        try {
            Thread.sleep(1_000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
