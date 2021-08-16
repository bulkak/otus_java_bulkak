package ru.otus.aop.proxy;

public class ProxyDemo {
    public static void main(String[] args) {
        TestLoggingInterface myClass = Ioc.createMyClass();
        myClass.calculation(5, 6, "any string");
    }
}
