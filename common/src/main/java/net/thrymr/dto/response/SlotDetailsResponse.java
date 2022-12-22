package net.thrymr.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.thrymr.enums.SlotShift;
import net.thrymr.enums.SlotStatus;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class SlotDetailsResponse {
    private LocalTime slotTiming;
    private SlotShift slotShift;
    private SlotStatus slotStatus;
    private Date slotDate;
    private DayOfWeek slotDay;
}
