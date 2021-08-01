package myjunit;

import myjunit.annotations.After;
import myjunit.annotations.Before;
import myjunit.annotations.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;

public class TestProcessor {
    private final ArrayList<Method> before = new ArrayList<>();
    private final ArrayList<Method> after = new ArrayList<>();
    private final ArrayList<Method> test = new ArrayList<>();
    private Object testObjectInstance;

    public void process(String className) throws ClassNotFoundException, InvocationTargetException, IllegalAccessException {
        Class<?> clazz = Class.forName(className);
        buildExecutionPlan(clazz);
        int countPassedTests = 0;
        int countFailedTests = 0;
        for (Method method : test) {
            boolean result = executeTest(clazz, method);
            if (result) {
                countPassedTests++;
            } else {
                countFailedTests++;
            }
        }
        System.out.printf("Tests executed: %d%n", countPassedTests + countFailedTests);
        System.out.printf("Tests passed: %d%n", countPassedTests);
        System.out.printf("Tests failed: %d%n", countFailedTests);
    }

    private void buildExecutionPlan(Class<?> clazz) {
        Method[] methodsAll = clazz.getMethods();
        Arrays.stream(methodsAll).forEach(method -> {
            Annotation[] annotations = method.getDeclaredAnnotations();
            Arrays.stream(annotations).forEach(annotation -> {
                if (annotation instanceof Before) {
                    before.add(method);
                }
                if (annotation instanceof After) {
                    after.add(method);
                }
                if (annotation instanceof Test) {
                    test.add(method);
                }
            });
        });
    }


    private boolean executeTest(Class<?> clazz, Method testMethod) throws InvocationTargetException, IllegalAccessException {
        try {
            Constructor<?> constructor = clazz.getConstructor();
            testObjectInstance = constructor.newInstance();
            for (Method method : before) {
                method.invoke(testObjectInstance);
            }
            testMethod.invoke(testObjectInstance);
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            if (testObjectInstance != null) {
                for (Method method : after) {
                    method.invoke(testObjectInstance);
                }
            }
        }
    }
}
