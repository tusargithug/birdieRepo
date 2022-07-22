package net.thrymr.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.thrymr.model.Roles;


@Setter
@Getter
@NoArgsConstructor
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

private Roles roles;
}
