package com.app.hotel.hotelmgmtfx.model;

public class Order {
    private Long id;
    private int tableId;
    private int menuItemId;
    private int quantity;

    // Constructor
    public Order(Long id, int tableId, int menuItemId, int quantity) {
        this.id = id;
        this.tableId = tableId;
        this.menuItemId = menuItemId;
        this.quantity = quantity;
    }

    // Getters and Setters
    public int getTableId() {
        return tableId;
    }

    public void setTableId(int tableId) {
        this.tableId = tableId;
    }

    public int getMenuItemId() {
        return menuItemId;
    }

    public void setMenuItemId(int menuItemId) {
        this.menuItemId = menuItemId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Order{" +
                "tableId=" + tableId +
                ", menuItemId=" + menuItemId +
                ", quantity=" + quantity +
                '}';
    }
}
