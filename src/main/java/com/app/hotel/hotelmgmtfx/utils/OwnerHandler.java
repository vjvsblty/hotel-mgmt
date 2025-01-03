package com.app.hotel.hotelmgmtfx.utils;

import com.app.hotel.hotelmgmtfx.db.DatabaseConnection;
import com.app.hotel.hotelmgmtfx.model.HotelTable;
import com.app.hotel.hotelmgmtfx.model.MenuItem;
import com.app.hotel.hotelmgmtfx.model.Owner;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OwnerHandler {

    public static List<Owner> fetchOwnerListFromDb() {
        List<Owner> owners = new ArrayList<>();

        String query = "SELECT id, name FROM owner";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet resultSet = stmt.executeQuery(query)) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");

                owners.add(new Owner(id, name));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return owners;
    }

    public static String fetchOwnerNameById(int id) {
        String ownerName = null;
        String query = "SELECT name FROM owner WHERE id = ?";  // Use a parameterized query

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);  // Set the owner id parameter

            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    ownerName = resultSet.getString("name"); // Get the name of the owner
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ownerName; // Return the owner's name or null if not found
    }

}
