package com.app.hotel.hotelmgmtfx.utils;

import com.app.hotel.hotelmgmtfx.db.DatabaseConnection;
import com.app.hotel.hotelmgmtfx.model.MenuItem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MenuItemFetcher {

    public static List<MenuItem> fetchMenuItems() {
        List<MenuItem> menuItems = new ArrayList<>();
        String query = "SELECT id,name, price FROM menu_items";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Integer id = rs.getInt("id");
                String itemName = rs.getString("name");
                double itemPrice = rs.getDouble("price");
                menuItems.add(new MenuItem(id, itemName, itemPrice));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return menuItems;
    }

    public static MenuItem fetchMenuItemById(int id) {
        String query = "SELECT id,name, price FROM menu_items where id="+id;

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Integer index = rs.getInt("id");
                String itemName = rs.getString("name");
                double itemPrice = rs.getDouble("price");
                return new MenuItem(index, itemName, itemPrice);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void deleteItemFromDatabase(MenuItem item) throws SQLException {
        String query = "DELETE FROM menu_items WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, item.getId()); // Assuming MenuItem has an 'id' field
            statement.executeUpdate();
        }
    }

}