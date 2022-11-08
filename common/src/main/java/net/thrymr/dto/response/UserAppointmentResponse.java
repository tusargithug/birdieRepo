package net.thrymr.dto.response;

import lombok.Getter;
import lombok.Setter;
import net.thrymr.model.AppUser;

@Getter
@Setter
public class UserAppointmentResponse {
    private Long id;
    private Integer availableAppointmentCount;
    private Integer rescheduledAppointmentCount;
    private Integer canceledAppointmentCount;
    private Integer totalAppointmentsCount;
    private Integer blockedAppointmentCount;
    private AppUser appUser;
}
