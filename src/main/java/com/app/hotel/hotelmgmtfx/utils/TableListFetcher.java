package com.app.hotel.hotelmgmtfx.utils;

import com.app.hotel.hotelmgmtfx.db.DatabaseConnection;
import com.app.hotel.hotelmgmtfx.model.HotelTable;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TableListFetcher {

    public static List<HotelTable> fetchTablesFromDatabase() {
        List<HotelTable> tables = new ArrayList<>();

        String query = "SELECT id, table_name, table_size FROM hotel_table";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet resultSet = stmt.executeQuery(query)) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String tableName = resultSet.getString("table_name");
                int tableSize = resultSet.getInt("table_size");

                tables.add(new HotelTable(id, tableName, tableSize));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return tables;
    }

    public static boolean addTableToDatabase(String tableName, String tableCapacity) {

        String query = "INSERT INTO hotel_table (table_name, table_size) VALUES (?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement statement = conn.prepareStatement(query)) {

            statement.setString(1, tableName);
            statement.setInt(2, Integer.parseInt(tableCapacity));
            statement.executeUpdate();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public static void updateTableInDatabase(HotelTable table) throws SQLException {
        String query = "UPDATE hotel_table SET table_name = ?, table_size = ? WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, table.getTableName());
            statement.setInt(2, table.getTableSize());
            statement.setInt(3, table.getId());
            statement.executeUpdate();
        }
    }
}
