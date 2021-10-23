package ru.otus.cachehw;

public class MyCacheException extends RuntimeException {
    public MyCacheException(Exception ex) {
        super(ex);
    }
}
