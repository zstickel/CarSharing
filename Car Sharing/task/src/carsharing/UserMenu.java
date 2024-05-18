package carsharing;

import java.sql.Connection;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class UserMenu {
    CompanyDAO companyDAO;
    CarDAO carDAO;
    CustomerDAO customerDAO;

    UserMenu(Connection connection){
        this.companyDAO = new CompanyDAO(connection);
        this.carDAO = new CarDAO(connection);
        this.customerDAO = new CustomerDAO(connection);
    }
    Scanner scanner = new Scanner(System.in);
    public void startMenu(){

        boolean exit = false;
        while(!exit) {
            System.out.println("1. Log in as a manager");
            System.out.println("2. Log in as a customer");
            System.out.println("3. Create a customer");
            System.out.println("0. Exit");
            String selection = scanner.nextLine();
            switch (selection){
                case "1":
                    managerMenu();
                    break;
                case "2":
                    customerMenu();
                    break;
                case "3":
                    createCustomer();
                    break;
                case "0":
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid input");
            }
        }

    }

    private void createCustomer(){
        System.out.println("Enter the customer name:");
        String name = scanner.nextLine();
        Customer customer = new Customer();
        customer.setName(name);
        customerDAO.addCustomer(customer);
        System.out.println("The customer was added!");
    }

    private void customerMenu(){
        List<Customer> customers = customerDAO.getCustomers();
        if (customers.isEmpty()){
            System.out.println("The customer list is empty!");
        }else{
            for(Customer customer : customers){
                System.out.println(customer.getId() + ". " + customer.getName());
            }
            System.out.println("0. Back");
            String input = scanner.nextLine();
            if (!input.equals("0")){
                carMenu(Integer.parseInt(input));
            }
        }
    }

    private void carMenu(int customerNumber){
        boolean exit = false;
        while(!exit) {
            System.out.println("1. Rent a car");
            System.out.println("2. Return a rented car");
            System.out.println("3. My rented car");
            System.out.println("0. Back");
            String selection = scanner.nextLine();
            switch (selection){
                case "1":
                    rentCar(customerNumber);
                    break;
                case "2":
                    returnCar(customerNumber);
                    break;
                case "3":
                    getMyCar(customerNumber);
                    break;
                case "0":
                    exit=true;
                    break;
                default:
                    System.out.println("Invalid selection");

            }
        }
    }
    private void getMyCar(int customerNumber){
        Car myCar = customerDAO.getCar(customerNumber);

        if(myCar != null){
            System.out.println("Car's company id: " + myCar.getCompanyId() );
            Company company = companyDAO.getCompanyById(myCar.getCompanyId());
            System.out.println("Your rented car:");
            System.out.println(myCar.getName());
            System.out.println("Company:");
            System.out.println(company.getName());
        }else{
            System.out.println("You didn't rent a car!");
        }
    }
    private void returnCar(int customerNumber){
        if(customerDAO.hasCar(customerNumber)) {
            customerDAO.returnCar(customerNumber);
            System.out.println("You've returned a rented car!");
        }else{
            System.out.println("You didn't rent a car!");
        }
    }
    private void rentCar(int customerNumber){
        if (customerDAO.hasCar(customerNumber)){
            System.out.println("You've already rented a car!");
            return;
        }
        List<Company> companies = companyDAO.getAllCompanies();
        System.out.println("Choose a company");
        for(Company company: companies){
            System.out.println(company.getId() + ". " + company.getName());
        }
        System.out.println("0. Back");
        String selection = scanner.nextLine();
        if (!selection.equals("0")) {
            chooseModel(customerNumber, Integer.parseInt(selection));
        }
    }
    private void chooseModel(int customerNumber, int companyNumber){
        List<Car> cars = carDAO.getCarsByCompany(companyNumber).stream().filter(car -> !car.getRented()).toList();
        if (cars.isEmpty()){
            System.out.println("No cars for this company");
        }
        System.out.println("Choose a car:");
        int number = 1;
        for(Car car: cars){
                System.out.println(number + ". " + car.getName());
                number++;
        }
        System.out.println("0. Back");
        String selection = scanner.nextLine();
        if(!selection.equals("0")){
            Car car = cars.get(Integer.parseInt(selection)-1);
            customerDAO.addCar(customerNumber, car.getId());
            carDAO.rentCar(car.getId());
            System.out.println("You rented '" + car.getName() + "'");
        }

    }
    private void managerMenu(){
        boolean back = false;
        while (!back) {
            System.out.println("1. Company list");
            System.out.println("2. Create a company");
            System.out.println("0. Back");
            String selection = scanner.nextLine();
            switch (selection) {
                case "1":
                    List<Company> companies = companyDAO.getAllCompanies();
                    if (companies.isEmpty()) {
                        System.out.println("The company list is empty!");
                    } else {
                        System.out.println("Choose the company:");
                        for (Company company : companies) {
                            System.out.println(company.getId() + ". " + company.getName());
                        }
                        System.out.println("0. Back");
                        String secondSelection = scanner.nextLine();
                        if (secondSelection.equals("0")){
                            break;
                        }else {
                            companyMenu(Integer.parseInt(secondSelection));
                        }
                    }
                    break;
                case "2":
                    System.out.println("Enter the company name:");
                    getCompanyandStore();
                    break;
                case "0":
                    back = true;
                    break;
                default:
                    System.out.println("Invalid input");
            }
        }
    }
    private void getCompanyandStore(){
        String companyName = scanner.nextLine();
        Company company = new Company();
        company.setName(companyName);
        companyDAO.addCompany(company);
        System.out.println("The company was created!");
    }
    private void companyMenu(int companyId){
        boolean exit = false;
        while(!exit){
            System.out.println("1. Car list");
            System.out.println("2. Create a car");
            System.out.println("0. Back");
            String input = scanner.nextLine();
            switch (input) {
                case "1" -> listCars(companyId);
                case "2" -> createCar(companyId);
                case "0" -> exit = true;
                default -> System.out.println("Invalid entry");
            }
        }

    }
    private void listCars(int companyId){
        List<Car> cars = carDAO.getCarsByCompany(companyId);
        if (cars.isEmpty()){
            System.out.println("The car list is empty!");
        }else {
            int number = 1;
            for (Car car : cars) {
                System.out.println(number + ". " + car.getName());
                number++;
            }
        }
    }

    private void createCar(int company_id){
        System.out.println("Enter the car name:");
        String carName = scanner.nextLine();
        Car car = new Car();
        car.setName(carName);
        car.setCompanyId(company_id);
        carDAO.addCar(car);
        System.out.println("The car was added!");
    }
}
