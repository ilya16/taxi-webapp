package model.beans;

import java.io.Serializable;

public class Car implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String serialNumber;
    private String model;
    private String color;
    private int driverId;
    private Driver driver;
    private boolean hasChildSeat;
    private boolean isBlocked;

    public Car(int id, String serialNumber, String model, String color,
               int driverId, boolean hasChildSeat, boolean isBlocked) {
        this.id = id;
        this.serialNumber = serialNumber;
        this.model = model;
        this.color = color;
        this.driverId = driverId;
        this.hasChildSeat = hasChildSeat;
        this.isBlocked = isBlocked;
    }

    public Car(int id, String serialNumber, String model, String color,
               Driver driver, boolean hasChildSeat, boolean isBlocked) {
        this.id = id;
        this.serialNumber = serialNumber;
        this.model = model;
        this.color = color;
        this.driverId = driver.getId();
        this.driver = driver;
        this.hasChildSeat = hasChildSeat;
        this.isBlocked = isBlocked;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getDriverId() {
        return driverId;
    }

    public void setDriverId(int driverId) {
        this.driverId = driverId;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public boolean isHasChildSeat() {
        return hasChildSeat;
    }

    public void setHasChildSeat(boolean hasChildSeat) {
        this.hasChildSeat = hasChildSeat;
    }

    public boolean isBlocked() {
        return isBlocked;
    }

    public void setBlocked(boolean blocked) {
        isBlocked = blocked;
    }

    @Override
    public String toString() {
        return "Car{" +
                "id=" + id +
                ", serialNumber='" + serialNumber + '\'' +
                ", model='" + model + '\'' +
                ", color='" + color + '\'' +
                ", driverId=" + driverId +
                ", driver=" + driver +
                ", hasChildSeat=" + hasChildSeat +
                ", isBlocked=" + isBlocked +
                '}';
    }
}
