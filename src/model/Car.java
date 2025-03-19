package model;

public class Car {
    private int id;
    private String brand;
    private String model;
    private String color;
    private String fuel_type;
    private int seats;
    private double pricePerDay;
    private String imagePath;
    private boolean available;
    
    public Car(int id, String brand, String model, String color, int seats, 
               double pricePerDay, String imagePath, boolean available) {
        this.id = id;
        this.brand = brand;
        this.model = model;
        this.color = color;
        this.seats = seats;
        this.pricePerDay = pricePerDay;
        this.imagePath = imagePath;
        this.available = available;
    }
    
    // Getters
    public int getId() {
        return id;
    }
    
    public String getBrand() {
        return brand;
    }
    
    public String getModel() {
        return model;
    }
    
    public String getColor() {
        return color;
    }
    
    public int getSeats() {
        return seats;
    }
    
    public double getPricePerDay() {
        return pricePerDay;
    }
    
    public String getImagePath() {
        return imagePath;
    }
    
    public String getfuel_type() {
        return fuel_type;
    }
    
    public boolean isAvailable() {
        return available;
    }
    
    // Setters
    public void setId(int id) {
        this.id = id;
    }
    
    public void setBrand(String brand) {
        this.brand = brand;
    }
    
    public void setModel(String model) {
        this.model = model;
    }
    
    public void setColor(String color) {
        this.color = color;
    }
    
    public void setSeats(int seats) {
        this.seats = seats;
    }
    
    public void setfuel_type(String fuel_type) {
        this.fuel_type = fuel_type;
    }
    
    public void setPricePerDay(double pricePerDay) {
        this.pricePerDay = pricePerDay;
    }
    
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
    
    public void setAvailable(boolean available) {
        this.available = available;
    }
    
    @Override
    public String toString() {
        return brand + " " + model;
    }
}