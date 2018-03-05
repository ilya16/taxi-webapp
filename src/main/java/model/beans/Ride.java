package model.beans;

import java.io.Serializable;
import java.sql.Timestamp;

public class Ride implements Serializable {
    private static final long serialVersionUID = 1L;

    private long id;
    private long userId;
    private User user;
    private long carId;
    private Car car;
    private long serviceId;
    private Service service;
    private Timestamp orderTime;
    private String locationFrom;
    private String locationTo;
    private Timestamp timeStart;
    private Timestamp timeEnd;
    private int price;
    private int rating;
    private String orderComments;
    private String status;

    public Ride(long id, long userId, long carId, long serviceId, Timestamp orderTime,
                String locationFrom, String locationTo, Timestamp timeStart, Timestamp timeEnd,
                int price, int rating, String orderComments, String status) {
        this.id = id;
        this.userId = userId;
        this.carId = carId;
        this.serviceId = serviceId;
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

    public Ride(long id, User user, Car car, Service service, Timestamp orderTime,
                String locationFrom, String locationTo, Timestamp timeStart, Timestamp timeEnd,
                int price, int rating, String orderComments, String status) {
        this.id = id;
        this.user = user;
        this.car = car;
        this.service = service;
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public long getCarId() {
        return carId;
    }

    public void setCarId(long carId) {
        this.carId = carId;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public long getServiceId() {
        return serviceId;
    }

    public void setServiceId(long serviceId) {
        this.serviceId = serviceId;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
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
                ", serviceId=" + serviceId +
                ", service=" + service +
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
