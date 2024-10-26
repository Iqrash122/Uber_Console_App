import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class User {
    private String username;
    private String password;
    private String email;
    private String phone;
    private String location;
    private String destination; // Added destination field

    public User(String username, String password, String email, String phone, String location) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.location = location;
    }

    public void addUserToDatabase(Connection connection) throws SQLException {
        String sql = "INSERT INTO users (username, password, email, phone, location) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            statement.setString(2, password);
            statement.setString(3, email);
            statement.setString(4, phone);
            statement.setString(5, location);
            statement.executeUpdate();
        }
    }

    public void addFeedbackToDatabase(String feedback, Connection connection) throws SQLException {
        String sql = "INSERT INTO feedback (username, feedback) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            statement.setString(2, feedback);
            statement.executeUpdate();
        }
    }

    // Getter methods
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getLocation() {
        return location;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }
}
