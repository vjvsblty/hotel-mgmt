package com.app.hotel.hotelmgmtfx.model;

public class HotelTable {
    private int id;
    private String tableName;
    private int tableSize;

    public HotelTable(int id, String tableName, int tableSize) {
        this.id = id;
        this.tableName = tableName;
        this.tableSize = tableSize;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public int getTableSize() {
        return tableSize;
    }

    public void setTableSize(int tableSize) {
        this.tableSize = tableSize;
    }

    @Override
    public String toString() {
        return "HotelTable{" +
                "id=" + id +
                ", tableName='" + tableName + '\'' +
                ", tableSize=" + tableSize +
                '}';
    }
}
