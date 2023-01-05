package net.thrymr.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.thrymr.enums.SlotShift;

import javax.persistence.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class ShiftTimings extends BaseEntity {
    @Column(name = "shift_name")
    @Enumerated(EnumType.STRING)
    private SlotShift shiftName;
    private LocalTime shiftStatAt;
    private LocalTime shiftEndAt;
    private String shiftTimings;

    @OneToOne(cascade = CascadeType.ALL)
    private Site site;

   /*
    @OneToMany(cascade=CascadeType.ALL,fetch = FetchType.LAZY,mappedBy = "shiftTimings")
    private List<Team> team;
    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private Counsellor counsellors;*/
}
