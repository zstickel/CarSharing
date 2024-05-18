package carsharing;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CarDAO {
    private final Connection connection;
    public CarDAO(Connection connection){
        this.connection = connection;
    }
    public Car getCarById(int carId){
        String sql = "SELECT * FROM CAR WHERE ID = " + carId;
        Car car = new Car();
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                car.setId(resultSet.getInt("ID"));
                car.setName(resultSet.getString("NAME"));
                car.setCompanyId(resultSet.getInt("COMPANY_ID"));
            }
            return car;
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        return car;
    }
    public void rentCar(int carId){
        String sql = "UPDATE CAR SET RENTED = ? WHERE ID = " + carId;
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setBoolean(1,true);
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void addCar(Car car){
        String sql = "INSERT INTO CAR (name, company_id, rented) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, car.getName());
            statement.setInt(2,car.getCompanyId());
            statement.setBoolean(3,false);
            statement.executeUpdate();

            // Retrieve the generated ID
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    car.setId(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public List<Car> getAllCars(){
        List<Car> cars = new ArrayList<>();
        String sql = "SELECT * FROM CAR";
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                Car car = new Car();
                car.setId(resultSet.getInt("id"));
                car.setName(resultSet.getString("name"));
                car.setCompanyId(resultSet.getInt("company_id"));
                car.setRented(resultSet.getBoolean("rented"));
                cars.add(car);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cars;
    }
    public List<Car> getCarsByCompany(int companyId){
        List<Car> cars = new ArrayList<>();
        String sql = "SELECT * FROM CAR WHERE company_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setInt(1, companyId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Car car = new Car();
                car.setId(resultSet.getInt("id"));
                car.setName(resultSet.getString("name"));
                car.setCompanyId(resultSet.getInt("company_id"));
                car.setRented(resultSet.getBoolean("rented"));
                cars.add(car);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cars;
    }
}
