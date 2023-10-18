package com.example.airplane_android.admin.baseInterface;

import com.example.airplane_android.admin.model.Plane;
import com.example.airplane_android.admin.model.Trip;

import java.util.List;

public interface ITripService {
    List<Trip> GetAll();
    Trip GetTripById(String id);
    void CreateTrip(Trip trip);
    void EditTrip(Trip trip);
    void DeleteTrip(String id);
}
