package ru.otus.services.processors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.lib.SensorDataBufferedWriter;
import ru.otus.api.SensorDataProcessor;
import ru.otus.api.model.SensorData;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

// Этот класс нужно реализовать
public class SensorDataProcessorBuffered implements SensorDataProcessor {
    private static final Logger log = LoggerFactory.getLogger(SensorDataProcessorBuffered.class);

    private final int bufferSize;
    private final SensorDataBufferedWriter writer;
    private final AtomicBoolean flushingInProgress = new AtomicBoolean(false);
    private final BlockingQueue<SensorData> dataBuffer;

    public SensorDataProcessorBuffered(int bufferSize, SensorDataBufferedWriter writer) {
        this.bufferSize = bufferSize;
        this.writer = writer;
        this.dataBuffer = new ArrayBlockingQueue<>(bufferSize);
    }

    @Override
    public void process(SensorData data) {
        if (!dataBuffer.offer(data)) {
            flush();
            process(data);
        }
    }

    public void flush() {
        try {
            if (flushingInProgress.compareAndSet(false, true)) {
                var bufferedData = new ArrayList<SensorData>();
                while (!dataBuffer.isEmpty()) {
                    bufferedData.add(dataBuffer.poll());
                    if (bufferedData.size() >= bufferSize) {
                        break;
                    }
                }

                if (!bufferedData.isEmpty()) {
                    bufferedData.sort(Comparator.comparing(SensorData::getMeasurementTime));
                    writer.writeBufferedData(bufferedData);
                }
                flushingInProgress.set(false);
            }
        } catch (Exception e) {
            log.error("Ошибка в процессе записи буфера", e);
        }
    }

    @Override
    public void onProcessingEnd() {
        flush();
    }
}
