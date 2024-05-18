package carsharing;

public class Car {
    private int id;
    private String name;
    int companyId;
    private boolean rented = false;

    public String getName() {
        return name;
    }

    public int getCompanyId() {
        return companyId;
    }

    public int getId() {
        return id;
    }
    public boolean getRented(){
        return rented;
    }

    public void setRented(boolean rented) {
        this.rented = rented;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }


}
