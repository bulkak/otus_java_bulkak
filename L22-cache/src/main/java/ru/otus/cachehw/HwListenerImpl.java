package ru.otus.cachehw;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HwListenerImpl<K, V> implements HwListener<K, V> {
    private static final Logger log = LoggerFactory.getLogger(HwListenerImpl.class);
    @Override
    public void notify(K key, V value, String action) {
        log.info(action + ':' + value.toString());
    }
}
