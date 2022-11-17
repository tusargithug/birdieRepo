package net.thrymr.model.master;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.thrymr.enums.SlotShift;
import net.thrymr.model.AppUser;
import net.thrymr.model.BaseEntity;

import javax.persistence.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class MtShiftTimings extends BaseEntity {
    @Column(name = "shift_name")
    @Enumerated(EnumType.STRING)
    private SlotShift shiftName;
    @OneToOne(cascade = CascadeType.ALL)
    private MtSite mtSite;
    private LocalTime shiftStatAt;
    private LocalTime shiftEndAt;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "mtShiftTimings")
    private MtTeam mtTeam;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "mtShiftTimings")
    private List<AppUser> appUser = new ArrayList<>();
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private MtCounsellor counsellors;
}
