package model.beans;

import java.io.Serializable;

public class Service implements Serializable {
    private static final long serialVersionUID = 1L;

    private long id;
    private long cityId;
    private City city;
    private String serviceType;
    private int baseRate;
    private boolean isRemoved;

    public Service(long id, long cityId, String serviceType, int baseRate, boolean isRemoved) {
        this.id = id;
        this.cityId = cityId;
        this.serviceType = serviceType;
        this.baseRate = baseRate;
        this.isRemoved = isRemoved;
    }

    public Service(long id, City city, String serviceType, int baseRate, boolean isRemoved) {
        this.id = id;
        this.cityId = city.getId();
        this.city = city;
        this.serviceType = serviceType;
        this.baseRate = baseRate;
        this.isRemoved = isRemoved;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCityId() {
        return cityId;
    }

    public void setCityId(long cityId) {
        this.cityId = cityId;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public int getBaseRate() {
        return baseRate;
    }

    public void setBaseRate(int baseRate) {
        this.baseRate = baseRate;
    }

    public boolean isRemoved() {
        return isRemoved;
    }

    public void setRemoved(boolean removed) {
        isRemoved = removed;
    }

    @Override
    public String toString() {
        return "Service{" +
                "id=" + id +
                ", cityId=" + cityId +
                ", city=" + city +
                ", serviceType='" + serviceType + '\'' +
                ", baseRate=" + baseRate +
                ", isRemoved=" + isRemoved +
                '}';
    }
}
