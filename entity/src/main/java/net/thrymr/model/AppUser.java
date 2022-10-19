package net.thrymr.model;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.thrymr.enums.Roles;
import net.thrymr.model.master.MtRoles;

import javax.persistence.*;



@Setter
@Getter
@Entity
@NoArgsConstructor
@Table(name = "app_user")
public class AppUser extends BaseEntity {

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(unique = true)
    private String email;

    @Column(name = "user_name",unique = true)
    private String userName;

    private String mobile;

    @Column(name = "alternate_mobile")
    private String alternateMobile;

    @Column(name = "password")
    private String password;
    
    @Column(name = "emp_id",unique = true)
    private String empId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    private MtRoles mtRoles;

    @Column(name="user_role")
    @Enumerated(EnumType.STRING)
    private Roles roles;

}
