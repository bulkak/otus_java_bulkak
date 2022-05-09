package ru.otus.jdbc.mapper;


import ru.otus.jdbc.mapper.annotation.IdEntityField;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T> {
    private final String name;
    private final Constructor<T> constructor;
    private final List<Field> allFields = new ArrayList<>();
    private final List<Field> allFieldsWithoutId = new ArrayList<>();
    private final Field idField;
    private final Map<Field, Method> getters = new HashMap<>();

    private final Class<T> reflectionClass;

    public EntityClassMetaDataImpl(Class<T> entityClass) throws NoSuchMethodException {
        this.reflectionClass = entityClass;
        this.name = entityClass.getSimpleName();
        Field[] fieldsAll = entityClass.getDeclaredFields();
        Field localIdFiled = null;
        for (Field field : fieldsAll) {
            if (field.isAnnotationPresent(IdEntityField.class)) {
                localIdFiled = field;
            } else {
                this.allFieldsWithoutId.add(field);
            }
            this.allFields.add(field);
            setGetter(field);

        }
        this.idField = localIdFiled;
        this.constructor = entityClass.getConstructor(allFields.stream().map(Field::getType).toArray(Class[]::new));
    }

    private void setGetter(Field field)
    {
        String methodName = "get" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
        try {
            var method = reflectionClass.getMethod(methodName);
            this.getters.put(field, method);
        } catch (NoSuchMethodException e) {
            System.out.println("no such method:" + methodName);
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
        if (getters.containsKey(field)) {
            return getters.get(field);
        }
        return null;
    }
}
