package carsharing;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class Main {

    public static void main(String[] args) {
        // write your code here
        //still need to impliment getting the database name from the command line argument
        try {
            Class.forName("org.h2.Driver");
            Connection conn = DriverManager.getConnection("jdbc:h2:./src/carsharing/db/carsharing");
            conn.setAutoCommit(true);
            Statement stmt = conn.createStatement();

            // Create a table named "Company"
            String createTableQuery = "CREATE TABLE IF NOT EXISTS COMPANY (" +
                    "ID INT PRIMARY KEY AUTO_INCREMENT, " +
                    "NAME VARCHAR(255) NOT NULL UNIQUE)";
            stmt.executeUpdate(createTableQuery);
            String createCarTableQuery = "CREATE TABLE IF NOT EXISTS CAR (" +
                    "ID INT PRIMARY KEY AUTO_INCREMENT, " +
                    "NAME VARCHAR(255) NOT NULL UNIQUE," +
                    "RENTED BOOLEAN NOT NULL," +
                    "COMPANY_ID INT NOT NULL," +
                    "FOREIGN KEY (COMPANY_ID) REFERENCES COMPANY(id))";

            System.out.println("Table 'Company' created successfully!");
            stmt.executeUpdate(createCarTableQuery);

            String createCustomerTableQuery = "CREATE TABLE IF NOT EXISTS CUSTOMER(" +
                    "ID INT PRIMARY KEY AUTO_INCREMENT," +
                    "NAME VARCHAR(255) NOT NULL UNIQUE," +
                    "RENTED_CAR_ID INT," +
                    "FOREIGN KEY (RENTED_CAR_ID) REFERENCES CAR(ID))";
            stmt.executeUpdate(createCustomerTableQuery);
            UserMenu userMenu = new UserMenu(conn);
            userMenu.startMenu();

            // Clean up resources
            stmt.close();
            conn.close();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}