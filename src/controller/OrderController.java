package controller;

import model.Order;
import model.OrderDAO;
import view.OrderView;

import java.sql.SQLException;
import java.util.List;

public class OrderController {
    private static final String[] VALID_STATUSES = {"Pending", "Shipped", "Delivered", "Cancelled"};

    private final OrderDAO dao;
    private final OrderView view;

    public OrderController(OrderDAO dao, OrderView view) {
        this.dao = dao;
        this.view = view;
    }

    public void start() {
        boolean running = true;
        while (running) {
            int choice = view.showMenu();
            try {
                switch (choice) {
                    case 1:
                        handleInsertOrders();
                        break;
                    case 2:
                        handleFilterByStatus();
                        break;
                    case 3:
                        handleIncreaseQuantity();
                        break;
                    case 4:
                        handleUpdateStatus();
                        break;
                    case 5:
                        handleTotalValueAndDelete();
                        break;
                    case 6:
                        running = false;
                        view.showMessage("Exiting Order Management System.");
                        break;
                    default:
                        view.showMessage("Invalid choice. Please select 1-6.");
                }
            } catch (SQLException e) {
                view.showMessage("Database error: " + e.getMessage());
            }
        }
        view.close();
    }

    private void handleInsertOrders() throws SQLException {
        int count = view.readInt("How many orders to insert (minimum 3): ");
        if (count < 3) {
            view.showMessage("Please insert at least 3 order records.");
            return;
        }

        for (int i = 1; i <= count; i++) {
            view.showMessage("\n--- Enter details for Order " + i + " ---");
            Order order = view.readOrder();
            if (dao.insertOrder(order)) {
                view.showMessage("Order ID " + order.getOrderId() + " inserted successfully.");
            } else {
                view.showMessage("Duplicate Order ID " + order.getOrderId() + "! Record skipped.");
            }
        }
    }

    private void handleFilterByStatus() throws SQLException {
        String status = view.readString("Enter order status to filter: ");
        List<Order> orders = dao.getOrdersByStatusAndMinQty(status, 2);
        if (orders.isEmpty()) {
            view.showMessage("No orders found with status '" + status + "' and quantity > 2.");
        } else {
            view.displayOrders(orders);
        }
    }

    private void handleIncreaseQuantity() throws SQLException {
        int orderId = view.readInt("Enter Order ID: ");
        int additionalQty = view.readInt("Enter additional quantity: ");
        if (additionalQty <= 0) {
            view.showMessage("Additional quantity must be greater than 0.");
            return;
        }

        if (dao.increaseQuantity(orderId, additionalQty)) {
            view.showMessage("Quantity updated successfully for Order ID " + orderId + ".");
        } else {
            view.showMessage("Order ID " + orderId + " does not exist.");
        }
    }

    private void handleUpdateStatus() throws SQLException {
        int orderId = view.readInt("Enter Order ID: ");
        String newStatus = view.readString("Enter new status (Pending, Shipped, Delivered, Cancelled): ");

        Order existing = dao.getOrderById(orderId);
        if (existing == null) {
            view.showMessage("Order ID " + orderId + " does not exist.");
            return;
        }

        view.showMessage("Current Order Details:");
        view.displayOrder(existing);

        String normalized = normalizeStatus(newStatus);
        if (normalized == null) {
            view.showMessage("Invalid status. Allowed values: Pending, Shipped, Delivered, Cancelled.");
            return;
        }

        if (dao.updateOrderStatus(orderId, normalized)) {
            Order updated = dao.getOrderById(orderId);
            view.showMessage("Updated Order Details:");
            view.displayOrder(updated);
        } else {
            view.showMessage("Failed to update status for Order ID " + orderId + ".");
        }
    }

    private void handleTotalValueAndDelete() throws SQLException {
        double total = dao.getTotalOrderValue();
        view.showMessage(String.format("Total Order Value: %.2f", total));

        int orderId = view.readInt("Enter Order ID to delete (only if Cancelled): ");
        Order existing = dao.getOrderById(orderId);
        if (existing == null) {
            view.showMessage("Cannot delete: Order ID " + orderId + " does not exist.");
            return;
        }

        if (!"Cancelled".equalsIgnoreCase(existing.getStatus())) {
            view.showMessage("Cannot delete: Order ID " + orderId + " has status '" + existing.getStatus()
                    + "'. Only 'Cancelled' orders can be deleted.");
            return;
        }

        if (dao.deleteCancelledOrder(orderId)) {
            view.showMessage("Order ID " + orderId + " deleted successfully.");
        } else {
            view.showMessage("Order ID " + orderId + " could not be deleted.");
        }
    }

    private String normalizeStatus(String input) {
        for (String valid : VALID_STATUSES) {
            if (valid.equalsIgnoreCase(input)) {
                return valid;
            }
        }
        return null;
    }
}
