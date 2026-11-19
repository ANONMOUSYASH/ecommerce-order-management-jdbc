package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {

    public boolean insertOrder(Order order) throws SQLException {
        String sql = "INSERT INTO Orders (order_id, customer_name, product_name, quantity, unit_price, status) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, order.getOrderId());
            ps.setString(2, order.getCustomerName());
            ps.setString(3, order.getProductName());
            ps.setInt(4, order.getQuantity());
            ps.setDouble(5, order.getUnitPrice());
            ps.setString(6, order.getStatus());
            ps.executeUpdate();
            return true;
        } catch (SQLIntegrityConstraintViolationException e) {
            return false;
        } catch (SQLException e) {
            if (e.getErrorCode() == 1062 || "23000".equals(e.getSQLState())) {
                return false;
            }
            throw e;
        }
    }

    public List<Order> getOrdersByStatusAndMinQty(String status, int minQty) throws SQLException {
        String sql = "SELECT order_id, customer_name, product_name, quantity, unit_price, status "
                + "FROM Orders WHERE status = ? AND quantity > ? ORDER BY unit_price DESC";
        List<Order> orders = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, minQty);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    orders.add(mapRow(rs));
                }
            }
        }
        return orders;
    }

    public boolean increaseQuantity(int orderId, int additionalQty) throws SQLException {
        String sql = "UPDATE Orders SET quantity = quantity + ? WHERE order_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, additionalQty);
            ps.setInt(2, orderId);
            return ps.executeUpdate() > 0;
        }
    }

    public Order getOrderById(int orderId) throws SQLException {
        String sql = "SELECT order_id, customer_name, product_name, quantity, unit_price, status FROM Orders WHERE order_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        }
        return null;
    }

    public boolean updateOrderStatus(int orderId, String newStatus) throws SQLException {
        String sql = "UPDATE Orders SET status = ? WHERE order_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newStatus);
            ps.setInt(2, orderId);
            return ps.executeUpdate() > 0;
        }
    }

    public double getTotalOrderValue() throws SQLException {
        String sql = "SELECT COALESCE(SUM(quantity * unit_price), 0) FROM Orders";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getDouble(1) : 0.0;
        }
    }

    public boolean deleteCancelledOrder(int orderId) throws SQLException {
        String sql = "DELETE FROM Orders WHERE order_id = ? AND status = 'Cancelled'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            return ps.executeUpdate() > 0;
        }
    }

    private Order mapRow(ResultSet rs) throws SQLException {
        return new Order(
                rs.getInt("order_id"),
                rs.getString("customer_name"),
                rs.getString("product_name"),
                rs.getInt("quantity"),
                rs.getDouble("unit_price"),
                rs.getString("status")
        );
    }
}
