package myjunit;

import java.lang.reflect.InvocationTargetException;

public class Main {
    public static void main(String[] args) throws ClassNotFoundException, InvocationTargetException, IllegalAccessException {
        TestProcessor testProcessor = new TestProcessor();
        testProcessor.process("myjunit.DummyTest");
    }
}
