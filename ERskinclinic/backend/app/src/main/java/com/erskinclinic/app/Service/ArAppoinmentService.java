package com.erskinclinic.app.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;

import java.time.LocalDateTime;




import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.erskinclinic.app.DTO.ArAppointmentDTO;
import com.erskinclinic.app.Entity.ArAppointment;
import com.erskinclinic.app.Entity.Schedule;
import com.erskinclinic.app.Enum.Dermatologist;
import com.erskinclinic.app.IRepository.IArAppointmentRepository;
import com.erskinclinic.app.IService.IArAppoinmentService;



@Service
public class ArAppoinmentService implements IArAppoinmentService {
	
	@Autowired
	IArAppointmentRepository repository;

	@Override

	public List<ArAppointmentDTO> getAllArAppointment() {
        try {
            List<ArAppointmentDTO> reply = new ArrayList<>();
            List<ArAppointment> recordList = repository.findAll();

            recordList.forEach(arAppointment -> {
                reply.add(new ArAppointmentDTO(
                		arAppointment.getId(),
                	    arAppointment.getFirstName(),
                	    arAppointment.getLastName(),
                	    arAppointment.getNic(),
                	    arAppointment.getEmail(),
                	    arAppointment.getPhoneNo(),
                	    arAppointment.getDermatologist(), 
                	    arAppointment.getTreatmentType(),  
                	    arAppointment.getTreatmentType().getPrice(), 
                	    arAppointment.getTax(),
                	    arAppointment.getTotalPrice(),
                	    arAppointment.getAppointmentDate(), 
                	    arAppointment.getSelectedTimeSlot(), 
                	    arAppointment.getRegistrationFee(),
                	    arAppointment.isPaid()
                ));
            });
            return reply;
        } catch (Exception ex) {
            throw ex;
        }
    }
	
	@Override
	public ArAppointmentDTO getArAppointmentById(int id) {
	    try {
	        ArAppointment arAppointment = repository.findById(id).orElse(null);

	        if (arAppointment != null) {
	            // Creating ArAppointmentDTO using the defined constructor
	            ArAppointmentDTO arAppointmentDTO = new ArAppointmentDTO(
	            		arAppointment.getId(),
                	    arAppointment.getFirstName(),
                	    arAppointment.getLastName(),
                	    arAppointment.getNic(),
                	    arAppointment.getEmail(),
                	    arAppointment.getPhoneNo(),
                	    arAppointment.getDermatologist(), 
                	    arAppointment.getTreatmentType(), 
                	    arAppointment.getTreatmentType().getPrice(), 
                	    arAppointment.getTax(),
                	    arAppointment.getTotalPrice(),
                	    arAppointment.getAppointmentDate(), 
                	    arAppointment.getSelectedTimeSlot(), 
                	    arAppointment.getRegistrationFee(),
                	    arAppointment.isPaid()
	            );

	            return arAppointmentDTO;
	        } else {
	            return null;
	        }
	    } catch (Exception ex) {
	        throw ex;
	    }
	}



	@Override
	public boolean deleteAppoinment(int id) {

		try {
			repository.delete(repository.findById(id).get());
			return true;
		} catch (NoSuchElementException e) {
			throw e;
		} catch (Exception ex) {
			throw ex;
		}
	}

	@Override
	public ArAppointmentDTO editArAppointment(int id, ArAppointmentDTO arAppointmentDTO) {
	    try {
	        ArAppointment arAppointment = repository.findById(id).orElse(null);

	        if (arAppointment != null) {
	            // Set the ID from the DTO
	            arAppointmentDTO.setId(id);

	            BeanUtils.copyProperties(arAppointmentDTO, arAppointment, "id");

	            // Update treatment price, tax, and total price based on the treatment type
	            if (arAppointmentDTO.getTreatmentType() != null) {
	                arAppointment.setTreatmentPrice(arAppointmentDTO.getTreatmentType().getPrice());
	                double subtotal = arAppointment.getTreatmentPrice() + arAppointment.getRegistrationFee();
	                arAppointment.setTax(subtotal * 0.025);
	                arAppointment.setTotalPrice(Math.round((subtotal + arAppointment.getTax()) * 10.0) / 10.0);
	            }

	            // Save the updated appointment entity
	            ArAppointment savedArAppointment = repository.save(arAppointment);

	            // Prepare the DTO to return
	            ArAppointmentDTO returnArAppointmentDTO = new ArAppointmentDTO();
	            BeanUtils.copyProperties(savedArAppointment, returnArAppointmentDTO);

	            return returnArAppointmentDTO;
	        } else {
	            return null; 
	        }
	    } catch (Exception ex) {
	        throw new RuntimeException("Error while updating appointment", ex);
	    }
	}



	@Override
	public ArAppointmentDTO addArAppointment(ArAppointmentDTO arAppointmentDTO) {
	    try {
	        ArAppointment arAppointment = new ArAppointment();
	        
	        BeanUtils.copyProperties(arAppointmentDTO, arAppointment);
	        
	        // Set the calculated treatment price, tax, and total price based on treatment type
	        if (arAppointmentDTO.getTreatmentType() != null) {
	            arAppointment.setTreatmentPrice(arAppointmentDTO.getTreatmentType().getPrice());
	            double subtotal = arAppointment.getTreatmentPrice() + arAppointment.getRegistrationFee();
	            arAppointment.setTax(subtotal * 0.025); 
	            arAppointment.setTotalPrice(Math.round((subtotal + arAppointment.getTax()) * 10.0) / 10.0);
	        }
	        if (arAppointmentDTO.getSelectedTimeSlot() == null) {
	            throw new IllegalArgumentException("Selected time slot cannot be null");
	        }
	        // Save the appointment entity and return the saved entity as DTO
	        ArAppointment savedArAppointment = repository.save(arAppointment);
	        
	     // Prepare the DTO to return
	        ArAppointmentDTO returnArAppointmentDTO = new ArAppointmentDTO();
	        BeanUtils.copyProperties(savedArAppointment, returnArAppointmentDTO);
	        
	        return returnArAppointmentDTO;
	    } catch (Exception ex) {
	        throw new RuntimeException( ex);
	    }
	}

	
	@Override
	public List<ArAppointmentDTO> getArAppointmentsByDate(LocalDate appointmentDate) {
	    try {
	        List<ArAppointment> arAppointments = repository.findByAppointmentDate(appointmentDate);

	        List<ArAppointmentDTO> arAppointmentDTOs = arAppointments.stream()
	            .map(arAppointment -> {
	                ArAppointmentDTO dto = new ArAppointmentDTO();

	                BeanUtils.copyProperties(arAppointment, dto);
	               
	                dto.setAppointmentDate(arAppointment.getAppointmentDate());
	                dto.setSelectedTimeSlot(arAppointment.getSelectedTimeSlot());
	                
	                return dto;
	            })
	            .collect(Collectors.toList());

	        return arAppointmentDTOs;

	    } catch (Exception ex) {
	        throw new RuntimeException("Error while fetching appointments", ex);
	    }
	}
	
	@Override
	public List<ArAppointmentDTO> searchAppointmentsByName(String name) {
        try {
          
            List<ArAppointment> appointmentsByFirstName = repository.findByFirstNameContainingIgnoreCase(name);
            List<ArAppointment> appointmentsByLastName = repository.findByLastNameContainingIgnoreCase(name);

            List<ArAppointment> combinedResults = new ArrayList<>(appointmentsByFirstName);
            combinedResults.addAll(appointmentsByLastName.stream()
                .filter(appt -> !appointmentsByFirstName.contains(appt))
                .collect(Collectors.toList()));

            return combinedResults.stream().map(arAppointment -> {
                ArAppointmentDTO dto = new ArAppointmentDTO();
                BeanUtils.copyProperties(arAppointment, dto);
                return dto;
            }).collect(Collectors.toList());
        } catch (Exception ex) {
            throw new RuntimeException("Error while searching for appointments by name", ex);
        }
    }
	
	@Override 
    public boolean isAppointmentExists(LocalDate appointmentDate, Dermatologist dermatologist, String timeSlot) {
        Optional<ArAppointment> existingAppointment = repository.findByAppointmentDateAndDermatologistAndSelectedTimeSlot(
        		appointmentDate, dermatologist, new Schedule(timeSlot));
        return existingAppointment.isPresent();
    }

}

