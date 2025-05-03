package com.assignment.selenium;

import org.junit.jupiter.api.*;

import java.sql.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class OrderDatabaseTest {

    private static Connection connection;

    @BeforeAll
    static void setupConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/skillsharedb";
        String username = "root";
        String password = "root";

        connection = DriverManager.getConnection(url, username, password);
        Assertions.assertNotNull(connection);
    }

    @Test
    @Order(1)
    void testInsertOrder() throws SQLException {
        String query = "INSERT INTO orders (customer_name, product_name, quantity) VALUES (?, ?, ?)";
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setString(1, "Udara");
        stmt.setString(2, "Laptop");
        stmt.setInt(3, 2);

        int rowsInserted = stmt.executeUpdate();
        Assertions.assertEquals(1, rowsInserted);
    }

    @Test
    @Order(2)
    void testRetrieveOrder() throws SQLException {
        String query = "SELECT * FROM orders WHERE customer_name = ?";
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setString(1, "Udara");

        ResultSet rs = stmt.executeQuery();
        Assertions.assertTrue(rs.next());
        Assertions.assertEquals("Laptop", rs.getString("product_name"));
        Assertions.assertEquals(2, rs.getInt("quantity"));
    }

    @Test
    @Order(3)
    void testUpdateOrder() throws SQLException {
        String query = "UPDATE orders SET quantity = ? WHERE customer_name = ?";
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setInt(1, 3);
        stmt.setString(2, "Udara");

        int updated = stmt.executeUpdate();
        Assertions.assertEquals(1, updated);
    }

    @Test
    @Order(4)
    void testDeleteOrder() throws SQLException {
        String query = "DELETE FROM orders WHERE customer_name = ?";
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setString(1, "Udara");

        int deleted = stmt.executeUpdate();
        Assertions.assertEquals(1, deleted);
    }

    @AfterAll
    static void closeConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}
