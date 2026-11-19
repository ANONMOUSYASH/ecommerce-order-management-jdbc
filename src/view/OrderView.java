package view;

import model.Order;

import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class OrderView {
    private final Scanner scanner = new Scanner(System.in);

    public int showMenu() {
        System.out.println("\n=== Order Management System ===");
        System.out.println("1. Insert Orders (at least 3)");
        System.out.println("2. Display Orders by Status (Quantity > 2, Sorted by Unit Price DESC)");
        System.out.println("3. Increase Order Quantity");
        System.out.println("4. Update Order Status");
        System.out.println("5. Display Total Order Value & Delete Cancelled Order");
        System.out.println("6. Exit");
        return readInt("Enter your choice: ");
    }

    public int showInsertMenu() {
        System.out.println("\n1. Insert Sample Orders");
        System.out.println("2. Insert Orders Manually (at least 3)");
        return readInt("Choose insert option: ");
    }

    public int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid integer. Please try again.");
            }
        }
    }

    public double readDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Please try again.");
            }
        }
    }

    public String readString(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            }
            System.out.println("Input cannot be empty. Please try again.");
        }
    }

    public Order readOrder() {
        int orderId = readInt("Order ID: ");
        String customerName = readString("Customer Name: ");
        String productName = readString("Product Name: ");
        int quantity = readInt("Quantity: ");
        double unitPrice = readDouble("Unit Price: ");
        String status = readString("Status (Pending/Shipped/Delivered/Cancelled): ");
        return new Order(orderId, customerName, productName, quantity, unitPrice, status);
    }

    public void displayOrder(Order order) {
        displayOrders(Collections.singletonList(order));
    }

    public void displayOrders(List<Order> orders) {
        System.out.printf("%-10s %-20s %-20s %-10s %-12s %-15s%n",
                "Order ID", "Customer Name", "Product Name", "Quantity", "Unit Price", "Status");
        System.out.println("--------------------------------------------------------------------------------");
        for (Order o : orders) {
            System.out.printf("%-10d %-20s %-20s %-10d %-12.2f %-15s%n",
                    o.getOrderId(),
                    o.getCustomerName(),
                    o.getProductName(),
                    o.getQuantity(),
                    o.getUnitPrice(),
                    o.getStatus());
        }
    }

    public void showMessage(String message) {
        System.out.println(message);
    }

    public void close() {
        scanner.close();
    }
}
