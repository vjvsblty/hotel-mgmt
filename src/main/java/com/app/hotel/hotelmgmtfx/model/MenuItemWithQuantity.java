package com.app.hotel.hotelmgmtfx.model;

public class MenuItemWithQuantity {
    private int menuItemId;
    private String menuItemName;
    private int quantity;

    // Constructor
    public MenuItemWithQuantity(int menuItemId, String menuItemName, int quantity) {
        this.menuItemId = menuItemId;
        this.menuItemName = menuItemName;
        this.quantity = quantity;
    }

    // Getters and Setters
    public int getMenuItemId() {
        return menuItemId;
    }

    public void setMenuItemId(int menuItemId) {
        this.menuItemId = menuItemId;
    }

    public String getMenuItemName() {
        return menuItemName;
    }

    public void setMenuItemName(String menuItemName) {
        this.menuItemName = menuItemName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "MenuItemWithQuantity{" +
                "menuItemId=" + menuItemId +
                ", menuItemName='" + menuItemName + '\'' +
                ", quantity=" + quantity +
                '}';
    }
}