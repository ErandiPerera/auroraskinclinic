package com.erskinclinic.app.Entity;

import java.time.LocalTime;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class Schedule {
	 @Column(nullable = false) 
    private String timeSlot; 

    // Default constructor required by JPA
    public Schedule() {}

    public Schedule(LocalTime start, LocalTime end) {
        this.timeSlot = start + " - " + end; 
    }

    public String getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot;
    }

    @Override
    public String toString() {
        return timeSlot;
    }
    
    public Schedule(String timeSlot) {
        if (timeSlot != null) {
            String[] parts = timeSlot.split(" - ");
            if (parts.length == 2) {
                this.timeSlot = timeSlot; 
            } else {
                throw new IllegalArgumentException("Invalid time slot format: " + timeSlot);
            }
        } else {
            throw new IllegalArgumentException("Time slot cannot be null");
        }
    }
}
