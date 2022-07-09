package ru.otus.crm.repository;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import ru.otus.crm.model.Address;
import ru.otus.crm.model.Client;

import java.util.List;


public interface AddressRepository extends CrudRepository<Address, Long> {
}
