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

    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "end_time")
    private LocalTime endTime;

    @Column(name = "slot_date")
    private Date slotDate;

    @Enumerated(EnumType.STRING)
    @ElementCollection(targetClass = SlotShift.class)
    private List<SlotShift> slotShift;

    @ElementCollection(targetClass = DayOfWeek.class)
    private List<DayOfWeek> slotDays;

    @Enumerated(EnumType.STRING)
    private SlotStatus slotStatus;
    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private AppUser appUser;

    private Date fromDate;

    private Date toDate;

    private int AvailableSlots;

    private Boolean isBooked;

}
