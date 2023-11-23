package com.bikie.in.POJO_Classes;

import com.google.firebase.Timestamp;

public class Booking {
    String vehicleId;
    private Timestamp pickupTime,dropoffTime;

    public Booking() {
    }

    public Booking(String vehicleId, Timestamp pickupTime, Timestamp dropoffTime) {
        this.vehicleId = vehicleId;
        this.pickupTime = pickupTime;
        this.dropoffTime = dropoffTime;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public Timestamp getPickupTime() {
        return pickupTime;
    }

    public void setPickupTime(Timestamp pickupTime) {
        this.pickupTime = pickupTime;
    }

    public Timestamp getDropoffTime() {
        return dropoffTime;
    }

    public void setDropoffTime(Timestamp dropoffTime) {
        this.dropoffTime = dropoffTime;
    }
}
