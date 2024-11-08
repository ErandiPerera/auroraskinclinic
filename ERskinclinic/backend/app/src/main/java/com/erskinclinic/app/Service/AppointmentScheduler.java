package com.erskinclinic.app.Service;



import com.erskinclinic.app.Entity.Schedule;

import java.time.LocalTime;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppointmentScheduler {

    // Method to generate a weekly schedule with predefined time slots for each day
    public static Map<DayOfWeek, List<Schedule>> generateWeeklySchedule() {
        Map<DayOfWeek, List<Schedule>> weeklySchedule = new HashMap<>();

        // Define time slots for each working day
        weeklySchedule.put(DayOfWeek.MONDAY, generateTimeSlots(LocalTime.of(10, 0), LocalTime.of(13, 0)));
        weeklySchedule.put(DayOfWeek.WEDNESDAY, generateTimeSlots(LocalTime.of(14, 0), LocalTime.of(17, 0)));
        weeklySchedule.put(DayOfWeek.FRIDAY, generateTimeSlots(LocalTime.of(16, 0), LocalTime.of(20, 0)));
        weeklySchedule.put(DayOfWeek.SATURDAY, generateTimeSlots(LocalTime.of(9, 0), LocalTime.of(13, 0)));

        return weeklySchedule;
    }

    // Method to generate 15-minute time slots within a specified time range
    private static List<Schedule> generateTimeSlots(LocalTime startTime, LocalTime endTime) {
        List<Schedule> timeSlots = new ArrayList<>();
        LocalTime slotTime = startTime;

        while (slotTime.isBefore(endTime)) {
            timeSlots.add(new Schedule(slotTime, slotTime.plusMinutes(15)));
            slotTime = slotTime.plusMinutes(15);
        }

        return timeSlots;
    }

    // Retrieve available time slots for a specific date
    public static List<Schedule> getAvailableTimeSlots(LocalDate appointmentDate) {
        DayOfWeek dayOfWeek = appointmentDate.getDayOfWeek();
        Map<DayOfWeek, List<Schedule>> weeklySchedule = generateWeeklySchedule();
        return weeklySchedule.getOrDefault(dayOfWeek, new ArrayList<>());
    }
    

}
