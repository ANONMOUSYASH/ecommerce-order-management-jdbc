package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnection {
    private static final String URL =
            "jdbc:mysql://localhost:3306/ecommerce_db?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String USER = System.getenv("DB_USER") != null ? System.getenv("DB_USER") : "root";
    // Set your MySQL password here or via DB_PASSWORD env variable
    private static final String PASSWORD = System.getenv("DB_PASSWORD") != null ? System.getenv("DB_PASSWORD") : "";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void initDatabase() throws SQLException {
        String createTableSql = "CREATE TABLE IF NOT EXISTS Orders ("
                + "order_id INT PRIMARY KEY, "
                + "customer_name VARCHAR(100), "
                + "product_name VARCHAR(100), "
                + "quantity INT, "
                + "unit_price DOUBLE, "
                + "status VARCHAR(30)"
                + ")";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(createTableSql);
        }
    }
}
