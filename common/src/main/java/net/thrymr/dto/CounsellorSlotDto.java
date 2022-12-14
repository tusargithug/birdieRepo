package net.thrymr.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
public class CounsellorSlotDto {
    private Long counsellorId;
    private String startTime;
    private String endTime;
    private String slotDate;
    private List<SlotShift> slotShift;
    private List<DayOfWeek> slotDays;
    private String slotStatus;
    private Long appUserId;
    private String fromDate;
    private String toDate;
    private Boolean isActive;
    private Long counsellorSlotId;
    private Integer pageNumber;
    private Integer pageSize;
    private Long siteIdList;
    private Long vendorIdList;
    private String shiftTimingList;
}
