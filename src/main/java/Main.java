import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        // Database connection details
        String url = "jdbc:mysql://localhost:3306/PIVRide";
        String username = "username"; // Replace with your DB username
        String password = "password"; // Replace with your DB password

        Collection<User> users = new ArrayList<>();
        Collection<Driver> drivers = new ArrayList<>();
        drivers.add(new Driver("Driver A"));
        drivers.add(new Driver("Driver B"));
        drivers.add(new Driver("Driver C"));

        Scanner scanner = new Scanner(System.in);
        int choice;

        // try (Connection connection = DriverManager.getConnection(url, username, password)) {
        do {
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    System.out.println("Enter new username:");
                    String regUsername = scanner.nextLine();
                    System.out.println("Enter new password:");
                    String regPassword = scanner.nextLine();
                    System.out.println("Enter email:");
                    String regEmail = scanner.nextLine();
                    System.out.println("Enter phone:");
                    String regPhone = scanner.nextLine();
                    System.out.println("Enter location:");
                    String regLocation = scanner.nextLine();

                    // Register user and insert into database
                    User newUser = new User(regUsername, regPassword, regEmail, regPhone, regLocation);
                    users.add(newUser);
                    // addUserToDatabase(newUser, connection);
                    System.out.println("Your account has been registered!");
                    break;

                case 2:
                    System.out.println("Enter your username:");
                    String loginUsername = scanner.nextLine();
                    System.out.println("Enter your password:");
                    String loginPassword = scanner.nextLine();

                    // Check if user exists and login
                    User loggedInUser = login(users, loginUsername, loginPassword);
                    if (loggedInUser != null) {
                        System.out.println("Login successful!");
                        displayAvailableDrivers(drivers);
                        System.out.print("Choose a driver (enter driver's number): ");
                        int driverChoice = scanner.nextInt();
                        scanner.nextLine(); // Consume newline

                        selectDriver(drivers, driverChoice);

                        System.out.println("Enter your destination:");
                        String destination = scanner.nextLine();
                        loggedInUser.setDestination(destination);

                        // Pause for 5 seconds
                        try {
                            Thread.sleep(5000); // 5 seconds
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        // Open feedback option
                        System.out.println("Please provide your feedback about the driver:");
                        String feedback = scanner.nextLine();
                        // loggedInUser.addFeedbackToDatabase(feedback, connection); // Store feedback in database
                        System.out.println("Thank you for your feedback!");

                        // Optionally, you can allow the user to perform more actions here or log out
                    } else {
                        System.out.println("Invalid username or password. Please try again.");
                    }
                    break;

                case 0:
                    System.out.println("Exiting the program...");
                    break;

                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        } while (choice != 0);

        // } catch (SQLException e) {
        //     e.printStackTrace();
        // }
    }

    private static User login(Collection<User> users, String username, String password) {
        return users.stream()
                .filter(user -> username.equals(user.getUsername()) && password.equals(user.getPassword()))
                .findFirst()
                .orElse(null);
    }

    private static void displayAvailableDrivers(Collection<Driver> drivers) {
        System.out.println("Available Drivers:");
        int driverNumber = 1;
        for (Driver driver : drivers) {
            System.out.println(driverNumber + ". " + driver.getName());
            driverNumber++;
        }
    }

    private static void selectDriver(Collection<Driver> drivers, int choice) {
        int driverIndex = choice - 1;
        if (driverIndex >= 0 && driverIndex < drivers.size()) {
            Driver selectedDriver = drivers.stream().skip(driverIndex).findFirst().orElse(null);
            if (selectedDriver != null) {
                System.out.println("You have selected: " + selectedDriver.getName());
            }
        } else {
            System.out.println("Invalid driver choice.");
        }
    }

    private static void addUserToDatabase(User user, Connection connection) throws SQLException {
        String sql = "INSERT INTO users (username, password, email, phone, location) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getEmail());
            statement.setString(4, user.getPhone());
            statement.setString(5, user.getLocation());
            statement.executeUpdate();
        }
    }
}
