package net.thrymr.model;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.thrymr.enums.Alerts;
import net.thrymr.enums.Gender;
import net.thrymr.enums.Roles;
import javax.persistence.*;
import javax.validation.constraints.Size;
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

    @Column(name = "emp_id", unique = true)
    private String empId;

    @Column(name = "user_name", unique = true)
    private String userName;

    private String countryCode;

    @Column(name = "mobile_number", unique = true)
    @Size(min = 10, max = 10)
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
    private Alerts alerts;

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
