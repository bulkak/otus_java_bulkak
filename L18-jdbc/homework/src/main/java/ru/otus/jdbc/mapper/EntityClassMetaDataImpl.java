package ru.otus.jdbc.mapper;


import ru.otus.jdbc.mapper.annotation.IdEntityField;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T> {
    private String name;
    private Constructor<T> constructor;
    private List<Field> allFields = new ArrayList<>();
    private List<Field> allFieldsWithoutId = new ArrayList<>();
    private Field idField;

    private final Class<T> reflectionClass;

    public EntityClassMetaDataImpl(Class<T> entityClass) {
        this.reflectionClass = entityClass;
        name = entityClass.getSimpleName();
        Field[] fieldsAll = entityClass.getDeclaredFields();
        Arrays.stream(fieldsAll).forEach(field -> {
            if (field.isAnnotationPresent(IdEntityField.class)) {
                idField = field;
            } else {
                allFieldsWithoutId.add(field);
            }
            allFields.add(field);
        });
        try {
            constructor = entityClass.getConstructor(allFields.stream().map(Field::getType).toArray(Class[]::new));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Constructor<T> getConstructor() {
        return constructor;
    }

    @Override
    public Field getIdField() {
        return idField;
    }

    @Override
    public List<Field> getAllFields() {
        return allFields;
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        return allFieldsWithoutId;
    }

    @Override
    public Method getGetterByField(Field field) {
        String methodName = "get" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
        try {
            return reflectionClass.getMethod(methodName);
        } catch (NoSuchMethodException e) {
            System.out.println("no such method:" + methodName);
        }
        return null;
    }
}
