package net.thrymr.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.thrymr.enums.SlotShift;

@Getter
@Setter
@NoArgsConstructor
public class ShiftTimingsDto {
    private Long id;
    private SlotShift shiftName;
    private SiteDto site;
    private String shiftStatAt;
    private String shiftEndAt;
    private Long TeamId;
    private Long SiteId;
    private Boolean status;
    private Long CounsellorId;
}
