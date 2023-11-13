package com.example.airplane_android.admin.model;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.HashMap;
import java.util.Map;

public class BillModel {
    private String planeId;
    private String from;
    private String to;
    private String start;
    private String end;
    private boolean isBusiness;
    private boolean purchaseStatus;
    private int quantity;
    private String estimateTime;
    private double price;
    private String fullname;
    private String identityId;
    private String address;
    private String dob;
    private String ticketCode;
    private String email;
    private String paymentMethod;
    private String uid;
    public BillModel() {
        // Required empty public constructor for Firebase
    }

    public BillModel(String planeId, String from, String to, String start, String end, boolean isBusiness, boolean isCheckIn,
                       boolean purchaseStatus, int quantity, String ticketStatus, String estimateTime, double price,
                       String fullname, String identityId, String address) {
        this.planeId = planeId;
        this.from = from;
        this.to = to;
        this.start = start;
        this.end = end;
        this.isBusiness = isBusiness;
        this.purchaseStatus = purchaseStatus;
        this.quantity = quantity;
        this.estimateTime = estimateTime;
        this.price = price;
        this.fullname = fullname;
        this.identityId = identityId;
        this.address = address;
    }
    public static BillModel fromDocument(DocumentSnapshot document) {
        if (document == null || !document.exists()) {
            return null;
        }
        String planeId = document.getString("PlaneId");
        String from = document.getString("From");
        String to = document.getString("To");
        String start = document.getString("Start");
        String end = document.getString("End");
        boolean isBusiness = document.getBoolean("isBusiness");
        boolean purchaseStatus = document.getBoolean("purchaseStatus");
        int quantity = document.getLong("quantity").intValue();
        String estimateTime = document.getString("EstimateTime");
        double price = document.getDouble("Price");
        String fullname = document.getString("fullname");
        String dob = document.getString("dob");
        String identityId = document.getString("identityId");
        String address = document.getString("address");
        String paymentMethod = document.getString("paymentMethod");
        String email = document.getString("email");
        String uid = document.getString("uid");
        BillModel BillModel = new BillModel();
        BillModel.setPlaneId(planeId);
        BillModel.setFrom(from);
        BillModel.setTo(to);
        BillModel.setStart(start);
        BillModel.setEnd(end);
        BillModel.setIsBusiness(isBusiness);
        BillModel.setPurchaseStatus(purchaseStatus);
        BillModel.setQuantity(quantity);
        BillModel.setEstimateTime(estimateTime);
        BillModel.setPrice(price);
        BillModel.setFullname(fullname);
        BillModel.setIdentityId(identityId);
        BillModel.setAddress(address);
        BillModel.setDob(dob);
        BillModel.setEmail(email);
        BillModel.setUid(uid);
        BillModel.setPaymentMethod(paymentMethod);
        return BillModel;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("PlaneId", planeId);
        map.put("From", from);
        map.put("To", to);
        map.put("Start", start);
        map.put("End", end);
        map.put("isBusiness", isBusiness);
        map.put("purchaseStatus", purchaseStatus);
        map.put("quantity", quantity);
        map.put("EstimateTime", estimateTime);
        map.put("Price", price);
        map.put("fullname", fullname);
        map.put("identityId", identityId);
        map.put("address", address);
        map.put("dob",dob);
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


    public boolean getPurchaseStatus() {
        return purchaseStatus;
    }

    public int getQuantity() {
        return quantity;
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


    public void setPurchaseStatus(boolean purchaseStatus) {
        this.purchaseStatus = purchaseStatus;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
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


    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
