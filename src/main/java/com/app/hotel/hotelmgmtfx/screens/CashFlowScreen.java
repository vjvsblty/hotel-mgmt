package com.app.hotel.hotelmgmtfx.screens;

import com.app.hotel.hotelmgmtfx.model.FinalOrder;
import com.app.hotel.hotelmgmtfx.model.HotelTable;
import com.app.hotel.hotelmgmtfx.utils.FinalOrderHandler;
import com.app.hotel.hotelmgmtfx.utils.TableListFetcher;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.control.ScrollPane;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CashFlowScreen {

    public void loadScreen(StackPane contentPane) {
        contentPane.getChildren().clear();

        // Create layout for CashFlow screen
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));

        // Create HBox for DatePicker and Fetch Button to place them side by side
        HBox datePickerContainer = new HBox(10); // 10px spacing between DatePicker and button
        datePickerContainer.setAlignment(Pos.CENTER); // Align to center
        datePickerContainer.setPadding(new Insets(20));

        // Date Picker for selecting date
        DatePicker datePicker = new DatePicker();
        datePicker.setValue(LocalDate.now());  // Set default to today's date
        datePicker.setStyle("-fx-font-size: 16px; -fx-padding: 5px;");

        // Button to trigger data fetching based on selected date
        Button fetchDataButton = new Button("Fetch Data");
        fetchDataButton.setStyle("-fx-font-size: 16px; -fx-padding: 10px;");

        // Add DatePicker and Button to HBox
        datePickerContainer.getChildren().addAll(datePicker, fetchDataButton);

        // Create a container to hold the cards
        VBox cardsContainer = new VBox(10); // Vertical layout for cards
        cardsContainer.setAlignment(Pos.CENTER);

        // Add the cards container to a ScrollPane
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(cardsContainer); // Set the cards container inside the scroll pane
        scrollPane.setFitToWidth(true); // Ensure the cards take full width
        scrollPane.setFitToHeight(true); // Ensure the scroll pane expands to fit available height
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS); // Show vertical scrollbar

        // Action when the button is clicked
        fetchDataButton.setOnAction(e -> {
            LocalDate selectedDate = datePicker.getValue();
            List<TableData> tableDataList = fetchDataForDate(selectedDate);
            cardsContainer.getChildren().clear();  // Clear previous cards
            for (TableData tableData : tableDataList) {
                cardsContainer.getChildren().add(createCard(tableData));
            }
        });

        // Add all elements to the layout
        layout.getChildren().addAll(datePickerContainer, scrollPane);

        // Set the layout to the contentPane
        contentPane.getChildren().add(layout);
    }

    // Fetch data for the selected date from the database
    private List<TableData> fetchDataForDate(LocalDate selectedDate) {
        String formattedDate = selectedDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        List<TableData> tableDataList = new ArrayList<>();
        List<FinalOrder> orders = FinalOrderHandler.getOrdersForDate(formattedDate);
        List<HotelTable> tables = TableListFetcher.fetchTablesFromDatabase();
        double totalFinalEarning = 0.0;

        for (HotelTable table : tables) {
            int tableId = table.getId();
            String tableName = table.getTableName();
            double totalCostForTable = 0.0;

            // Iterate over all orders for the specific date
            for (FinalOrder order : orders) {
                // If the order's table_id matches the current table's table_id, add the order's total_cost
                if (order.getTableId() == tableId) {
                    totalCostForTable += order.getTotalCost();
                }
            }
            totalFinalEarning += totalCostForTable;
            tableDataList.add(new TableData(tableName, totalCostForTable, formattedDate));
        }
        tableDataList.add(new TableData("TOTAL EARNING", totalFinalEarning, formattedDate));

        return tableDataList;
    }

    // Helper class to hold table data
    public static class TableData {
        private String table;
        private Double totalCost;
        private String date;

        public TableData(String table, Double totalCost, String date) {
            this.table = table;
            this.totalCost = totalCost;
            this.date = date;
        }

        public String getTable() {
            return table;
        }

        public Double getTotalCost() {
            return totalCost;
        }

        public String getDate() {
            return date;
        }
    }

    // Create a card for each table
    private Region createCard(TableData tableData) {
        // Create a VBox for each card
        VBox card = new VBox(10);
        card.setPadding(new Insets(15));
        card.setStyle("-fx-background-color: #ecf0f1; -fx-border-radius: 10; -fx-border-color: #bdc3c7;");
        card.setPrefWidth(300);

        // Create a Text element for the table name (Heading)
        Text tableName = new Text(tableData.getTable());
        tableName.setFont(Font.font("Arial", 18));
        tableName.setStyle("-fx-font-weight: bold;");

        // Create a Text element for the total cost (Earnings)
        Text totalCost = new Text("Total Earnings: ₹" + tableData.getTotalCost());
        totalCost.setFont(Font.font("Arial", 14));

        // Create a Text element for the date
        Text dateText = new Text("Date: " + tableData.getDate());
        dateText.setFont(Font.font("Arial", 12));
        dateText.setFill(Color.GRAY);

        // Add the elements to the card
        card.getChildren().addAll(tableName, totalCost, dateText);

        return card;
    }
}
