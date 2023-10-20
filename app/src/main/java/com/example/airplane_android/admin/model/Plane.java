package com.example.airplane_android.admin.model;

import java.io.Serializable;

public class Plane implements Serializable {
    private String Id;
    private String brand;
    private String type;
    private boolean active;
    private int capacity;

    public Plane() {
    }

    public Plane(String id, String brand, String type, boolean active, int capacity) {
        Id = id;
        this.brand = brand;
        this.type = type;
        this.active = active;
        this.capacity = capacity;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
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

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
}
