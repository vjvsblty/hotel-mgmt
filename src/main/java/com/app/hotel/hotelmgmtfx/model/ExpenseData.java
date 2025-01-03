package com.app.hotel.hotelmgmtfx.model;

public class ExpenseData {
    private String expenseName;
    private Double cost;
    private String doneBy;
    private String date;

    public ExpenseData(String expenseName, Double cost, String doneBy, String date) {
        this.expenseName = expenseName;
        this.cost = cost;
        this.doneBy = doneBy;
        this.date = date;
    }

    public String getExpenseName() {
        return expenseName;
    }

    public Double getCost() {
        return cost;
    }

    public String getDoneBy() {
        return doneBy;
    }

    public String getDate() {
        return date;
    }
}
