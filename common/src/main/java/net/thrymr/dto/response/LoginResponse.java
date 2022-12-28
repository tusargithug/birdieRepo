package net.thrymr.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.thrymr.enums.Roles;


@Getter
@Setter
@NoArgsConstructor
public class LoginResponse {
    private Long id;
    private String userName;
    private String contactNumber;
    private String accessToken;
    private Roles userRole;
    private String email;
}
