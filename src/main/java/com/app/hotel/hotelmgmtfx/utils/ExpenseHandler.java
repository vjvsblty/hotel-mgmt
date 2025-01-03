package com.app.hotel.hotelmgmtfx.utils;

import com.app.hotel.hotelmgmtfx.db.DatabaseConnection;
import com.app.hotel.hotelmgmtfx.model.Expense;
import com.app.hotel.hotelmgmtfx.model.FinalOrder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ExpenseHandler {
    public static List<Expense> getExpensesForDate(String formattedDate) {
        List<Expense> expenses = new ArrayList<>();
        String query = "SELECT * FROM expense WHERE date = ?";

        try (Connection connection = DatabaseConnection.getConnection(); // Assume DatabaseConnection class handles DB connections
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, formattedDate);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Long id = resultSet.getLong("id");
                    String name = resultSet.getString("name");
                    double cost = resultSet.getDouble("cost");
                    int ownerId = resultSet.getInt("owner_id");
                    String date = resultSet.getString("date");


                    Expense expense = new Expense(id,name, cost, ownerId, date);
                    expenses.add(expense);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return expenses;
    }

    public static void saveExpenseToDatabase(String expenseName,
                                             double cost,
                                             int ownerId,
                                             String date) throws JsonProcessingException {
        String query = "INSERT INTO expense (name, cost, owner_id, date) VALUES (?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection(); // Assume DatabaseConnection class handles DB connections
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, expenseName);
            statement.setDouble(2, cost);
            statement.setInt(3, ownerId);
            statement.setString(4, date);

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Expense> fetchExpensesSortedByDate() {
        List<Expense> expenses = new ArrayList<>();
        String query = "SELECT id, name, cost, owner_id, date FROM expense ORDER BY date";

        try (Connection connection = DatabaseConnection.getConnection(); // Assume DatabaseConnection handles DB connections
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String name = resultSet.getString("name");
                double cost = resultSet.getDouble("cost");
                int ownerId = resultSet.getInt("owner_id");
                String date = resultSet.getString("date");

                // Create an Expense object for each row in the result set
                Expense expense = new Expense(id,name, cost, ownerId, date);
                expenses.add(expense);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return expenses;
    }

    public static List<Expense> getExpensesByDateRange(String fromDate, String toDate) {
        List<Expense> expenses = new ArrayList<>();

        // SQL query to fetch expenses between the provided date range
        String query = "SELECT id, name, cost, owner_id, date FROM expense WHERE date BETWEEN ? AND ? ORDER BY date";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            // Set the date range parameters in the query
            statement.setString(1, fromDate);  // Set 'fromDate'
            statement.setString(2, toDate);    // Set 'toDate'

            // Execute the query and retrieve the results
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Long id = resultSet.getLong("id");
                    String name = resultSet.getString("name");
                    double cost = resultSet.getDouble("cost");
                    int ownerId = resultSet.getInt("owner_id");
                    String date = resultSet.getString("date");

                    // Create an Expense object for each row in the result set
                    Expense expense = new Expense(id, name, cost, ownerId, date);
                    expenses.add(expense);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return expenses;
    }

}
