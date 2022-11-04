package net.thrymr.services;

import net.thrymr.dto.AppointmentDto;
import net.thrymr.model.UserAppointment;

import java.text.ParseException;

public interface AppointmentService {
    String createAppointment(AppointmentDto appointmentDto);

    UserAppointment getAppointmentByCounsellorSlotId(Long id);

    String rescheduledUserAppointment(AppointmentDto request) throws ParseException;

}