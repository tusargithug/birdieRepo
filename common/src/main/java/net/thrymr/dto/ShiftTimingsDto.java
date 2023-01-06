package net.thrymr.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.thrymr.enums.SlotShift;
import net.thrymr.model.Site;

import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
public class ShiftTimingsDto {
    private Long id;
    private SlotShift shiftName;
    private String shiftStatAt;
    private String shiftEndAt;
    private Boolean status;

    private Long siteId;
}
