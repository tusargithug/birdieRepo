package net.thrymr.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class LoginResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String contactNumber;
    private String accessToken;
    private String userRole;

    private String email;


}
