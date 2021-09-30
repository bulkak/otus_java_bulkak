package ru.otus.dataprocessor;

import ru.otus.model.Measurement;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ProcessorAggregator implements Processor {

    @Override
    public Map<String, Double> process(List<Measurement> data) {
        var map = new TreeMap<String, Double>();
        for (Measurement dataObj : data) {
            if (map.containsKey(dataObj.getName())) {
                map.put(dataObj.getName(), dataObj.getValue() + map.get(dataObj.getName()));
            } else {
                map.put(dataObj.getName(), dataObj.getValue());
            }
        }
        return map;
    }
}
