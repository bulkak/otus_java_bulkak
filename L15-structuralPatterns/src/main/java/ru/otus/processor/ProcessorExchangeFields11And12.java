package ru.otus.processor;

import ru.otus.model.Message;

public class ProcessorExchangeFields11And12 implements Processor {

    @Override
    public Message process(Message message) {
        var field11Value = message.getField11();
        var field12Value = message.getField12();
        return message.toBuilder()
                .field12(field11Value)
                .field11(field12Value)
                .build();
    }
}
