package com.example.airplane_android.admin.model;

import java.io.Serializable;
import java.util.Date;

public class Trip implements Serializable {
    private String Id;
    private String From;
    private String To;
    private int BusinessPrice;
    private int EconomyPrice;
    private int BusinessTicket;
    private int EconomyTicket;
    private String Start;
    private String End;
    private String RoundTrip;
    private String PlaneId;
    public Trip() {

    }

    public Trip(String id, String from, String to, int businessPrice, int economyPrice, int businessTicket, int economyTicket, String start, String end, String roundTrip, String idPlane) {
        Id = id;
        From = from;
        To = to;
        BusinessPrice = businessPrice;
        EconomyPrice = economyPrice;
        BusinessTicket = businessTicket;
        EconomyTicket = economyTicket;
        Start = start;
        End = end;
        RoundTrip = roundTrip;
        PlaneId = idPlane;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getFrom() {
        return From;
    }

    public void setFrom(String from) {
        From = from;
    }

    public String getTo() {
        return To;
    }

    public void setTo(String to) {
        To = to;
    }

    public Number getBusinessPrice() {
        return BusinessPrice;
    }

    public void setBusinessPrice(int businessPrice) {
        BusinessPrice = businessPrice;
    }

    public Number getEconomyPrice() {
        return EconomyPrice;
    }

    public void setEconomyPrice(int economyPrice) {
        EconomyPrice = economyPrice;
    }

    public Number getBusinessTicket() {
        return BusinessTicket;
    }

    public void setBusinessTicket(int businessTicket) {
        BusinessTicket = businessTicket;
    }

    public Number getEconomyTicket() {
        return EconomyTicket;
    }

    public void setEconomyTicket(int economyTicket) {
        EconomyTicket = economyTicket;
    }

    public String getStart() {
        return Start;
    }

    public void setStart(String start) {
        Start = start;
    }

    public String getEnd() {
        return End;
    }

    public void setEnd(String end) {
        End = end;
    }

    public String getRoundTrip() {
        return RoundTrip;
    }

    public void setRoundTrip(String roundTrip) {
        RoundTrip = roundTrip;
    }

    public String getPlaneId() {
        return PlaneId;
    }

    public void setPlaneId(String planeId) {
        PlaneId = planeId;
    }
}
