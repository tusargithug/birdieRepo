package net.thrymr.model;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.thrymr.enums.SlotShift;
import net.thrymr.enums.SlotStatus;

import javax.persistence.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "Counsellor_slot")
public class CounsellorSlot extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    private Counsellor counsellor;

    private Date fromDate;

    private Date toDate;
}
