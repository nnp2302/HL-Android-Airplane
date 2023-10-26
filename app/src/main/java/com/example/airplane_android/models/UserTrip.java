package com.example.airplane_android.models;

public class UserTrip {
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
}
