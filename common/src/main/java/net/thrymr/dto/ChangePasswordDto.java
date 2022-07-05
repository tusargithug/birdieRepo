package net.thrymr.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class ChangePasswordDto {
    private String newPassword;
    private String confirmPassword;
}
