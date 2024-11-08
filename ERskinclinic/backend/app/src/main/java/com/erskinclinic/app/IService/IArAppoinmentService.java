package com.erskinclinic.app.IService;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;

import java.util.List;


import com.erskinclinic.app.DTO.ArAppointmentDTO;
import com.erskinclinic.app.Enum.Dermatologist;

public interface IArAppoinmentService {

	List<ArAppointmentDTO> getAllArAppointment();
	ArAppointmentDTO getArAppointmentById(int id);
	List<ArAppointmentDTO> getArAppointmentsByDate(LocalDate date);
	boolean deleteAppoinment(int id);
	ArAppointmentDTO editArAppointment(int id, ArAppointmentDTO arAppointmentDTO);
	ArAppointmentDTO addArAppointment(ArAppointmentDTO arAppointmentDTO);
	boolean isAppointmentExists(LocalDate appointmentDate, Dermatologist dermatologist, String timeSlot);
	List<ArAppointmentDTO> searchAppointmentsByName(String name);
}
