package homework;

import java.util.*;

public class CustomerService {

    private final TreeMap<Customer, String> customerMap = new TreeMap<>();

    public Map.Entry<Customer, String> getSmallest() {
        Map.Entry<Customer, String> entry = customerMap.firstEntry();
        if (entry != null) {
            return makeNewEntry(entry);
        }
        return null;
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
        Map.Entry<Customer, String> entry = customerMap.higherEntry(customer);
        if (entry != null) {
            return makeNewEntry(entry);
        }
        return null;
    }

    private Map.Entry<Customer, String> makeNewEntry(Map.Entry<Customer, String> entry)
    {
        TreeMap<Customer, String> anotherMap = new TreeMap<>();
        anotherMap.put(entry.getKey().clone(), entry.getValue());
        return anotherMap.firstEntry();
    }

    public void add(Customer customer, String data) {
        customerMap.put(customer, data);
    }
}
