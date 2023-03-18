package com.example.car_rental_prm392.model;

public class Car {
    private int id;
    private String name;
    private String description;
    private double price;
    private byte[] image;
    private String dateUpdate;
    private int status;
    private int locationId;

    public Car() {
    }

    public Car(String name, String description, double price, byte[] image, String dateUpdate, int status, int locationId) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.image = image;
        this.dateUpdate = dateUpdate;
        this.status = status;
        this.locationId = locationId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getDateUpdate() {
        return dateUpdate;
    }

    public void setDateUpdate(String dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }
}
