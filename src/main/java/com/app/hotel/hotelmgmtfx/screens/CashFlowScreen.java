package com.app.hotel.hotelmgmtfx.screens;

import com.app.hotel.hotelmgmtfx.model.Expense;
import com.app.hotel.hotelmgmtfx.model.ExpenseData;
import com.app.hotel.hotelmgmtfx.model.Owner;
import com.app.hotel.hotelmgmtfx.utils.ExpenseHandler;
import com.app.hotel.hotelmgmtfx.utils.OwnerHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CashFlowScreen {
    // Declare expenseTable as a class-level variable
    private TableView<Expense> expenseTable;

    public void loadScreen(StackPane contentPane) {
        contentPane.getChildren().clear();

        // Main layout container
        VBox layout = new VBox(20);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.TOP_CENTER);

        // Set up form layout for adding an expense
        VBox addExpenseForm = new VBox(20);
        addExpenseForm.setPadding(new Insets(20));
        addExpenseForm.setStyle("-fx-background-color: #f9f9f9; -fx-border-radius: 10; -fx-border-color: #dcdcdc;");
        addExpenseForm.setAlignment(Pos.CENTER);

        // Common field width and style
        double fieldWidth = 200;
        String commonStyle = "-fx-font-size: 14px; -fx-padding: 5px; -fx-border-color: #dcdcdc; -fx-border-radius: 5; -fx-background-radius: 5;";

        // Title for "Add Expense" form
        Text formTitle = new Text("Add Expense");
        formTitle.setFont(Font.font("Arial", 18));
        formTitle.setStyle("-fx-font-weight: bold;");

        // Row 1: Expense Name and Expense Cost
        TextField expenseNameField = new TextField();
        expenseNameField.setPromptText("Expense Name");
        expenseNameField.setPrefWidth(fieldWidth);
        expenseNameField.setStyle(commonStyle);

        TextField expenseCostField = new TextField();
        expenseCostField.setPromptText("Expense Cost");
        expenseCostField.setPrefWidth(fieldWidth);
        expenseCostField.setStyle(commonStyle);

        HBox row1 = new HBox(10, expenseNameField, expenseCostField);
        row1.setAlignment(Pos.CENTER);

        // Row 2: Date and Done By
        DatePicker expenseDatePicker = new DatePicker(LocalDate.now());
        expenseDatePicker.setPrefWidth(fieldWidth);
        expenseDatePicker.setStyle(commonStyle);

        ComboBox<String> ownerComboBox = new ComboBox<>();
        ownerComboBox.setPromptText("Select Done By");
        ownerComboBox.getItems().addAll(fetchOwnerNames());
        ownerComboBox.setPrefWidth(fieldWidth);
        ownerComboBox.setStyle(commonStyle);

        HBox row2 = new HBox(10, expenseDatePicker, ownerComboBox);
        row2.setAlignment(Pos.CENTER);

        // Save Button
        Button saveExpenseButton = new Button("Save Expense");
        saveExpenseButton.setPrefWidth(300);
        saveExpenseButton.setStyle("-fx-font-size: 14px; -fx-padding: 10px; -fx-background-color: #4CAF50; -fx-text-fill: white; -fx-border-radius: 5; -fx-background-radius: 5;");

        // Add components to form
        addExpenseForm.getChildren().addAll(
                formTitle,
                row1,
                row2,
                saveExpenseButton
        );

        // Action for Save Button
        saveExpenseButton.setOnAction(e -> {
            String expenseName = expenseNameField.getText();
            String costText = expenseCostField.getText();
            String doneBy = ownerComboBox.getValue();
            LocalDate expenseDate = expenseDatePicker.getValue();

            if (validateExpenseInput(expenseName, costText, doneBy)) {
                try {
                    double cost = Double.parseDouble(costText);
                    int ownerId = fetchOwnerId(doneBy);
                    ExpenseHandler.saveExpenseToDatabase(expenseName, cost, ownerId, expenseDate.toString());

                    // Clear form fields
                    clearForm(expenseNameField, expenseCostField, ownerComboBox);

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else {
            }
        });

        // Add form to the main layout
        layout.getChildren().add(addExpenseForm);

        // Title for "View Expense" section
        Text viewExpenseTitle = new Text("View Expenses");
        viewExpenseTitle.setFont(Font.font("Arial", 18));
        viewExpenseTitle.setStyle("-fx-font-weight: bold;");

        // Create a bordered VBox for View Expenses
        VBox viewExpensesBox = new VBox(10);
        viewExpensesBox.setPadding(new Insets(10));
        viewExpensesBox.setStyle("-fx-background-color: #f9f9f9; -fx-border-radius: 10; -fx-border-color: #dcdcdc; -fx-border-width: 2;");
        viewExpensesBox.setAlignment(Pos.TOP_CENTER);

        // Add title and the DatePicker/ Search button inside the View Expenses box
        viewExpensesBox.getChildren().add(viewExpenseTitle);

        // Create HBox for From Date, To Date, and Search Button in the same row
        HBox dateSearchRow = new HBox(10); // Horizontal layout with spacing of 10px
        dateSearchRow.setAlignment(Pos.CENTER); // Align elements to the center

        // Add DatePicker for From Date to select a start date
        DatePicker fromDatePicker = new DatePicker(LocalDate.now());
        fromDatePicker.setPrefWidth(200);
        fromDatePicker.setStyle("-fx-font-size: 14px; -fx-padding: 5px;");

        // Add DatePicker for To Date to select an end date
        DatePicker toDatePicker = new DatePicker(LocalDate.now());
        toDatePicker.setPrefWidth(200);
        toDatePicker.setStyle("-fx-font-size: 14px; -fx-padding: 5px;");

        // Create Search Button
        Button searchButton = new Button("Search");
        searchButton.setStyle("-fx-font-size: 14px; -fx-padding: 10px; -fx-background-color: #4CAF50; -fx-text-fill: white; -fx-border-radius: 5;");
        searchButton.setPrefWidth(100);

        // Search button action: Trigger the update of the expense list based on selected date range
        searchButton.setOnAction(e -> updateExpenseListBetweenDates(fromDatePicker.getValue(), toDatePicker.getValue()));

        // Add DatePickers and Search Button to the HBox
        dateSearchRow.getChildren().addAll(fromDatePicker, toDatePicker, searchButton);

        // Add the HBox to the View Expenses box
        viewExpensesBox.getChildren().add(dateSearchRow);

        // TableView for displaying the filtered expenses
        expenseTable = new TableView<>();
        expenseTable.setPrefWidth(700);
        expenseTable.setStyle("-fx-background-color: #f9f9f9; -fx-border-radius: 10; -fx-border-color: #dcdcdc;");

        // Create Index Column for row number
        TableColumn<Expense, String> indexColumn = new TableColumn<>("#");
        indexColumn.setCellValueFactory(param -> {
            int index = expenseTable.getItems().indexOf(param.getValue()) + 1; // +1 for 1-based index
            return new SimpleStringProperty(String.valueOf(index));
        });
        indexColumn.setPrefWidth(50); // Set width for the index column

        // Create columns for the table
        TableColumn<Expense, String> dateColumn = new TableColumn<>("Date");
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        dateColumn.setPrefWidth(200);

        TableColumn<Expense, String> nameColumn = new TableColumn<>("Expense Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.setPrefWidth(400);

        TableColumn<Expense, Double> costColumn = new TableColumn<>("Cost");
        costColumn.setCellValueFactory(new PropertyValueFactory<>("cost"));

        TableColumn<Expense, String> ownerColumn = new TableColumn<>("Owner Name");
        ownerColumn.setCellValueFactory(expense -> {
            return new SimpleStringProperty(expense.getValue().getOwnerName());
        });
        nameColumn.setPrefWidth(300);

        expenseTable.getColumns().addAll(indexColumn, dateColumn, nameColumn, costColumn, ownerColumn);

        // Add the TableView to the View Expenses box
        viewExpensesBox.getChildren().add(expenseTable);

        // Add the View Expenses box to the main layout
        layout.getChildren().add(viewExpensesBox);

        // Set the layout to the contentPane
        contentPane.getChildren().add(layout);
    }

    private void updateExpenseListBetweenDates(LocalDate fromDate, LocalDate toDate) {
        // Here you would fetch the expenses from the database between the given dates
        // Assume we fetch the list of expenses from the database for the selected date range
        List<Expense> filteredExpenses = ExpenseHandler.getExpensesByDateRange(fromDate.toString(), toDate.toString());

        // Update the table with filtered expenses
        expenseTable.getItems().setAll(filteredExpenses);
    }


    private void updateExpenseList(LocalDate selectedDate) {
        List<Expense> expenses = ExpenseHandler.getExpensesForDate(selectedDate.toString());

        // Assume that the expenseTable is already created as a class variable
        ObservableList<Expense> expenseData = FXCollections.observableArrayList(expenses);
        expenseTable.setItems(expenseData);
    }


    private List<String> fetchOwnerNames() {
        List<Owner> owners = OwnerHandler.fetchOwnerListFromDb();
        List<String> ownerNames = new ArrayList<>();
        for (Owner owner : owners) {
            ownerNames.add(owner.getName());
        }
        return ownerNames;
    }

    private int fetchOwnerId(String ownerName) {
        List<Owner> owners = OwnerHandler.fetchOwnerListFromDb();
        return owners.stream()
                .filter(owner -> owner.getName().equals(ownerName))
                .map(Owner::getId)
                .findFirst()
                .orElse(-1); // Return -1 if not found
    }

    private boolean validateExpenseInput(String name, String cost, String doneBy) {
        try {
            if (name == null || name.isEmpty() || doneBy == null || doneBy.isEmpty()) return false;
            Double.parseDouble(cost);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void clearForm(TextField nameField, TextField costField, ComboBox<String> ownerComboBox) {
        nameField.clear();
        costField.clear();
        ownerComboBox.setValue(null);
    }



}
