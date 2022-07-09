package ru.otus.crm.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;
import javax.annotation.Nonnull;

@Table("client")
public class Client {
    @Id
    private Long id;
    @Nonnull
    private String name;
    @Transient
    private Address address;
    @MappedCollection(idColumn = "client_id")
    private Set<Phone> phones = new HashSet<Phone>();

    public Client() {
    }

    public Client(String name) {
        this(null, name, new HashSet<Phone>());
    }

    public Client(Long id, String name) {
        this(id, name, new HashSet<Phone>());
    }

    @PersistenceConstructor
    public Client(Long id, String name, Set<Phone> phones) {
        this.id = id;
        this.name = name;
        this.phones = phones;
    }

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Phone> getPhones()
    {
        return phones;
    }

    public void setPhones(Set<Phone> phones) {
        this.phones = phones;
    }

    public void addPhone(Phone phone) {
        phones.add(phone);
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Address getAddress() {
        return address;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}

