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

    private String mobile;
    
    private String alternateMobile;

    @Column(name = "password")
    private String password;
    
  @Column(unique = true)
    private String empId;

  @ManyToOne

private Roles roles;

}
