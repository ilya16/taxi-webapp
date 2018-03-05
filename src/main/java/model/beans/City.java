package model.beans;

import java.io.Serializable;

public class City implements Serializable {
    private static final long serialVersionUID = 1L;

    private long id;
    private String name;
    private String region;
    private boolean isUnsupported;

    public City(long id, String name, String region, boolean isUnsupported) {
        this.id = id;
        this.name = name;
        this.region = region;
        this.isUnsupported = isUnsupported;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public boolean isUnsupported() {
        return isUnsupported;
    }

    public void setUnsupported(boolean unsupported) {
        isUnsupported = unsupported;
    }

    @Override
    public String toString() {
        return "City{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", region='" + region + '\'' +
                ", isUnsupported=" + isUnsupported +
                '}';
    }
}
