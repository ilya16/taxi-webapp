package model.beans;

import java.io.Serializable;
import java.sql.Timestamp;

public class Ride implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private int userId;
    private User user;
    private int carId;
    private Car car;
    private int taxiServiceId;
    private TaxiService taxiService;
    private Timestamp orderTime;
    private String locationFrom;
    private String locationTo;
    private Timestamp timeStart;
    private Timestamp timeEnd;
    private int price;
    private int rating;
    private String orderComments;
    private String status;

    public Ride() {
    }

    public Ride(int id, int userId, int carId, int taxiServiceId, Timestamp orderTime,
                String locationFrom, String locationTo, Timestamp timeStart, Timestamp timeEnd,
                int price, int rating, String orderComments, String status) {
        this.id = id;
        this.userId = userId;
        this.carId = carId;
        this.taxiServiceId = taxiServiceId;
        this.orderTime = orderTime;
        this.locationFrom = locationFrom;
        this.locationTo = locationTo;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.price = price;
        this.rating = rating;
        this.orderComments = orderComments;
        this.status = status;
    }

    public Ride(int id, int userId, Car car, TaxiService taxiService, Timestamp orderTime,
                String locationFrom, String locationTo, Timestamp timeStart, Timestamp timeEnd,
                int price, int rating, String orderComments, String status) {
        this.id = id;
        this.userId = userId;
        this.car = car;
        this.taxiService = taxiService;
        this.orderTime = orderTime;
        this.locationFrom = locationFrom;
        this.locationTo = locationTo;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.price = price;
        this.rating = rating;
        this.orderComments = orderComments;
        this.status = status;
    }

    public Ride(int id, User user, Car car, TaxiService taxiService, Timestamp orderTime,
                String locationFrom, String locationTo, Timestamp timeStart, Timestamp timeEnd,
                int price, int rating, String orderComments, String status) {
        this.id = id;
        this.user = user;
        this.car = car;
        this.taxiService = taxiService;
        this.orderTime = orderTime;
        this.locationFrom = locationFrom;
        this.locationTo = locationTo;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.price = price;
        this.rating = rating;
        this.orderComments = orderComments;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getCarId() {
        return carId;
    }

    public void setCarId(int carId) {
        this.carId = carId;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public int getTaxiServiceId() {
        return taxiServiceId;
    }

    public void setTaxiServiceId(int taxiServiceId) {
        this.taxiServiceId = taxiServiceId;
    }

    public TaxiService getTaxiService() {
        return taxiService;
    }

    public void setTaxiService(TaxiService taxiService) {
        this.taxiService = taxiService;
    }

    public Timestamp getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Timestamp orderTime) {
        this.orderTime = orderTime;
    }

    public String getLocationFrom() {
        return locationFrom;
    }

    public void setLocationFrom(String locationFrom) {
        this.locationFrom = locationFrom;
    }

    public String getLocationTo() {
        return locationTo;
    }

    public void setLocationTo(String locationTo) {
        this.locationTo = locationTo;
    }

    public Timestamp getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(Timestamp timeStart) {
        this.timeStart = timeStart;
    }

    public Timestamp getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(Timestamp timeEnd) {
        this.timeEnd = timeEnd;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getOrderComments() {
        return orderComments;
    }

    public void setOrderComments(String orderComments) {
        this.orderComments = orderComments;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Ride{" +
                "id=" + id +
                ", userId=" + userId +
                ", user=" + user +
                ", carId=" + carId +
                ", car=" + car +
                ", taxiServiceId=" + taxiServiceId +
                ", taxiService=" + taxiService +
                ", orderTime=" + orderTime +
                ", locationFrom='" + locationFrom + '\'' +
                ", locationTo='" + locationTo + '\'' +
                ", timeStart=" + timeStart +
                ", timeEnd=" + timeEnd +
                ", price=" + price +
                ", rating=" + rating +
                ", orderComments='" + orderComments + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
