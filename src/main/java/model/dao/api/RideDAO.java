package model.dao.api;

import model.beans.Ride;

import java.util.List;

public interface RideDAO extends DAO<Ride, Integer> {
    List<Ride> getAllUserRides(Integer userId);
}
