package com.erskinclinic.app.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.erskinclinic.app.DTO.ArAppointmentDTO;
import com.erskinclinic.app.DTO.ResponseDTO;
import com.erskinclinic.app.Entity.Schedule;
import com.erskinclinic.app.IService.IArAppoinmentService;
import com.erskinclinic.app.Service.AppointmentScheduler;

import java.time.LocalDate;
import java.util.List;


@RestController
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*")
@RequestMapping("/app/arAppoinmentController")


public class ArAppoinmentController {
	
	@Autowired
	IArAppoinmentService service;
	
	@GetMapping
	 public ResponseDTO getAllAppointments() {
	        try {
	            List<ArAppointmentDTO> allArAppointment = service.getAllArAppointment();
	            return new ResponseDTO(1, "Success", allArAppointment);
	        } catch (Exception ex) {
	            return new ResponseDTO(0, ex.getMessage().toString(), null);
	        }
	    }

	//Add New Appointment
	@PostMapping
	public ResponseDTO saveArAppointment(@RequestBody ArAppointmentDTO arAppointmentDTO) {

		try {
			
	        
	        LocalDate appointmentDate = arAppointmentDTO.getAppointmentDate();
	        List<Schedule> availableSlots = AppointmentScheduler.getAvailableTimeSlots(appointmentDate);
	        
	        if (!availableSlots.isEmpty()) {
	        	Schedule selectedTimeSlot = arAppointmentDTO.getSelectedTimeSlot(); 
	            arAppointmentDTO.setSelectedTimeSlot(selectedTimeSlot);
	        } else {
	            return new ResponseDTO(0, "No available time slots for the selected date", null); 
	        }
	        
	        if (service.isAppointmentExists(
	                arAppointmentDTO.getAppointmentDate(),
	                arAppointmentDTO.getDermatologist(),
	                arAppointmentDTO.getSelectedTimeSlot().getTimeSlot()
	        )) {
	            return new ResponseDTO(0, "The dermatologist already has an appointment for this date and time.", null);
	        }

	        ArAppointmentDTO reply = service.addArAppointment(arAppointmentDTO);
	        return new ResponseDTO(1, "Data Added Sucessfully", reply);
	    } catch (Exception ex) {
	        return new ResponseDTO(0, ex.getMessage().toString(), null);
	    }
	}

	//Update Appointment By ID
	@PutMapping("/{id}")
	public ResponseDTO editArAppointment(@PathVariable("id") int id, @RequestBody ArAppointmentDTO arAppointmentDTO) {

		try {
	        
	        if (service.isAppointmentExists(
	                arAppointmentDTO.getAppointmentDate(),
	                arAppointmentDTO.getDermatologist(),
	                arAppointmentDTO.getSelectedTimeSlot().getTimeSlot()
	        )) {
	            return new ResponseDTO(0, "The dermatologist already has an appointment for this date and time.", null);
	        }

			ArAppointmentDTO reply = service.editArAppointment(id, arAppointmentDTO);
			return new ResponseDTO(1, "Data Updated Sucessfully", reply);
		} catch (Exception ex) {

			return new ResponseDTO(0, ex.getMessage().toString(), null);
		}

	}

	//Delete Appointment By ID
	@DeleteMapping("/{id}")
	public ResponseDTO deleteArAppointment(@PathVariable("id") int id) {

		try {
			if (service.deleteAppoinment(id)) {
				return new ResponseDTO(1, "Successfully Deleted Appoinment", "");
			} else {
				return new ResponseDTO(1, "No Such an Appoinment", "");
			}
		}
		catch(Exception ex)
		{
			return new ResponseDTO(0, ex.getMessage().toString(), "");
		}


	}

	//Find Appointment By ID
	@GetMapping("/{id}")
	public ResponseDTO findAppointmentById(@PathVariable("id") int id) {

		try {
			ArAppointmentDTO arAppointmentById = service.getArAppointmentById(id);
			if (arAppointmentById != null) {
				return new ResponseDTO(1, "Success", arAppointmentById);
			} else {
				return new ResponseDTO(1, "No User",null);
			}	
		}
		catch(Exception ex)
		{
			return new ResponseDTO(0, "No User", null);
		}
		
	}
	
	
	//Find Appointment By Date
	@GetMapping("/date/{date}")
    public ResponseDTO findAppointmentsByDate(@PathVariable("date") String date) {
        try {
            // Parse the date string to LocalDate
            LocalDate appointmentDate = LocalDate.parse(date);
            List<ArAppointmentDTO> appointments = service.getArAppointmentsByDate(appointmentDate);
            
            if (!appointments.isEmpty()) {
                return new ResponseDTO(1, "Success", appointments);
            } else {
                return new ResponseDTO(1, "No appointments found for the given date", null);
            }
        } catch (Exception ex) {
            return new ResponseDTO(0, "Error retrieving appointments", null);
        }
    }
	
	//Find Appointment By Patient's Name
	@GetMapping("/search/{searchTerm}")
	public ResponseDTO findByFirstNameOrLastName(@PathVariable("searchTerm") String searchTerm) {
	    try {
	        List<ArAppointmentDTO> appointments = service.searchAppointmentsByName(searchTerm);

	        if (!appointments.isEmpty()) {
	            return new ResponseDTO(1, "Success", appointments);
	        } else {
	            return new ResponseDTO(1, "No appointments found", null);
	        }
	    } catch (Exception ex) {
	        return new ResponseDTO(0, "Error while fetching appointments", null);
	    }
	}

	 

}
