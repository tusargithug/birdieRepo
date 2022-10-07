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
    private AppUser user;

    private String designation;

    @OneToMany(mappedBy = "counsellor",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<CounsellorSlot>counsellorSlots=new ArrayList<>();
   // branch
    private String site;

    @Column(name = "educational_details")
    private String educationalDetails;

    @ElementCollection(targetClass = String.class)
    private List<String> languages=new ArrayList<>();

    @Column(name = "shift_timings")
    private String shiftTimings;

//    @Column(name = "shift_allowance")
//    private boolean shiftAllowance = false;
}
