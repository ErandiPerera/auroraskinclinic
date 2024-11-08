package com.erskinclinic.app.IRepository;

import java.time.DayOfWeek;
import java.time.LocalDate;

import java.time.LocalDateTime;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import com.erskinclinic.app.DTO.ArAppointmentDTO;
import com.erskinclinic.app.Entity.ArAppointment;
import com.erskinclinic.app.Entity.Schedule;
import com.erskinclinic.app.Enum.Dermatologist;

@Repository
public interface IArAppointmentRepository extends JpaRepository<ArAppointment, Integer> {

	List<ArAppointment> findByAppointmentDate(LocalDate appointmentDate);

	Optional<ArAppointment> findByAppointmentDateAndDermatologistAndSelectedTimeSlot(
            LocalDate appointmentDate, Dermatologist dermatologist, Schedule selectedTimeSlot);
    List<ArAppointment> findByFirstNameContainingIgnoreCase(String firstName);

    List<ArAppointment> findByLastNameContainingIgnoreCase(String lastName);

}
