package net.thrymr.controller;

import net.thrymr.dto.AppUserDto;
import net.thrymr.dto.ChangePasswordDto;
import net.thrymr.dto.LoginDto;
import net.thrymr.dto.response.LoginResponse;
import net.thrymr.service.LoginUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginUserController {
    @Autowired
    private LoginUserService loginUserService;

    @MutationMapping("signUpUser")
    public String signUpUser(@Argument (name = "input") AppUserDto request) {
        return loginUserService.signUpUser(request);
    }

    @MutationMapping("loginUser")
    public String loginUser(@Argument (name = "input") LoginDto request) {
        return loginUserService.loginUser(request);
    }

    @MutationMapping("changePassword")
    public String changePassword(@Argument (name = "input") ChangePasswordDto changePasswordDto) {
        return loginUserService.changePassword(changePasswordDto);
    }

    @MutationMapping("getOtp")
    public String getOtp(@Argument (name = "input") LoginDto request) {
        return loginUserService.getOtp(request);
    }

    @MutationMapping("validateOtp")
    public String validateOtp(@Argument (name = "input") LoginDto request) {
        return loginUserService.validateOtp(request);
    }

    @QueryMapping("logoutUser")
    public String logoutUser() {
        return loginUserService.logoutUser();
    }

    @QueryMapping("getLoggedInUser")
    public LoginResponse getLoggedInUser() {
        return loginUserService.getLoggedInUser();
    }
}
