package net.thrymr.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.thrymr.enums.SlotShift;
import org.apache.commons.lang3.builder.ToStringExclude;

import javax.persistence.*;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class ShiftTimings extends BaseEntity {
    @Column(name = "shift_name")
    @Enumerated(EnumType.STRING)
    private SlotShift shiftName;
    @OneToOne(cascade = CascadeType.ALL)
    private Site site;
    private String shiftStatAt;
    private String shiftEndAt;
    @OneToOne(cascade=CascadeType.ALL)
    private Team team;
}
