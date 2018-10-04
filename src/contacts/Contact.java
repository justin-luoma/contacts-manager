package contacts;

public class Contact implements Comparable<Contact> {
    private String name;
    private long number;

    public Contact(String name, long number) {
        this.name = name;
        this.number = number;
    }

    public String name() {
        return name;
    }

    public void name(String name) {
        this.name = name;
    }

    public long number() {
        return number;
    }

    public void number(long number) {
        this.number = number;
    }

    @Override
    public int compareTo(Contact c) {
        return name.compareTo(c.name());
    }
}
