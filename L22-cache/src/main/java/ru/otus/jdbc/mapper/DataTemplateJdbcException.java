package ru.otus.jdbc.mapper;

public class DataTemplateJdbcException extends RuntimeException {
    public DataTemplateJdbcException(Exception ex) {
        super(ex);
    }
}
