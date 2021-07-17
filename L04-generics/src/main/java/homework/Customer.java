package homework;


import org.jetbrains.annotations.NotNull;

public class Customer implements Comparable<Customer>, Cloneable {
    private final long id;
    private String name;
    private long scores;

    public Customer(long id, String name, long scores) {
        this.id = id;
        this.name = name;
        this.scores = scores;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getScores() {
        return scores;
    }

    public void setScores(long scores) {
        this.scores = scores;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", scores=" + scores +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Customer customer = (Customer) o;

        return id == customer.id;
    }

    @Override
    public int hashCode() {
        return Math.toIntExact(id);
    }

    @Override
    public int compareTo(@NotNull Customer o) {
        if (o.getScores() > scores) {
            return -1;
        } else if (o.getScores() == scores) {
            return 0;
        }
        return 1;
    }

    @Override
    protected Customer clone() {
        try {
            return (Customer) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
