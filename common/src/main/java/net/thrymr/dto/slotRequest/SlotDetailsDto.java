package net.thrymr.dto.slotRequest;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.thrymr.enums.SlotStatus;
import net.thrymr.model.AppUser;

import java.util.Date;


@Getter
@Setter
@NoArgsConstructor
public class SlotDetailsDto {
    private Long counsellorId;
    private String slotShift;
    private String startTime;
    private String endTime;
    private Integer duration;
    private String dayOfWeek;
    private String date;
    //Date range
    private String toDate;
    private String fromDate;
    private Long appUserId;
    private String slotStatus;
    private Boolean isActive;
}
