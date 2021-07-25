package homework;

import java.util.*;

public class CustomerService {

    private final TreeMap<Customer, String> customerMap = new TreeMap<>(
        (o1, o2) -> {
            if (o2.getScores() > o1.getScores()) {
                return -1;
            } else if (o2.getScores() == o1.getScores()) {
                return 0;
            }
            return 1;
        }
    );

    public Map.Entry<Customer, String> getSmallest() {
        Map.Entry<Customer, String> entry = customerMap.firstEntry();
        if (entry != null) {
            entry =  makeNewEntry(entry);
        }
        return entry;
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
        Map.Entry<Customer, String> entry = customerMap.higherEntry(customer);
        if (entry != null) {
            entry =  makeNewEntry(entry);
        }
        return entry;
    }

    private CustomerMapEntry makeNewEntry(Map.Entry<Customer, String> entry)
    {
        return new CustomerMapEntry(entry.getKey().clone(), entry.getValue());
    }

    public void add(Customer customer, String data) {
        customerMap.put(customer, data);
    }
}
