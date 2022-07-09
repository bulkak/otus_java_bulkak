package ru.otus.crm.repository;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import ru.otus.crm.model.Address;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Phone;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClientResultSetExtractorClass implements ResultSetExtractor<List<Client>> {

    @Override
    public List<Client> extractData(ResultSet rs) throws SQLException, DataAccessException {
        var clientList = new ArrayList<Client>();
        Long prevClientId = null;
        while (rs.next()) {
            var clientId = (Long) rs.getObject("client_id");
            Client client = null;
            if (prevClientId == null || !prevClientId.equals(clientId)) {
                client = new Client(clientId, rs.getString("client_name"));
                clientList.add(client);
                prevClientId = clientId;
            }
            Long addressId = (Long) rs.getObject("address_id");
            String addressStreet = rs.getString("address_street");
            if (client != null && client.getAddress() == null  && addressId == null) {
                client.setAddress(new Address());
            }
            if (client != null && client.getAddress() == null && addressId != null) {
                client.setAddress(new Address(addressId, addressStreet));
            }
            Long phoneId = (Long) rs.getObject("phone_id");
            if (client != null && phoneId != null) {
                client.addPhone(
                        new Phone(phoneId, clientId, rs.getString("phone_number")));
            }
        }
        return clientList;
    }
}
