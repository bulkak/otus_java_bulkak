package ru.otus.crm.service;

import ru.otus.crm.model.Address;

import java.util.List;
import java.util.Optional;

public interface DBServiceAddress {

    Address saveAddress(Address address);

    Optional<Address> findById(long id);
}
