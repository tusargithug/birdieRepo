package net.thrymr.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class Counsellor extends BaseEntity {
    @OneToOne
    private AppUser appUser;
    @OneToOne
    private AppUser teamManager;
    @OneToMany(mappedBy = "counsellor",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<CounsellorSlot>counsellorSlots=new ArrayList<>();
    @Column(name = "shift_timings")
    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY,mappedBy = "counsellors")
    private List<ShiftTimings> shiftTimings;
    @Column(name = "educational_details")
    @ElementCollection(targetClass = String.class)
    private List<String> educationalDetails=new ArrayList<>();
    @ElementCollection(targetClass = String.class)
    private List<String> languages=new ArrayList<>();
    @Column(columnDefinition = "TEXT")
    private String bio;
}
