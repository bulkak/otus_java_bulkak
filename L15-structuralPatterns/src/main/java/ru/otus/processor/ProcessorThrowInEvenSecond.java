package ru.otus.processor;

import ru.otus.model.Message;

import java.time.Clock;
import java.time.Instant;

public class ProcessorThrowInEvenSecond implements Processor {
    private final Clock clock;

    public ProcessorThrowInEvenSecond(Clock clock) {
        this.clock = clock;
    }

    @Override
    public Message process(Message message) {
        var currentSecond = Instant.now(clock).getEpochSecond();
        if (currentSecond % 2 == 0) {
            throw new RuntimeException("What's the hell?!");
        }
        return message;
    }
}
