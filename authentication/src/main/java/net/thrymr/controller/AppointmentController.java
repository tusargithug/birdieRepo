package net.thrymr.controller;

import net.thrymr.dto.AppointmentDto;
import net.thrymr.dto.slotRequest.TimeSlotDto;
import net.thrymr.model.UserAppointment;
import net.thrymr.services.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.List;

@RestController
public class AppointmentController {

    @Autowired
    AppointmentService appointmentService;

    @MutationMapping("createAppointment")
    public String createAppointment(@Argument(name = "input") TimeSlotDto request){
        return appointmentService.createAppointment(request);
    }
    @MutationMapping("rescheduledUserAppointment")
    public String rescheduledUserAppointment(@Argument(name = "input") TimeSlotDto request) throws ParseException {
        return appointmentService.rescheduledUserAppointment(request);
    }
    @QueryMapping("getAppointmentById")
    public UserAppointment getAppointmentById(@Argument Long id){
        return appointmentService.getAppointmentById(id);
    }
    @QueryMapping("getAllAppointment")
    public List<UserAppointment> getAllAppointment(){
        return appointmentService.getAllAppointment();
    }

}
