package ru.otus.model;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

//Допустим, этот класс библиотечный, его нельзя менять
public final class Measurement {
    private final String name;
    private final double value;
    @JsonCreator
    public Measurement(@JsonProperty("name") String name, @JsonProperty("value") double value) {
        System.out.println("JsonCreator makes object...");
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public double getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Measurement{" +
                "name='" + name + '\'' +
                ", value=" + value +
                '}';
    }
}
