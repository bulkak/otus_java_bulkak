package ru.otus.crm.repository;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import ru.otus.crm.model.Client;

import java.util.List;


public interface ClientRepository extends CrudRepository<Client, Long> {
    // закоментируйте, чтобы получить N+1
    @Override
    @Query(value = """
            select c.id           as client_id,
                   c.name         as client_name,
                   a.id           as address_id,
                   a.street       as address_street,
                   p.id           as phone_id,
                   p.number       as phone_number
            from client c
                     left outer join address a
                                     on a.id = c.address_id
                     left outer join phone p
                                     on p.client_id = c.id
            order by c.id
                                                          """,
            resultSetExtractorClass = ClientResultSetExtractorClass.class)
    List<Client> findAll();
}
