package ru.otus.aop.proxy;

public interface TestLoggingInterface {

    void calculation(int param);

    void calculation(int param, int param2);

    void calculation(int param, int param2, String param3);
}
