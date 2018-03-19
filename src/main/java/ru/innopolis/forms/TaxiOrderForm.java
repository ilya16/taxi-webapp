package ru.innopolis.forms;

/**
 * Taxi ordering form.
 *
 * @author      Ilya Borovik
 * @version     1.0
 */
public class TaxiOrderForm {

    private int cityId;
    private boolean saveCity = true;
    private String locationFrom;
    private String locationTo;
    private String phoneNumber;
    private boolean savePhoneNumber = true;
    private int serviceId;
    private boolean childSeat;
    private String orderComments;

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public boolean isSaveCity() {
        return saveCity;
    }

    public void setSaveCity(boolean saveCity) {
        this.saveCity = saveCity;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean isSavePhoneNumber() {
        return savePhoneNumber;
    }

    public void setSavePhoneNumber(boolean savePhoneNumber) {
        this.savePhoneNumber = savePhoneNumber;
    }

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public boolean isChildSeat() {
        return childSeat;
    }

    public void setChildSeat(boolean childSeat) {
        this.childSeat = childSeat;
    }

    public String getOrderComments() {
        return orderComments;
    }

    public void setOrderComments(String orderComments) {
        this.orderComments = orderComments;
    }

    @Override
    public String toString() {
        return "TaxiOrderForm{" +
                "cityId=" + cityId +
                ", saveCity=" + saveCity +
                ", locationFrom='" + locationFrom + '\'' +
                ", locationTo='" + locationTo + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", savePhoneNumber=" + savePhoneNumber +
                ", serviceId=" + serviceId +
                ", childSeat=" + childSeat +
                ", orderComments='" + orderComments + '\'' +
                '}';
    }
}
