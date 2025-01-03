package com.app.hotel.hotelmgmtfx.model;

import com.app.hotel.hotelmgmtfx.utils.ExpenseHandler;
import com.app.hotel.hotelmgmtfx.utils.OwnerHandler;

public class Expense {
    private Long id;
    private String name;
    private double cost;
    private int ownerId;
    private String date;

    public Expense() {
    }

    // Constructor
    public Expense(Long id, String name, double cost, int ownerId, String date) {
        this.id = id;
        this.name = name;
        this.cost = cost;
        this.ownerId = ownerId;
        this.date = date;
    }

    // Method to get owner name by ownerId
    public String getOwnerName() {
        return fetchOwnerNameById(ownerId);
    }

    // Fetch owner name from the database using ownerId
    private String fetchOwnerNameById(int ownerId) {
        return OwnerHandler.fetchOwnerNameById(ownerId);
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
