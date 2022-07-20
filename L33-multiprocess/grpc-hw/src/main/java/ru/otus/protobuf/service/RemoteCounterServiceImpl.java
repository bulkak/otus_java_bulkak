package ru.otus.protobuf.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.grpc.stub.StreamObserver;
import ru.otus.protobuf.generated.RemoteCounterServiceGrpc;
import ru.otus.protobuf.generated.CounterServerResponse;
import ru.otus.protobuf.generated.CounterServerLimits;

public class RemoteCounterServiceImpl extends RemoteCounterServiceGrpc.RemoteCounterServiceImplBase {

    private static final Logger log = LoggerFactory.getLogger(RemoteCounterServiceImpl.class);
    @Override
    public void startCounter(CounterServerLimits request, StreamObserver<CounterServerResponse> responseObserver) {
        long fistValue = request.getFirstValue();
        long lastValue =  request.getLastValue();
        for (long i = fistValue + 1L; i <= lastValue; i++) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info(String.format("sending value: %s", i));
            responseObserver.onNext(value2Response(i));
        }
        responseObserver.onCompleted();
    }
    private CounterServerResponse value2Response(long value) {
        return CounterServerResponse.newBuilder()
                .setCurrentValue(value)
                .build();
    }
}
