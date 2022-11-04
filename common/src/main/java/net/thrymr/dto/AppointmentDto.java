package net.thrymr.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.thrymr.dto.slotRequest.SlotDetailsDto;
import net.thrymr.enums.SlotShift;
import net.thrymr.enums.SlotStatus;
import net.thrymr.model.AppUser;
import net.thrymr.model.Counsellor;

import javax.persistence.*;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class AppointmentDto {
   private Long id;
    private Long appUserId;
    private LocalTime startTime;

    private LocalTime endTime;

    private Date slotDate;

    private String slotShift;

    private DayOfWeek days;

    private SlotStatus slotStatus;

    private AppUser appUser;

    private Counsellor counsellor;

    private Boolean isCurrentAppointment = Boolean.FALSE;

    private Long counsellorId;

    private List<SlotDetailsDto> slots;
}
