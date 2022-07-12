package ru.otus.crm.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.relational.core.mapping.Table;
import javax.annotation.Nonnull;
import java.util.Set;

@Table("address")
public class Address implements Cloneable {
    @Id
    private Long clientId;
    @Nonnull
    private String street;

    public Address() {
    }

    public Address(String street) {
        this(null, street);
    }
    @PersistenceConstructor
    public Address(Long clientId, String street) {
        this.clientId = clientId;
        this.street = street;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }
    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    @Override
    public String toString() {
        return "Address{" +
                "id=" + clientId +
                ", street='" + street + '\'' +
                '}';
    }
}
