package fecasado.citius.usc.tecalischallenge;

/**
 * This class represents a customer, with ID, name and address
 */
public class Customer {
    private short ID;
    private String firstName;
    private String lastName;
    private String street;
    private String city;

    public Customer() {
        this.ID = -1;
        this.firstName = "unknown";
        this.lastName = "unknown";
        this.street = "unknown";
        this.city = "unknown";
    }

    public Customer(short ID, String firstName, String lastName, String street, String city) {
        this.ID = ID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.street = street;
        this.city = city;
    }

    public short getID() {
        return ID;
    }

    public void setID(short ID) {
        this.ID = ID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public String toString() {
        String data = this.getFirstName() + " " + this.getLastName() + ", \n"
                + this.getStreet() + ", \n"
                + this.getCity();
        return data;
    }
}
