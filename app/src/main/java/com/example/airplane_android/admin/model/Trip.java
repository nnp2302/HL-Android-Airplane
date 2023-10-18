package com.example.airplane_android.admin.model;

public class Trip {
    private String brand;
    private String type;
    private boolean active;
    private Number capacity;

    public Trip(String brand, String type, boolean active, Number capacity) {
        this.brand = brand;
        this.type = type;
        this.active = active;
        this.capacity = capacity;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Number getCapacity() {
        return capacity;
    }

    public void setCapacity(Number capacity) {
        this.capacity = capacity;
    }
}
