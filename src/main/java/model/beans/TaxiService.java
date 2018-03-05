package model.beans;

import java.io.Serializable;

public class TaxiService implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private int cityId;
    private City city;
    private String serviceType;
    private int baseRate;
    private boolean isRemoved;

    public TaxiService(int id, int cityId, String serviceType, int baseRate, boolean isRemoved) {
        this.id = id;
        this.cityId = cityId;
        this.serviceType = serviceType;
        this.baseRate = baseRate;
        this.isRemoved = isRemoved;
    }

    public TaxiService(int id, City city, String serviceType, int baseRate, boolean isRemoved) {
        this.id = id;
        this.cityId = city.getId();
        this.city = city;
        this.serviceType = serviceType;
        this.baseRate = baseRate;
        this.isRemoved = isRemoved;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
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
        return "TaxiService{" +
                "id=" + id +
                ", cityId=" + cityId +
                ", city=" + city +
                ", serviceType='" + serviceType + '\'' +
                ", baseRate=" + baseRate +
                ", isRemoved=" + isRemoved +
                '}';
    }
}
