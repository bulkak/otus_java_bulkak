package ru.otus.aop.proxy;

import ru.otus.aop.proxy.annotations.Log;

public class TestLoggingImpl implements TestLoggingInterface {

    public void calculation(int param) {
        System.out.println("calc result: " + param);
    }

    public void calculation(int param, int param2) {
        System.out.println("calc result: " + param + param2);
    }

    @Log
    public void calculation(int param, int param2, String param3) {
        System.out.println("calc result: " + param + param2 + param3);
    }

    @Override
    public String toString() {
        return "TestLoggingImpl{}";
    }
}
