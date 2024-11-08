package com.erskinclinic.app.Controller;

import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

import com.erskinclinic.app.Entity.Schedule;
import com.erskinclinic.app.Service.AppointmentScheduler;

@RestController
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*")
public class ScheduleController {

	@GetMapping("/app/available-time-slots")
	public List<Schedule> getTimeSlots(@RequestParam("date") String date) {
	    LocalDate localDate = LocalDate.parse(date);
	    return AppointmentScheduler.getAvailableTimeSlots(localDate);
	}
}
