package com.app.hotel.hotelmgmtfx.utils;

import com.app.hotel.hotelmgmtfx.db.DatabaseConnection;
import com.app.hotel.hotelmgmtfx.model.FinalOrder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.app.hotel.hotelmgmtfx.model.MenuItem;
import com.app.hotel.hotelmgmtfx.model.Order;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class FinalOrderHandler {

    public static void addOrder(FinalOrder order) throws JsonProcessingException {
        String query = "INSERT INTO final_order (table_id, order_date, total_cost, items) VALUES (?, ?, ?, ?)";

        ObjectMapper objectMapper = new ObjectMapper(); // For JSON serialization
        String itemsJson = objectMapper.writeValueAsString(order.getItems());

        try (Connection connection = DatabaseConnection.getConnection(); // Assume DatabaseConnection class handles DB connections
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, order.getTableId());
            statement.setString(2, order.getOrderDate());
            statement.setDouble(3, order.getTotalCost());
            statement.setString(4, itemsJson);

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<FinalOrder> getOrdersForDate(String date) {
        List<FinalOrder> orders = new ArrayList<>();
        String query = "SELECT * FROM final_order WHERE order_date = ?";

        try (Connection connection = DatabaseConnection.getConnection(); // Assume DatabaseConnection class handles DB connections
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, date);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Long id = resultSet.getLong("id");
                    int tableId = resultSet.getInt("table_id");
                    String orderDate = resultSet.getString("order_date");
                    double totalCost = resultSet.getDouble("total_cost");


                    FinalOrder order = new FinalOrder(id,tableId, orderDate, totalCost, null);
                    orders.add(order);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }
}
