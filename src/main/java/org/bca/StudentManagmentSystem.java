package org.bca;

import java.sql.*;
import java.util.Scanner;

public class StudentManagmentSystem {
    static final String DB_URL = "jdbc:mysql://localhost:3306/student_db";
    static final String USER = "root";
    static final String PASS = "";

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            Scanner scanner = new Scanner(System.in);

            while (true) {
                System.out.println("\nStudent Management System");
                System.out.println("1 - Display All Records");
                System.out.println("2 - Insert Student");
                System.out.println("3 - Update Student");
                System.out.println("4 - Delete Student");
                System.out.println("5 - Show Student Detail");
                System.out.println("6 - Exit");
                System.out.print("Choose an option: ");

                int choice = scanner.nextInt();
                switch (choice) {
                    case 1 -> displayAll(conn);
                    case 2 -> insertStudent(conn, scanner);
                    case 3 -> updateStudent(conn, scanner);
                    case 4 -> deleteStudent(conn, scanner);
                    case 5 -> showStudentDetail(conn, scanner);
                    case 6 -> { System.out.println("Exiting..."); return; }
                    default -> System.out.println("Invalid choice! Try again.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void displayAll(Connection conn) throws SQLException {
        String query = "SELECT * FROM students";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                System.out.printf("ID: %d, Name: %s, Age: %d\n", rs.getInt("id"), rs.getString("name"), rs.getInt("age"));
            }
        }
    }

    public static void insertStudent(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Enter Name: ");
        scanner.nextLine(); // Consume newline
        String name = scanner.nextLine();
        System.out.print("Enter Age: ");
        int age = scanner.nextInt();

        String query = "INSERT INTO students (name, age) VALUES (?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, name);
            pstmt.setInt(2, age);
            pstmt.executeUpdate();
            System.out.println("Student added successfully!");
        }
    }

    public static void updateStudent(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Enter Student ID to update: ");
        int id = scanner.nextInt();
        System.out.print("Enter New Name: ");
        scanner.nextLine(); // Consume newline
        String name = scanner.nextLine();
        System.out.print("Enter New Age: ");
        int age = scanner.nextInt();

        String query = "UPDATE students SET name = ?, age = ? WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, name);
            pstmt.setInt(2, age);
            pstmt.setInt(3, id);
            int rows = pstmt.executeUpdate();
            if (rows > 0) System.out.println("Student updated successfully!");
            else System.out.println("Student not found!");
        }
    }

    public static void deleteStudent(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Enter Student ID to delete: ");
        int id = scanner.nextInt();

        String query = "DELETE FROM students WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, id);
            int rows = pstmt.executeUpdate();
            if (rows > 0) System.out.println("Student deleted successfully!");
            else System.out.println("Student not found!");
        }
    }
    public static void showStudentDetail(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Enter Student ID to search: ");
        int id = scanner.nextInt();

        String query = "SELECT * FROM students WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    System.out.printf("ID: %d, Name: %s, Age: %d\n", rs.getInt("id"), rs.getString("name"), rs.getInt("age"));
                } else {
                    System.out.println("Student not found!");
                }
            }
        }
    }
}