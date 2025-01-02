package com.app.hotel.hotelmgmtfx.utils;

import com.app.hotel.hotelmgmtfx.db.DatabaseConnection;
import com.app.hotel.hotelmgmtfx.model.MenuItem;
import com.app.hotel.hotelmgmtfx.model.Order;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderHandler {
    public static void addOrder(Order order) {
        String query = "INSERT INTO orders (table_id, menu_item_id, quantity) VALUES (?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection(); // Assume DatabaseConnection class handles DB connections
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, order.getTableId());
            statement.setInt(2, order.getMenuItemId());
            statement.setInt(3, order.getQuantity());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteOrdersByTableId(int tableId) {
        String query = "DELETE FROM orders WHERE table_id = ?";

        try (Connection connection = DatabaseConnection.getConnection(); // Assume DatabaseConnection class handles DB connections
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, tableId);  // Set the tableId parameter for the DELETE query

            int rowsAffected = statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();  // Handle SQL exceptions
        }
    }

    public static void deleteOrderById(int table_id, int menu_item_id) {
        // Modify the query to delete using both table_id and menu_item_id
        String query = "DELETE FROM orders WHERE table_id = ? AND menu_item_id = ?";

        try (Connection connection = DatabaseConnection.getConnection(); // Assume DatabaseConnection class handles DB connections
             PreparedStatement statement = connection.prepareStatement(query)) {

            // Set the parameters for the DELETE query
            statement.setInt(1, table_id);  // Set the table_id parameter
            statement.setInt(2, menu_item_id);  // Set the menu_item_id parameter

            // Execute the query
            int rowsAffected = statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();  // Handle SQL exceptions
        }
    }



    public static void updateOrder(Order order) {
        String query = "UPDATE orders SET quantity = ? WHERE table_id = ? AND menu_item_id = ?";

        try (Connection connection = DatabaseConnection.getConnection(); // Assuming DatabaseConnection class handles DB connections
             PreparedStatement statement = connection.prepareStatement(query)) {

            // Set the updated quantity (new quantity is already updated in the Order object)
            statement.setInt(1, order.getQuantity());
            statement.setInt(2, order.getTableId());
            statement.setInt(3, order.getMenuItemId());

            // Execute the update
            int rowsUpdated = statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static List<Order> fetchOrdersForTable(int tableId) {
        List<Order> orders = new ArrayList<>();
        String query = "SELECT o.id, o.table_id, o.menu_item_id, o.quantity, m.name, m.price " +
                "FROM orders o " +
                "JOIN menu_items m ON o.menu_item_id = m.id " +
                "WHERE o.table_id = ?";

        try (Connection connection = DatabaseConnection.getConnection(); // Assume DatabaseConnection class handles DB connections
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, tableId); // Set table ID parameter

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int menuItemId = resultSet.getInt("menu_item_id");
                    String menuItemName = resultSet.getString("name");
                    double menuItemPrice = resultSet.getDouble("price");
                    int quantity = resultSet.getInt("quantity");

                    // Create a MenuItem object to store the name and price
                    MenuItem menuItem = new MenuItem(menuItemId, menuItemName, menuItemPrice);

                    // Create an Order object
                    Order order = new Order(Long.valueOf(1), tableId, menuItemId, quantity);
                    orders.add(order);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orders;
    }
}
