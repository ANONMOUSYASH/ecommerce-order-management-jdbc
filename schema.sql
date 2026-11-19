CREATE DATABASE IF NOT EXISTS ecommerce_db;
USE ecommerce_db;

CREATE TABLE IF NOT EXISTS Orders (
    order_id INT PRIMARY KEY,
    customer_name VARCHAR(100),
    product_name VARCHAR(100),
    quantity INT,
    unit_price DOUBLE,
    status VARCHAR(30)
);

INSERT INTO Orders (order_id, customer_name, product_name, quantity, unit_price, status) VALUES
(101, 'Rahul Sharma', 'Laptop', 3, 55000.00, 'Pending'),
(102, 'Priya Verma', 'Smartphone', 5, 25000.00, 'Pending'),
(103, 'Amit Kumar', 'Mouse', 2, 800.00, 'Shipped'),
(104, 'Neha Gupta', 'Keyboard', 4, 1500.00, 'Cancelled');
