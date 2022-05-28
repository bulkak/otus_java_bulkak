package ru.otus.appcontainer;

import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class AppComponentsContainerImpl implements AppComponentsContainer {

    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();

    public AppComponentsContainerImpl(Class<?> initialConfigClass) {
        processConfig(initialConfigClass);
    }

    private void processConfig(Class<?> configClass) {
        checkConfigClass(configClass);
        var methodsToAnnotations = new HashMap<AppComponent, Method>();
        var annotations = new ArrayList<AppComponent>();
        for (Method m : configClass.getMethods()) {
            AppComponent annotation = m.getAnnotation(AppComponent.class);
            if (annotation != null) {
                methodsToAnnotations.put(annotation, m);
                annotations.add(annotation);
            }
        }
        Collections.sort(
            annotations,
            Comparator.comparingInt(AppComponent::order)
        );

        try {
            var configClassInstance = configClass.getConstructor().newInstance();
            for (int i = 0; i < annotations.size(); i++) {
                var method = methodsToAnnotations.get(annotations.get(i));
                var parametersTypes = method.getParameterTypes();
                var parameters = new Object[parametersTypes.length];
                for (int j = 0; j < parametersTypes.length; j++) {
                    parameters[j] = getAppComponent(parametersTypes[j]);
                }
                var component = method.invoke(configClassInstance, parameters);
                appComponents.add(component);
                appComponentsByName.put(annotations.get(i).name(), component);
            }
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
        }
    }

    @Override
    public <C> C getAppComponent(Class<C> componentClass) {
        var match = appComponents.stream()
                .filter((component) -> componentClass.isAssignableFrom(component.getClass()))
                .findAny();
        if (match.isPresent()) {
            return (C)match.get();
        }
        throw new RuntimeException("Component not found!");

    }

    @Override
    public <C> C getAppComponent(String componentName) {
        if (appComponentsByName.containsKey(componentName)) {
            return (C)appComponentsByName.get(componentName);
        }
        throw new RuntimeException("Component not found!");
    }
}
