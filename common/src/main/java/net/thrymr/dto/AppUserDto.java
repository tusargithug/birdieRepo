package net.thrymr.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AppUserDto {

    private Long id;

    private String firstName;

    private String lastName;
    
    private String email;

    private String userName;

    private String mobile;
    
    private String alternateMobile;

    private String password;

    private String searchKey;

    private Boolean isActive;

      private RolesDto rolesDto;

    private String empId;

    private LocalDate date;
}
