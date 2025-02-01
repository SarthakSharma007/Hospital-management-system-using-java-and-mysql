package hospitalmanagmentsystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Patient {
    private Connection connection;
    private Scanner scanner;

    public Patient(Connection connection, Scanner scanner) {
        this.connection = connection;
        this.scanner = scanner;
    }

    public void addPatient() {
        scanner.nextLine(); // Consume any leftover newline character

        System.out.print("Enter Patient Name: ");
        String name = scanner.nextLine(); // Allow full names

        int age = 0;
        while (true) {  // Loop until valid input
            System.out.print("Enter Patient Age: ");
            if (scanner.hasNextInt()) { // Check if input is an integer
                age = scanner.nextInt();
                scanner.nextLine(); // Consume newline after integer input
                break;
            } else {
                System.out.println("Invalid input! Please enter a valid age.");
                scanner.next(); // Discard invalid input
            }
        }

        System.out.print("Enter Patient Gender: ");
        String gender = scanner.next(); // Assuming gender is a single word like Male/Female

        try {
            String query = "INSERT INTO patients(name, age, gender) VALUES(?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, age);
            preparedStatement.setString(3, gender);

            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("Patient Added Successfully.");
            } else {
                System.out.println("Failed to add Patient.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void viewPatients() {
        String query = "SELECT * FROM patients";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            System.out.println("Patients:  ");
            System.out.println("+------------+--------------------+------------+-------------+");
            System.out.println("| Patient ID | Name               | Age        | Gender      |");
            System.out.println("+------------+--------------------+------------+-------------+");

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int age = resultSet.getInt("age");
                String gender = resultSet.getString("gender");

                System.out.printf("|%-12d|%-20s|%-10d|%-12s|\n", id, name, age, gender);
            }

            System.out.println("+------------+--------------------+------------+-------------+");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean getPatientId(int id) {
        String query = "SELECT * FROM patients WHERE id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            return resultSet.next(); // Returns true if ID exists
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
