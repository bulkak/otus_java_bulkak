package ru.otus.crm.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;
import javax.annotation.Nonnull;
import java.util.Set;

@Table("address")
public class Address implements Cloneable {
    @Id
    private Long addressId;
    @Nonnull
    private String street;

    public Address() {
    }

    public Address(String street) {
        this(null, street);
    }
    @PersistenceConstructor
    public Address(Long addressId, String street) {
        this.addressId = addressId;
        this.street = street;
    }

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long id) {
        this.addressId = id;
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
                "id=" + addressId +
                ", street='" + street + '\'' +
                '}';
    }
}
