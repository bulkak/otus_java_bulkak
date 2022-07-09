package ru.otus.crm.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;
import javax.annotation.Nonnull;
import java.util.Set;

@Table("address")
public class Address implements Cloneable {
    @Id
    private Long id;

    @Nonnull
    private String street;

    @MappedCollection(keyColumn = "id", idColumn = "address_id")
    private Set<Client> clients;

    public Address() {
    }

    public Address(String street) {
        this(null, street);
    }
    @PersistenceConstructor
    public Address(Long addressId, String street) {
        this.id = addressId;
        this.street = street;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setClients(Set<Client> clients) {
        this.clients = clients;
    }

    public Set<Client> getClients() {
        return clients;
    }

    @Override
    public String toString() {
        return "Address{" +
                "id=" + id +
                ", street='" + street + '\'' +
                '}';
    }
}
