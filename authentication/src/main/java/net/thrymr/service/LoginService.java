package net.thrymr.service;

import net.thrymr.dto.AppUserDto;
import net.thrymr.dto.ChangePasswordDto;
import net.thrymr.dto.LoginDto;
import net.thrymr.utils.ApiResponse;

public interface LoginService {
    ApiResponse signUpUser(AppUserDto appUserDto);

    ApiResponse loginUser(LoginDto loginDto);

    ApiResponse changePassword(ChangePasswordDto changePasswordDto);

    ApiResponse logoutUser();

    ApiResponse getLoggedInUser();

    ApiResponse updateUser(AppUserDto appUserDto);

    ApiResponse getOtp(LoginDto request);
}
