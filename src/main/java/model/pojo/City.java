package model.pojo;

import java.io.Serializable;
import java.util.Objects;

public class City implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String name;
    private String region;
    private boolean isUnsupported;

    public City(int id, String name, String region, boolean isUnsupported) {
        this.id = id;
        this.name = name;
        this.region = region;
        this.isUnsupported = isUnsupported;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        City city = (City) o;
        return id == city.id &&
                isUnsupported == city.isUnsupported &&
                Objects.equals(name, city.name) &&
                Objects.equals(region, city.region);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, region, isUnsupported);
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
