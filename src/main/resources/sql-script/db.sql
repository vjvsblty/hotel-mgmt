CREATE DATABASE hotel_management;

USE hotel_management;

CREATE TABLE menu_items (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    price DECIMAL(10, 2) NOT NULL
);

CREATE TABLE hotel_table (
    id INT AUTO_INCREMENT PRIMARY KEY,
    table_name VARCHAR(255) NOT NULL,
    table_size DECIMAL(10, 2) NOT NULL
);

CREATE TABLE orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    table_id DECIMAL(10, 2) NOT NULL,
    menu_item_id DECIMAL(10, 2) NOT NULL
    quantity DECIMAL(10, 2) NOT NULL
);
