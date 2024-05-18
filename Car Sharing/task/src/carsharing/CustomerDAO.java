package carsharing;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {
    private Connection connection;
    private CarDAO carDAO;
    public CustomerDAO(Connection connection){
        this.connection = connection;
        carDAO = new CarDAO(connection);
    }

    public boolean hasCar(int customerId){
        String sql = "SELECT * FROM CUSTOMER WHERE ID = " + customerId;
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);
            resultSet.next();
            return resultSet.getInt("RENTED_CAR_ID") != 0;
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return false;
    }
    public List<Customer> getCustomers(){
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM CUSTOMER";
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                Customer customer = new Customer();
                customer.setId(resultSet.getInt("id"));
                customer.setName(resultSet.getString("name"));
                if (resultSet.getInt("RENTED_CAR_ID") != 0) {
                    customer.setRentedCarId(resultSet.getInt("RENTED_CAR_ID"));
                }
                customers.add(customer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customers;
    }
    public void addCustomer(Customer customer){
        String sql = "INSERT INTO CUSTOMER (name) VALUES (?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, customer.getName());
            statement.executeUpdate();

            // Retrieve the generated ID
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    customer.setId(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void addCar(int customerNumber, int rentalCarId){
        String sql = "UPDATE CUSTOMER SET RENTED_CAR_ID = ? WHERE ID = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, rentalCarId);
            statement.setInt(2, customerNumber);
            statement.executeUpdate();

            // Retrieve the generated ID
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void returnCar(int customerNumber){
        String sql = "UPDATE CUSTOMER SET RENTED_CAR_ID = NULL WHERE ID = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, customerNumber);
            statement.executeUpdate();

            // Retrieve the generated ID
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public Car getCar(int customerNumber){
        String sql = "SELECT * FROM CUSTOMER WHERE ID = " + customerNumber;
        Car car = new Car();
        try (Statement statement = connection.createStatement()) {
            int carId;
            ResultSet resultSet = statement.executeQuery(sql);
            resultSet.next() ;
            carId = resultSet.getInt("RENTED_CAR_ID");
            if (!(carId ==0)) {
                car = carDAO.getCarById(carId);
                return car;
            }else{
                return null;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
