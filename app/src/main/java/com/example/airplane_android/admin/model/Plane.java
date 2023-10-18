package com.example.airplane_android.admin.model;

import java.util.Date;

public class Plane {
    private String from;
    private String to;
    private String businessPrice;
    private String economyPrice;
    private String businessTicket;
    private String economyTicket;
    private Date start;
    private Date end;
    private Date roundTrip;
    private String idPlane;

    public Plane(String from, String to, String businessPrice, String economyPrice, String businessTicket, String economyTicket, Date start, Date end, Date roundTrip, String idPlane) {
        this.from = from;
        this.to = to;
        this.businessPrice = businessPrice;
        this.economyPrice = economyPrice;
        this.businessTicket = businessTicket;
        this.economyTicket = economyTicket;
        this.start = start;
        this.end = end;
        this.roundTrip = roundTrip;
        this.idPlane = idPlane;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getBusinessPrice() {
        return businessPrice;
    }

    public void setBusinessPrice(String businessPrice) {
        this.businessPrice = businessPrice;
    }

    public String getEconomyPrice() {
        return economyPrice;
    }

    public void setEconomyPrice(String economyPrice) {
        this.economyPrice = economyPrice;
    }

    public String getBusinessTicket() {
        return businessTicket;
    }

    public void setBusinessTicket(String businessTicket) {
        this.businessTicket = businessTicket;
    }

    public String getEconomyTicket() {
        return economyTicket;
    }

    public void setEconomyTicket(String economyTicket) {
        this.economyTicket = economyTicket;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public Date getRoundTrip() {
        return roundTrip;
    }

    public void setRoundTrip(Date roundTrip) {
        this.roundTrip = roundTrip;
    }

    public String getIdPlane() {
        return idPlane;
    }

    public void setIdPlane(String idPlane) {
        this.idPlane = idPlane;
    }
}
