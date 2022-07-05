package net.thrymr.model;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;



@Setter
@Getter
@Entity
@NoArgsConstructor
@Table(name = "app_user")
public class AppUser extends BaseEntity {

    private String firstName;

    private String lastName;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String userName;

    @Column(unique = true)
    private String mobile;
    
    private String alternateMobile;

//    private String occupation;
//
//    @Column(name = "primary_branch")
//    private Long primaryBranch;

    @Column(name = "password")
    private String password;

}
