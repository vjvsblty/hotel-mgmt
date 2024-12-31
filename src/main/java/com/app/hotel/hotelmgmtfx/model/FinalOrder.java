package com.app.hotel.hotelmgmtfx.model;


import java.util.List;

public class FinalOrder {
    private long id;
    private int tableId;
    private String orderDate;
    private double totalCost;
    private List<MenuItemWithQuantity> items;

    // Constructor
    public FinalOrder(long id, int tableId, String orderDate, double totalCost, List<MenuItemWithQuantity> items) {
        this.id = id;
        this.tableId = tableId;
        this.orderDate = orderDate;
        this.totalCost = totalCost;
        this.items = items;
    }

    // Getters and Setters
    public long getOrderId() {
        return id;
    }

    public void setOrderId(long orderId) {
        this.id = orderId;
    }

    public int getTableId() {
        return tableId;
    }

    public void setTableId(int tableId) {
        this.tableId = tableId;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public List<MenuItemWithQuantity> getItems() {
        return items;
    }

    public void setItems(List<MenuItemWithQuantity> items) {
        this.items = items;
    }

    // Inner Class for Menu Item and Quantity

    @Override
    public String toString() {
        return "FinalOrder{" +
                "orderId=" + id +
                ", tableId=" + tableId +
                ", orderDate='" + orderDate + '\'' +
                ", totalCost=" + totalCost +
                ", items=" + items +
                '}';
    }
}
