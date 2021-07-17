package homework;

import java.util.LinkedList;

public class CustomerReverseOrder {

    private final LinkedList<Customer> customerList = new LinkedList<>();

    public void add(Customer customer) {
        customerList.add(customer);
    }

    public Customer take() {
        return customerList.removeLast();
    }
}
