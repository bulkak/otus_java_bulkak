package ru.otus.aop.proxy;

import ru.otus.aop.proxy.annotations.Log;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashMap;

class Ioc {

    private Ioc() {
    }

    static TestLoggingInterface createMyClass() {
        InvocationHandler handler = new DemoInvocationHandler(new TestLoggingImpl());
        return (TestLoggingInterface) Proxy.newProxyInstance(Ioc.class.getClassLoader(),
                new Class<?>[]{TestLoggingInterface.class}, handler);
    }

    static class DemoInvocationHandler implements InvocationHandler {
        private final TestLoggingInterface myClass;
        private final HashMap<String, AnnotatedType[]> logMethods = new HashMap<>();

        DemoInvocationHandler(TestLoggingInterface myClass) {
            this.myClass = myClass;
            Class<?> clazz = myClass.getClass();
            defineMethodsToLog(clazz);
        }

        private void defineMethodsToLog(Class<?> clazz) {
            Method[] methodsAll = clazz.getMethods();
            Arrays.stream(methodsAll).forEach(method -> {
                Annotation[] annotations = method.getDeclaredAnnotations();
                Arrays.stream(annotations).forEach(annotation -> {
                    if (method.isAnnotationPresent(Log.class)) {
                        logMethods.put(method.getName(), method.getAnnotatedParameterTypes());
                    }
                });
            });
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (logMethods.containsKey(method.getName()) &&
                    Arrays.equals(logMethods.get(method.getName()), method.getAnnotatedParameterTypes())
            ) {
                System.out.println("executed method: " + method.getName() + ", params:" + Arrays.toString(args));
            }
            return method.invoke(myClass, args);
        }

        @Override
        public String toString() {
            return "DemoInvocationHandler{" +
                    "myClass=" + myClass +
                    '}';
        }
    }
}
