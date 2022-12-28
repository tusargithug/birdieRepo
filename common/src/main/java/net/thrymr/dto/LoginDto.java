package net.thrymr.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@NoArgsConstructor
public class LoginDto {

    private String mobile;

    private String password;

    private String userName;

    private String otp;

    private String registeredToken;

    private String email;

    private String empId;
}