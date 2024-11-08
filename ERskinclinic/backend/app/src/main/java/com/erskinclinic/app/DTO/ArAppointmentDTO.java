package com.erskinclinic.app.DTO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.erskinclinic.app.Entity.Schedule;
import com.erskinclinic.app.Enum.Dermatologist;
import com.erskinclinic.app.Enum.TreatmentType;
import com.erskinclinic.app.Service.AppointmentScheduler;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

public class ArAppointmentDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String firstName;
    private String lastName;
    private String nic; 
    private String email;
    private int phoneNo;

    @Enumerated(EnumType.STRING)
    private Dermatologist dermatologist; 

    @Enumerated(EnumType.STRING)
    private TreatmentType treatmentType; 
    
    private double treatmentPrice; 
    private double tax; 
    private double totalPrice; 
    
    private LocalDate appointmentDate; 

    @Column(nullable = false) 
    private Schedule selectedTimeSlot; 
    private double registrationFee = 500.00; 
    private boolean paid;

    public ArAppointmentDTO() {}

    public ArAppointmentDTO(int id, String firstName, String lastName, String nic, String email, int phoneNo,
                            Dermatologist dermatologist, TreatmentType treatmentType, double treatmentPrice, double tax,
                            double totalPrice, LocalDate appointmentDate, Schedule selectedTimeSlot, double registrationFee,
                            boolean paid) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.nic = nic;
        this.email = email;
        this.phoneNo = phoneNo;
        this.dermatologist = dermatologist;
        this.treatmentType = treatmentType;
        this.treatmentPrice = treatmentPrice;
        this.tax = tax;
        this.totalPrice = totalPrice;
        this.appointmentDate = appointmentDate;
        this.selectedTimeSlot = selectedTimeSlot;
        this.registrationFee = registrationFee;
        this.paid = paid;
    }

    // Getter and Setter Methods
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public String getNic() {
        return nic;
    }
    public void setNic(String nic) {
        this.nic = nic;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public int getPhoneNo() {
        return phoneNo;
    }
    public void setPhoneNo(int phoneNo) {
        this.phoneNo = phoneNo;
    }
    public Dermatologist getDermatologist() {
        return dermatologist;
    }
    public void setDermatologist(Dermatologist dermatologist) {
        this.dermatologist = dermatologist;
    }
    public TreatmentType getTreatmentType() {
        return treatmentType;
    }
    public void setTreatmentType(TreatmentType treatmentType) {
        this.treatmentType = treatmentType;
        this.treatmentPrice = treatmentType != null ? treatmentType.getPrice() : 0.0;
    }
    public double getTreatmentPrice() {
        return treatmentPrice;
    }
    public void setTreatmentPrice(double treatmentPrice) {
        this.treatmentPrice = treatmentPrice;
    }
    public double getTax() {
        return tax;
    }
    public void setTax(double tax) {
        this.tax = tax;
    }
    public double getTotalPrice() {
        return totalPrice;
    }
    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
    public LocalDate getAppointmentDate() {
        return appointmentDate;
    }
    public void setAppointmentDate(LocalDate appointmentDate) {
        this.appointmentDate = appointmentDate;
    }
    public Schedule getSelectedTimeSlot() {
        return selectedTimeSlot;
    }
    public void setSelectedTimeSlot(Schedule selectedTimeSlot) {
        this.selectedTimeSlot = selectedTimeSlot;
    }

    // Method to retrieve available time slots for the specific date
    public List<Schedule> getAvailableTimeSlotsForDate() {
        return AppointmentScheduler.getAvailableTimeSlots(appointmentDate);
    }

    // Check if a specific time slot is available
    public boolean isTimeSlotAvailable(Schedule timeSlot) {
        return getAvailableTimeSlotsForDate().contains(timeSlot);
    }

    public double getRegistrationFee() {
        return registrationFee;
    }
    public void setRegistrationFee(double registrationFee) {
        this.registrationFee = registrationFee;
    }
    public boolean isPaid() {
        return paid;
    }
    public void setPaid(boolean paid) {
        this.paid = paid;
    }
}
