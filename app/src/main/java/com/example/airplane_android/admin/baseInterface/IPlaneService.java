package com.example.airplane_android.admin.baseInterface;

import com.example.airplane_android.admin.model.Plane;

import java.util.List;

public interface IPlaneService {
    List<Plane> GetAll();
    Plane GetPlaneById(String id);
    void CreatePlane(Plane plane);
    void EditPlane(Plane plane);
    void DeletePlane(String id);

}
