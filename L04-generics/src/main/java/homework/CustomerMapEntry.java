package homework;

import java.util.Map;

class CustomerMapEntry implements Map.Entry<Customer, String> {
    private final Customer key;
    private String value;

    public CustomerMapEntry(Customer key, String value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public Customer getKey() {
        return key;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String setValue(String value) {
        return this.value = value;
    }
}
