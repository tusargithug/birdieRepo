package net.thrymr.services;

import net.thrymr.dto.AppointmentDto;
import net.thrymr.dto.slotRequest.TimeSlotDto;
import net.thrymr.model.UserAppointment;

import java.text.ParseException;
import java.util.List;

public interface AppointmentService {
    String createAppointment(TimeSlotDto appointmentDto);

    UserAppointment getAppointmentById(Long id);

    String rescheduledUserAppointment(TimeSlotDto request) throws ParseException;

    List<UserAppointment> getAllAppointment();
}