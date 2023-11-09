package com.example.airplane_android.models;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.HashMap;
import java.util.Map;

public class TicketModel {
    private String planeId;
    private String from;
    private String to;
    private String start;
    private String end;
    private boolean isBusiness;
    private boolean isCheckIn;
    private boolean purchaseStatus;
    private int quantity;
    private String ticketStatus;
    private String estimateTime;
    private double price;
    private String fullname;
    private String identityId;
    private String address;

    private String ticketCode;
    public TicketModel() {
        // Required empty public constructor for Firebase
    }

    public TicketModel(String planeId, String from, String to, String start, String end, boolean isBusiness, boolean isCheckIn,
                       boolean purchaseStatus, int quantity, String ticketStatus, String estimateTime, double price,
                       String fullname, String identityId, String address) {
        this.planeId = planeId;
        this.from = from;
        this.to = to;
        this.start = start;
        this.end = end;
        this.isBusiness = isBusiness;
        this.isCheckIn = isCheckIn;
        this.purchaseStatus = purchaseStatus;
        this.quantity = quantity;
        this.ticketStatus = ticketStatus;
        this.estimateTime = estimateTime;
        this.price = price;
        this.fullname = fullname;
        this.identityId = identityId;
        this.address = address;
    }
    public static TicketModel fromDocument(QueryDocumentSnapshot document) {
        if (document == null || !document.exists()) {
            return null;
        }
        String planeId = document.getString("PlaneId");
        String from = document.getString("From");
        String to = document.getString("To");
        String start = document.getString("Start");
        String end = document.getString("End");
        boolean isBusiness = document.getBoolean("isBusiness");
        boolean isCheckIn = document.getBoolean("isCheckIn");
        boolean purchaseStatus = document.getBoolean("purchaseStatus");
        int quantity = Integer.parseInt(document.getString("quantity"));
        String ticketStatus = document.getString("ticketStatus");
        String estimateTime = document.getString("EstimateTime");
        double price = document.getDouble("Price");
        String fullname = document.getString("fullname");
        String identityId = document.getString("identityId");
        String address = document.getString("address");

        TicketModel ticketModel = new TicketModel();
        ticketModel.setPlaneId(planeId);
        ticketModel.setFrom(from);
        ticketModel.setTo(to);
        ticketModel.setStart(start);
        ticketModel.setEnd(end);
        ticketModel.setIsBusiness(isBusiness);
        ticketModel.setIsCheckIn(isCheckIn);
        ticketModel.setPurchaseStatus(purchaseStatus);
        ticketModel.setQuantity(quantity);
        ticketModel.setTicketStatus(ticketStatus);
        ticketModel.setEstimateTime(estimateTime);
        ticketModel.setPrice(price);
        ticketModel.setFullname(fullname);
        ticketModel.setIdentityId(identityId);
        ticketModel.setAddress(address);

        return ticketModel;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("planeId", planeId);
        map.put("from", from);
        map.put("to", to);
        map.put("start", start);
        map.put("end", end);
        map.put("isBusiness", isBusiness);
        map.put("isCheckIn", isCheckIn);
        map.put("purchaseStatus", purchaseStatus);
        map.put("quantity", quantity);
        map.put("ticketStatus", ticketStatus);
        map.put("estimateTime", estimateTime);
        map.put("price", price);
        map.put("fullname", fullname);
        map.put("identityId", identityId);
        map.put("address", address);
        return map;
    }
    public String getPlaneId() {
        return planeId;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getStart() {
        return start;
    }

    public String getEnd() {
        return end;
    }

    public boolean isBusiness() {
        return isBusiness;
    }

    public boolean isCheckIn() {
        return isCheckIn;
    }

    public boolean getPurchaseStatus() {
        return purchaseStatus;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getTicketStatus() {
        return ticketStatus;
    }

    public String getEstimateTime() {
        return estimateTime;
    }

    public double getPrice() {
        return price;
    }

    public String getFullname() {
        return fullname;
    }

    public String getIdentityId() {
        return identityId;
    }

    public String getAddress() {
        return address;
    }
    public void setPlaneId(String planeId) {
        this.planeId = planeId;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public void setIsBusiness(boolean isBusiness) {
        this.isBusiness = isBusiness;
    }

    public void setIsCheckIn(boolean isCheckIn) {
        this.isCheckIn = isCheckIn;
    }

    public void setPurchaseStatus(boolean purchaseStatus) {
        this.purchaseStatus = purchaseStatus;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setTicketStatus(String ticketStatus) {
        this.ticketStatus = ticketStatus;
    }

    public void setEstimateTime(String estimateTime) {
        this.estimateTime = estimateTime;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public void setIdentityId(String identityId) {
        this.identityId = identityId;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTicketCode() {
        return ticketCode;
    }

    public void setTicketCode(String ticketCode) {
        this.ticketCode = ticketCode;
    }
}
