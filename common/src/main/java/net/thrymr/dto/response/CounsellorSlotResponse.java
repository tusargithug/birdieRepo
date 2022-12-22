package net.thrymr.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.thrymr.enums.SlotShift;
import net.thrymr.enums.SlotStatus;
import net.thrymr.model.Counsellor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CounsellorSlotResponse {
    private Long id;
    private String counsellorName;
    private String vendorName;
    private String siteName;
    private String shiftTimings;
    private List<SlotDetailsResponse> slotDetailsResponses = new ArrayList<>();
}
