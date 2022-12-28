package net.thrymr.service;

import net.thrymr.dto.AppUserDto;
import net.thrymr.dto.ChangePasswordDto;
import net.thrymr.dto.LoginDto;
import net.thrymr.dto.response.LoginResponse;
import net.thrymr.model.AppUser;

public interface LoginUserService {

    String signUpUser(AppUserDto request);

    String loginUser(LoginDto request);

    String changePassword(ChangePasswordDto changePasswordDto);

    String getOtp(LoginDto request);

    String validateOtp(LoginDto request);

    String logoutUser();

    LoginResponse getLoggedInUser();
}