package net.thrymr.model.master;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.thrymr.model.AppUser;
import net.thrymr.model.BaseEntity;
import net.thrymr.model.CounsellorSlot;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class MtCounsellor extends BaseEntity {
    @OneToOne
    private AppUser appUser;
    @OneToOne
    private AppUser teamManager;
    @OneToMany(mappedBy = "mtCounsellor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CounsellorSlot> counsellorSlots = new ArrayList<>();
    @Column(name = "shift_timings")
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "counsellors")
    private List<MtShiftTimings> mtShiftTimings;
    @Column(name = "educational_details")
    @ElementCollection(targetClass = String.class)
    private List<String> educationalDetails = new ArrayList<>();
    @ElementCollection(targetClass = String.class)
    private List<String> languages = new ArrayList<>();
    @Column(columnDefinition = "TEXT")
    private String bio;
}
