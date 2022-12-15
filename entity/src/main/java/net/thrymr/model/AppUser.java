package net.thrymr.model;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.thrymr.enums.Alerts;
import net.thrymr.enums.Gender;
import net.thrymr.enums.Roles;
import javax.persistence.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Setter
@Getter
@Entity
@NoArgsConstructor
@Table(name = "app_user")
public class AppUser extends BaseEntity {

    @Column(name = "emp_id")
    private String empId;

    @Column(name = "user_name")
    private String userName;

    private String countryCode;

    @Column(name = "mobile_number",unique = true)
    private String mobile;

    @Column(unique = true)
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "date_of_joining")
    private Date dateOfJoining;

    @Column(name = "user_role")
    @Enumerated(EnumType.STRING)
    private Roles roles;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Site site;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "appUser")
    private List<CounsellorSlot> counsellorSlotList = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @ElementCollection(targetClass = Alerts.class)
    private List<Alerts> alerts;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "shiftStartsAt")
    private LocalTime shiftStartAt;

    @Column(name = "shiftEndAt")
    private LocalTime shiftEndAt;

    @Column(name="shiftTimings")
    private String shiftTimings;

    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private FileEntity uploadPicture;

}
