package net.thrymr.dto;

import lombok.Getter;
import lombok.Setter;
import net.thrymr.enums.SlotShift;
import net.thrymr.enums.SlotStatus;
import net.thrymr.model.AppUser;
import net.thrymr.model.Counsellor;

import javax.persistence.*;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Date;

@Getter
@Setter
public class CounsellorSlotResponseDto {
    private LocalTime startTime;
    private LocalTime endTime;
    private Date slotDt;
    private SlotShift slotShift;
    private DayOfWeek days;
    private SlotStatus slotStatus;
}
