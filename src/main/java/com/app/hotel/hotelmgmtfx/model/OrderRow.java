package com.app.hotel.hotelmgmtfx.model;

import javafx.beans.property.*;

public class OrderRow {
    private final StringProperty itemName;
    private final IntegerProperty quantity;
    private final DoubleProperty totalPrice;

    public OrderRow(String itemName, int quantity, double totalPrice) {
        this.itemName = new SimpleStringProperty(itemName);
        this.quantity = new SimpleIntegerProperty(quantity);
        this.totalPrice = new SimpleDoubleProperty(totalPrice);
    }

    public StringProperty itemNameProperty() {
        return itemName;
    }

    public IntegerProperty quantityProperty() {
        return quantity;
    }

    public DoubleProperty totalPriceProperty() {
        return totalPrice;
    }
}

