package homework;

import java.util.ArrayDeque;

public class CustomerReverseOrder {

    private final ArrayDeque<Customer> customerQueue = new ArrayDeque<>();

    public void add(Customer customer) {
        customerQueue.push(customer);
    }

    public Customer take() {
        return customerQueue.pop();
    }
}
