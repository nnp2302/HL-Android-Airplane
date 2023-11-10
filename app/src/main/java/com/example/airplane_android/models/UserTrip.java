package com.example.airplane_android.models;

import android.util.Log;

import com.google.firebase.firestore.DocumentSnapshot;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class UserTrip implements Serializable {
  private String Id;
  private String From;
  private String To;
  private int Price;
  private String Start;
  private String End;
  private String EstimateTime;
  private String PlaneId;

  public UserTrip() {}


  public UserTrip(String id, String from, String to, int price, String start, String end, String estimateTime, String idPlane) {
    this.Id = id;
    this.From = from;
    this.To = to;
    this.Price = price;
    this.Start = start;
    this.End = end;
    this.EstimateTime = estimateTime;
    this.PlaneId = idPlane;
  }

  public String getId() {
    return this.Id;
  }

  public void setId(String id) {
    this.Id = id;
  }

  public String getFrom() {
    return this.From;
  }

  public void setFrom(String from) {
    this.From = from;
  }

  public String getTo() {
    return this.To;
  }

  public void setTo(String to) {
    this.To = to;
  }

  public Number getPrice() {
    return this.Price;
  }

  public void setPrice(int price) {
    this.Price = price;
  }

  public String getStart() {
    return this.Start;
  }

  public void setStart(String start) {
    this.Start = start;
  }

  public String getEnd() {
    return this.End;
  }

  public void setEnd(String end) {
    this.End = end;
  }

  public String getEstimateTime() { return this.EstimateTime; }

  public void setEstimateTime(String estimateTime) { this.EstimateTime = estimateTime; }

  public String getPlaneId() {
    return this.PlaneId;
  }

  public void setPlaneId(String planeId) {
    this.PlaneId = planeId;
  }


  public static UserTrip fromDocument(DocumentSnapshot document) {
    if (document == null || !document.exists()) {
      return null;
    }

    String id = document.getString("Id");
    String from = document.getString("From");
    String to = document.getString("To");
    String start = document.getString("Start");
    String end = document.getString("End");
    String estimateTime = calculateEstimateTime(start,end);
    String planeId = document.getString("PlaneId");

    UserTrip userTrip = new UserTrip();
    userTrip.setId(id);
    userTrip.setFrom(from);
    userTrip.setTo(to);
    userTrip.setStart(start);
    userTrip.setEnd(end);
    userTrip.setEstimateTime(estimateTime);
    userTrip.setPlaneId(planeId);

    return userTrip;
  }
  public Map<String, Object> toMap() {
    Map<String, Object> map = new HashMap<>();
    map.put("Id", Id);
    map.put("From", From);
    map.put("To", To);
    map.put("Start", Start);
    map.put("End", End);
    map.put("EstimateTime", EstimateTime);
    map.put("PlaneId", PlaneId);
    return map;
  }
  private static String calculateEstimateTime(String startTime, String endTime) {
    final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.ENGLISH);

    Date convertTimeStart = null, convertTimeEnd = null;
    try {
      convertTimeStart = dateFormat.parse(startTime);
      convertTimeEnd = dateFormat.parse(endTime);
    } catch (Exception e) {
      Log.e("Convert date time", "Failed to convert string time to date, log: " + e);
    }

    long estimateTime = convertTimeEnd.getTime() - convertTimeStart.getTime();
    long hours = TimeUnit.MILLISECONDS.toHours(estimateTime);
    long minutes = TimeUnit.MILLISECONDS.toMinutes(estimateTime - TimeUnit.HOURS.toMillis(hours));
    long seconds = TimeUnit.MILLISECONDS.toSeconds(estimateTime - TimeUnit.HOURS.toMillis(hours) - TimeUnit.MINUTES.toMillis(minutes));

    String formattedTime = "";
    if (hours > 0)
      formattedTime = hours + "h" + minutes + "m";
    else if (minutes > 0)
      formattedTime = minutes + "m" + seconds + "s";
    else
      formattedTime = seconds + "s";
    return formattedTime;
  }

}
