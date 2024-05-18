package carsharing;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CompanyDAO {
    private final Connection connection;
    public CompanyDAO(Connection connection){
        this.connection = connection;
    }
    public void addCompany(Company company){
        String sql = "INSERT INTO company (name) VALUES (?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, company.getName());
            statement.executeUpdate();

            // Retrieve the generated ID
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    company.setId(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public Company getCompanyById(int companyId){
        String sql = "SELECT * FROM COMPANY WHERE ID = " + companyId;
        Company company = new Company();
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);
            resultSet.next();
            company.setName(resultSet.getString("NAME"));
            company.setId(resultSet.getInt("ID"));
            return company;
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        return company;
    }
    public List<Company> getAllCompanies(){
        List<Company> companies = new ArrayList<>();
        String sql = "SELECT * FROM COMPANY";
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                Company company = new Company();
                company.setId(resultSet.getInt("id"));
                company.setName(resultSet.getString("name"));
                companies.add(company);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return companies;
    }
}
