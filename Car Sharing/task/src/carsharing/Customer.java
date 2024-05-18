package carsharing;

public class Customer {
    int id;
    String name;
    int rentedCarId;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getRentedCarId() {
        return rentedCarId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setRentedCarId(int rentedCarId) {
        this.rentedCarId = rentedCarId;
    }
}
