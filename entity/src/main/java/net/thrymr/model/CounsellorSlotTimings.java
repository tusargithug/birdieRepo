package net.thrymr.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.thrymr.enums.SlotShift;
import net.thrymr.enums.SlotStatus;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class CounsellorSlotTimings extends BaseEntity{
    @ManyToOne
    private Counsellor counsellor;

    private LocalTime slotTiming;

    @Enumerated(EnumType.STRING)
    private SlotShift slotShift;

    @Enumerated(EnumType.STRING)
    private SlotStatus slotStatus;

    private Date slotDate = new Date();

    @Enumerated(EnumType.STRING)
    private DayOfWeek slotDay;
}
