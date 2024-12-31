package com.app.hotel.hotelmgmtfx.screens;

import com.app.hotel.hotelmgmtfx.model.*;
import com.app.hotel.hotelmgmtfx.model.MenuItem;
import com.app.hotel.hotelmgmtfx.utils.FinalOrderHandler;
import com.app.hotel.hotelmgmtfx.utils.MenuItemFetcher;
import com.app.hotel.hotelmgmtfx.utils.TableListFetcher;
import com.app.hotel.hotelmgmtfx.utils.OrderHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.StringConverter;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class ManageOrdersScreen {
    private ComboBox<String> tableSelector; // Make it an instance variable

    public void loadScreen(StackPane contentPane) {
        contentPane.getChildren().clear();

        VBox manageOrdersPane = new VBox(15);
        manageOrdersPane.setAlignment(Pos.TOP_CENTER);
        manageOrdersPane.setPrefHeight(contentPane.getHeight());
        manageOrdersPane.setFillWidth(true);

        // Table Selector
        tableSelector = new ComboBox<>();
        tableSelector.setPromptText("Select Table");

        // Load tables from the database
        List<HotelTable> tables = TableListFetcher.fetchTablesFromDatabase();
        if (tables.isEmpty()) {
            showErrorDialog("No tables found in the database.");
            return;
        }
        tableSelector.getItems().addAll(tables.stream().map(HotelTable::getTableName).collect(Collectors.toList()));

        // Create a GridPane for displaying tables as cards
        GridPane tableCardsGrid = new GridPane();
        tableCardsGrid.setHgap(15);
        tableCardsGrid.setVgap(15);
        tableCardsGrid.setAlignment(Pos.TOP_CENTER);

        int row = 0;
        int col = 0;
        for (HotelTable table : tables) {
            VBox tableCard = createTableCard(table, tables, contentPane);
            tableCardsGrid.add(tableCard, col, row);
            col++;
            if (col == 2) {
                col = 0;
                row++;
            }
        }

        // Wrap tableCardsGrid in a ScrollPane
        ScrollPane tableCardsScrollPane = new ScrollPane();
        tableCardsScrollPane.setContent(tableCardsGrid);
        tableCardsScrollPane.setFitToWidth(true);
        tableCardsScrollPane.prefHeightProperty().bind(contentPane.heightProperty().subtract(20));

        VBox.setVgrow(tableCardsScrollPane, Priority.ALWAYS);

        // Add components to the layout
        manageOrdersPane.getChildren().addAll(tableCardsScrollPane);
        contentPane.getChildren().add(manageOrdersPane);
    }


    private VBox createTableCard(HotelTable table, List<HotelTable> tables, StackPane contentPane) {
        VBox tableCard = new VBox(10);
        styleTableCard(tableCard);

        HBox tableHeader = createTableHeader(table);
        VBox ordersDisplay = createOrdersDisplay(table, tables);
        HBox orderForm = createOrderForm(ordersDisplay, tables, table, contentPane);

        tableCard.getChildren().addAll(tableHeader, ordersDisplay, orderForm);
        return tableCard;
    }

    private void styleTableCard(VBox tableCard) {
        tableCard.setStyle("-fx-border-color: green; -fx-padding: 10; -fx-background-color: #f0f0f0; -fx-border-radius: 10; -fx-background-radius: 10;");
        tableCard.setPrefSize(600, 300);
        tableCard.setMinHeight(300);
        tableCard.setMaxHeight(400);
    }

    private HBox createTableHeader(HotelTable table) {
        Label tableNameLabel = new Label(table.getTableName());
        tableNameLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        HBox header = new HBox(tableNameLabel);
        header.setAlignment(Pos.CENTER);
        return header;
    }

    private VBox createOrdersDisplay(HotelTable table, List<HotelTable> tables) {
        VBox ordersDisplay = new VBox(10);
        ordersDisplay.setAlignment(Pos.TOP_LEFT);
        displayOrdersForTable(table.getTableName(), ordersDisplay, tables);
        return ordersDisplay;
    }

    private HBox createOrderForm(VBox ordersDisplay, List<HotelTable> tables, HotelTable table, StackPane contentPane) {
        ComboBox<MenuItem> menuItemSelector = setupMenuItemSelector();
        TextField quantityField = new TextField();
        quantityField.setPromptText("Quantity");
        quantityField.setPrefWidth(80);

        Button addOrderButton = createAddOrderButton(menuItemSelector, quantityField, ordersDisplay, tables, table);
        Button printButton = createPrintButton(table, tables);
        Button saveOrderButton = saveOrderButton(table, tables, contentPane);

        HBox form = new HBox(10, menuItemSelector, quantityField, addOrderButton, printButton, saveOrderButton);
        form.setAlignment(Pos.CENTER);
        return form;
    }


    private ComboBox<MenuItem> setupMenuItemSelector() {
        ComboBox<MenuItem> menuItemSelector = new ComboBox<>();
        List<MenuItem> allMenuItems = MenuItemFetcher.fetchMenuItems();
        ObservableList<MenuItem> filteredItems = FXCollections.observableArrayList(allMenuItems);

        menuItemSelector.getItems().addAll(filteredItems);
        menuItemSelector.setPromptText("Select Menu Item");
        menuItemSelector.setEditable(true);

        // Set a custom StringConverter to handle displaying MenuItem names
        menuItemSelector.setConverter(new StringConverter<>() {
            @Override
            public String toString(MenuItem item) {
                return item == null ? "" : item.getName();  // Display the name of the MenuItem
            }

            @Override
            public MenuItem fromString(String string) {
                // Convert the string typed by the user into a MenuItem object
                return menuItemSelector.getItems().stream()
                        .filter(item -> item.getName().equalsIgnoreCase(string))
                        .findFirst()
                        .orElse(null);  // Return null if no match found
            }
        });

        // Add a listener to filter menu items as the user types
        menuItemSelector.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
            // Avoid triggering the filtering logic on item selection
            if (menuItemSelector.getSelectionModel().getSelectedItem() != null) {
                return;  // Skip filtering if an item is selected
            }

            // Check if the entered text is not empty
            if (newValue != null && !newValue.trim().isEmpty()) {
                // Filter menu items dynamically as the user types
                List<MenuItem> filtered = allMenuItems.stream()
                        .filter(item -> item.getName().toLowerCase().contains(newValue.toLowerCase()))  // Match anywhere in the name
                        .collect(Collectors.toList());

                // Update the ComboBox with the filtered items
                filteredItems.setAll(filtered);  // Using filteredItems ObservableList for automatic update
                if (!filtered.isEmpty()) {
                    menuItemSelector.show();  // Show the dropdown with filtered items
                }
            } else {
                // Reset to show all menu items if the input is cleared
                filteredItems.setAll(allMenuItems);  // Reset to all items
                menuItemSelector.hide();  // Hide the dropdown when the text field is empty
            }
        });

        // Add a listener to avoid re-triggering filtering on selection
        menuItemSelector.getSelectionModel().selectedItemProperty().addListener((observable, oldItem, newItem) -> {
            if (newItem != null) {
                // When an item is selected, stop filtering logic to prevent stack overflow

                // Optionally, you may choose to hide the dropdown after selection
                menuItemSelector.hide();
            }
        });

        return menuItemSelector;
    }



    private Button createAddOrderButton(ComboBox<MenuItem> menuItemSelector, TextField quantityField, VBox ordersDisplay, List<HotelTable> tables, HotelTable table) {
        Button addOrderButton = new Button("Add");
        addOrderButton.setStyle("-fx-background-color: orange; -fx-text-fill: white;");
        addOrderButton.setOnAction(e -> handleAddOrder(menuItemSelector, quantityField, ordersDisplay, tables, table));
        return addOrderButton;
    }

    private Button createPrintButton(HotelTable table, List<HotelTable> tables) {
        Button printButton = new Button("Print");
        printButton.setStyle("-fx-background-color: green; -fx-text-fill: white;");
        printButton.setOnAction(e -> printReceipt(table.getTableName(), tables));
        return printButton;
    }

    private Button saveOrderButton(HotelTable table, List<HotelTable> tables, StackPane contentPane) {
        Button saveOrderButton = new Button("Save");
        saveOrderButton.setStyle("-fx-background-color: blue; -fx-text-fill: white;");

        saveOrderButton.setOnAction(e -> {
            try {
                // Fetch orders for the table
                List<Order> orders = OrderHandler.fetchOrdersForTable(table.getId());

                if (orders.isEmpty()) {
                    showErrorDialog("No orders to save for this table.");
                    return;
                }

                // Calculate total cost and map orders
                double totalCost = 0.0;

                // Final Order Object
                List<MenuItemWithQuantity> finalItems = new ArrayList<>();

                for (Order order : orders) {
                    // Fetch menu item details
                    MenuItem menuItem = MenuItemFetcher.fetchMenuItemById(order.getMenuItemId());

                    if (menuItem != null) {
                        int quantity = order.getQuantity();
                        double cost = menuItem.getPrice() * quantity;
                        totalCost += cost;

                        // Add item to final order
                        finalItems.add(new MenuItemWithQuantity(menuItem.getId(), menuItem.getName(), quantity));
                    }
                }

                // Create Final Order Object
                FinalOrder finalOrder = new FinalOrder(
                        0, // Auto-generate Order ID (if applicable)
                        table.getId(),
                        getTodayDate(), // Current Date
                        totalCost,
                        finalItems
                );

                // Save the final order
                FinalOrderHandler.addOrder(finalOrder);
                OrderHandler.deleteOrdersByTableId(finalOrder.getTableId());
                // Reset the table's UI after saving the order
                loadScreen(contentPane);
                showSuccessDialog("Order saved successfully with total cost: " + String.format("%.2f", totalCost) + " ₹");


            } catch (Exception ex) {
                showErrorDialog("Error saving order: " + ex.getMessage());
            }
        });

        return saveOrderButton;
    }


    private void clearOrdersForTable(HotelTable table, List<HotelTable> tables) {
        // Find the VBox that displays orders for the table
        VBox ordersDisplay = getOrdersDisplayForTable(table, tables);

        // Clear the orders display
        if (ordersDisplay != null) {
            ordersDisplay.getChildren().clear();  // Clear any existing orders
            Label emptyLabel = new Label("No orders for " + table.getTableName());
            ordersDisplay.getChildren().add(emptyLabel);  // Show a message saying no orders
        }
    }

    private VBox getOrdersDisplayForTable(HotelTable table, List<HotelTable> tables) {
        // Find the table card that corresponds to the given table and return the orders display VBox
        return tables.stream()
                .filter(t -> t.getTableName().equals(table.getTableName()))
                .findFirst()
                .map(t -> {
                    // Find the VBox of orders associated with this table
                    VBox ordersDisplay = new VBox(10);
                    ordersDisplay.setAlignment(Pos.TOP_LEFT);
                    displayOrdersForTable(table.getTableName(), ordersDisplay, tables);
                    return ordersDisplay;
                })
                .orElse(null); // Return null if no matching table found
    }


    private void handleAddOrder(ComboBox<MenuItem> menuItemSelector, TextField quantityField, VBox ordersDisplay, List<HotelTable> tables, HotelTable table) {
        String quantityStr = quantityField.getText();
        MenuItem selectedItem = menuItemSelector.getValue();

        // Check if an order already exists for this table and menu item
        Optional<Order> existingOrderOptional = OrderHandler.fetchOrdersForTable(table.getId()).stream()
                .filter(order -> order.getMenuItemId() == selectedItem.getId())
                .findFirst();

        if (validateInputs(quantityStr, selectedItem)) {
            try {
                int quantity = Integer.parseInt(quantityStr);

                if (existingOrderOptional.isPresent()) {
                    // If an order exists, update the quantity
                    Order existingOrder = existingOrderOptional.get();
                    existingOrder.setQuantity(existingOrder.getQuantity() + quantity); // Increment the existing quantity
                    OrderHandler.updateOrder(existingOrder); // Save the updated order to the database
                } else {
                    // If no existing order, create a new one
                    Order newOrder = new Order(Long.valueOf(1), table.getId(), selectedItem.getId(), quantity);
                    OrderHandler.addOrder(newOrder); // Save new order to the database
                }
                quantityField.clear();
                displayOrdersForTable(table.getTableName(), ordersDisplay, tables);
            } catch (NumberFormatException ex) {
                showErrorDialog("Please enter a valid quantity.");
            }
        } else if (selectedItem == null) {
            showErrorDialog("Invalid selection. Please select a valid menu item.");
        }
    }


    private boolean validateInputs(String quantityStr, MenuItem selectedItem) {
        if (selectedItem == null) {
            showErrorDialog("Please select a menu item.");
            return false;
        }
        if (quantityStr.isEmpty()) {
            showErrorDialog("Please enter a quantity.");
            return false;
        }
        return true;
    }



    private void displayOrdersForTable(String table, VBox ordersDisplay, List<HotelTable> tableList) {
        ordersDisplay.getChildren().clear();

        int tableId = tableList.stream().filter(t -> t.getTableName().equals(table)).findFirst().get().getId();

        // Fetch orders from the database for the selected table
        List<Order> orders = OrderHandler.fetchOrdersForTable(tableId);

        if (orders.isEmpty()) {
            ordersDisplay.getChildren().add(new Label("No orders for " + table + "."));
            return;
        }

        // Create a VBox for the table and total label
        VBox tableContainer = new VBox(10);
        tableContainer.setAlignment(Pos.TOP_CENTER);

        // Create TableView for displaying orders
        TableView<OrderRow> ordersTable = new TableView<>();
        ordersTable.setPrefWidth(400);

        ordersTable.setRowFactory(tv -> {
            TableRow<OrderRow> row = new TableRow<>();
            row.setMinHeight(30);  // Set a minimum height for rows
            return row;
        });


        // Define columns
        TableColumn<OrderRow, String> itemColumn = new TableColumn<>("Item");
        itemColumn.setCellValueFactory(data -> data.getValue().itemNameProperty());
        itemColumn.setPrefWidth(200);

        TableColumn<OrderRow, Integer> quantityColumn = new TableColumn<>("Quantity");
        quantityColumn.setCellValueFactory(data -> data.getValue().quantityProperty().asObject());
        quantityColumn.setPrefWidth(100);

        TableColumn<OrderRow, Double> totalColumn = new TableColumn<>("Total");
        totalColumn.setCellValueFactory(data -> data.getValue().totalPriceProperty().asObject());
        totalColumn.setPrefWidth(100);

        // Add columns to the table
        ordersTable.getColumns().addAll(itemColumn, quantityColumn, totalColumn);

        // Populate the table with data
        double grandTotal = 0;
        for (Order order : orders) {
            MenuItem menuItem = MenuItemFetcher.fetchMenuItemById(order.getMenuItemId());
            if (menuItem != null) {
                String itemName = menuItem.getName();
                int quantity = order.getQuantity();
                double totalPrice = menuItem.getPrice() * quantity;

                ordersTable.getItems().add(new OrderRow(itemName, quantity, totalPrice));
                grandTotal += totalPrice;
            }
        }

        // Display the total price of all items
        Label totalLabel = new Label("Total : " + String.format("%.2f", grandTotal)+" ₹");
        totalLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        // Add the table and total to the container
        tableContainer.getChildren().addAll(ordersTable, totalLabel);

        // Wrap the table container in a ScrollPane
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(tableContainer);
        scrollPane.setFitToWidth(true); // Adjust width to the page
        scrollPane.setFitToHeight(true); // Adjust height to the page

        // Set the maximum height for the ScrollPane
        scrollPane.setPrefHeight(500); // Adjust this value as needed

        // Add the ScrollPane to the main display
        ordersDisplay.getChildren().add(scrollPane);
    }


    private void showErrorDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showSuccessDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("SUCCESS");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void printReceipt(String selectedTable, List<HotelTable> tableList) {
        int tableId = tableList.stream().filter(t -> t.getTableName().equals(selectedTable)).findFirst().get().getId();

        // Fetch orders for the table
        List<Order> orders = OrderHandler.fetchOrdersForTable(tableId);

        if (orders.isEmpty()) {
            showErrorDialog("No orders to print for " + selectedTable + ".");
            return;
        }

        StringBuilder receipt = new StringBuilder();
        receipt.append("                हॉटेल चुलांगण                \n");

        receipt.append(String.format("%-20s %-60s\n", "Receipt: " + selectedTable, "Dt. " + getTodayDate()));
        receipt.append("----------------------------------------\n");
        receipt.append(String.format("%-20s %-10s %-10s\n", "Item", "Qty", "Total(₹)"));
        receipt.append("----------------------------------------\n");

        double grandTotal = 0;
        for (Order order : orders) {
            MenuItem menuItem = MenuItemFetcher.fetchMenuItemById(order.getMenuItemId());
            if (menuItem != null) {
                String itemName = menuItem.getName();
                int quantity = order.getQuantity();
                double totalPrice = menuItem.getPrice() * quantity;

                receipt.append(String.format("%-20s %-10d %-10.2f\n", itemName, quantity, totalPrice));
                grandTotal += totalPrice;
            }
        }

        receipt.append("----------------------------------------\n");
        receipt.append(String.format("%-30s %-10.2f\n", "Grand Total:", grandTotal));
        receipt.append("----------------------------------------\n");
        receipt.append("Thank you, please visit again!!\n");
        System.out.println(receipt);

        // Send receipt to the printer
        //sendToPrinter(receipt.toString());
    }

    /*private void sendToPrinter(String receipt) {
        try {
            // Create a PrintJob
            PrintService printService = findPrintService("YourPrinterName");
            if (printService == null) {
                showErrorDialog("Printer not found.");
                return;
            }

            DocPrintJob job = printService.createPrintJob();
            InputStream inputStream = new ByteArrayInputStream(receipt.getBytes());

            Doc doc = new SimpleDoc(inputStream, DocFlavor.INPUT_STREAM.AUTOSENSE, null);

            // Print the document
            job.print(doc, null);

        } catch (Exception e) {
            e.printStackTrace();
            showErrorDialog("Error printing receipt: " + e.getMessage());
        }
    }*/

    /*// Helper function to find the printer
    private PrintService findPrintService(String printerName) {
        PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);
        for (PrintService service : printServices) {
            if (service.getName().equalsIgnoreCase(printerName)) {
                return service;
            }
        }
        return null;
    }

    // Helper function to find the printer
    private PrintService findPrintService(String printerName) {
        PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);
        for (PrintService service : printServices) {
            if (service.getName().equalsIgnoreCase(printerName)) {
                return service;
            }
        }
        return null;
    }*/

    private String getTodayDate(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String today = dateFormat.format(new Date());
        return today;
    }

}
