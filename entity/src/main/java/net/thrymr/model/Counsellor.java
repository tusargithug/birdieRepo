package net.thrymr.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.thrymr.enums.Gender;
import net.thrymr.enums.Roles;
import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class Counsellor extends BaseEntity {

    private String counsellorName;

    @Column(unique = true)
    private String empId;

    @Column(unique = true)
    private String emailId;

    private String countryCode;

    @Column(unique = true)
    @Size(min = 10, max = 10)
    private String mobileNumber;

    @Enumerated(EnumType.STRING)
    private Roles designation;

    private LocalTime shiftStartAt;

    private LocalTime shiftEndAt;

    private String shiftTimings;

    @OneToMany(mappedBy = "counsellor",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<CounsellorSlot>counsellorSlots=new ArrayList<>();

    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private Site site;

    @Column(name = "educational_details")
    @ElementCollection(targetClass = String.class)
    private List<String> educationalDetails=new ArrayList<>();

    @ElementCollection(targetClass = String.class)
    private List<String> languages=new ArrayList<>();

    @Column(columnDefinition = "TEXT")
    private String bio;

    @Enumerated(EnumType.STRING)
    private Gender gender;
}
