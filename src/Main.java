import controller.OrderController;
import model.DBConnection;
import model.OrderDAO;
import view.OrderView;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        try {
            DBConnection.initDatabase();
            System.out.println("Connected to MySQL database 'ecommerce_db' successfully.");
        } catch (SQLException e) {
            System.err.println("Database connection failed: " + e.getMessage());
            System.err.println("Verify MySQL is running and credentials in src/model/DBConnection.java (or DB_USER/DB_PASSWORD env vars) are correct.");
            return;
        }

        OrderDAO dao = new OrderDAO();
        OrderView view = new OrderView();
        OrderController controller = new OrderController(dao, view);
        controller.start();
    }
}
