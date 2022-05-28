package ru.otus.crm.model.dto;

import java.util.List;

public class ClientDto {
    public String id;
    public String name;
    public String address;
    public List<String> phones;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public List<String> getPhones() {
        return phones;
    }
}
