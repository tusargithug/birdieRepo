package net.thrymr.model;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.thrymr.enums.SlotShift;
import net.thrymr.enums.SlotStatus;
import javax.persistence.*;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "user_appointment")
@NoArgsConstructor
public class UserAppointment extends BaseEntity{
    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "end_time")
    private LocalTime endTime;

    @Column(name = "slot_date")
    private Date slotDate;

    @Enumerated(EnumType.STRING)
    private SlotShift slotShift;

    private DayOfWeek days;

    @Enumerated(EnumType.STRING)
    private SlotStatus slotStatus;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private AppUser appUser;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Counsellor counsellor;

    @Column(name = "is_current_appointment", columnDefinition = "boolean default false")
    private Boolean isCurrentAppointment = Boolean.FALSE;

//    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//    private CounsellorSlot counsellorSlot;
}