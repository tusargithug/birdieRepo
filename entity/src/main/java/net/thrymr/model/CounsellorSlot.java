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

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "Counsellor_slot")
public class CounsellorSlot extends BaseEntity {


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "counsellor_slot")
    private Counsellor counsellor;

    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "end_time")
    private LocalTime endTime;

    @Column(name = "slot_date")
    private LocalDate slotDt;

    @Enumerated(EnumType.STRING)
    private SlotShift slotShift;



    private DayOfWeek days;

    private SlotStatus slotStatus;

//    private final ObjectMapper objectMapper = new ObjectMapper()
//            .registerModule(new ParameterNamesModule())
//            .registerModule(new Jdk8Module())
//            .registerModule(new JavaTimeModule());
//
//    CounsellorSlot input = objectMapper
//            .convertValue(dataFetchingEnvironment.getArgument("input"), CounsellorSlot.class);
}
