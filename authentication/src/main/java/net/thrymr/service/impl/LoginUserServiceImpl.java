package net.thrymr.service.impl;

import net.thrymr.dto.AppUserDto;
import net.thrymr.dto.ChangePasswordDto;
import net.thrymr.dto.LoginDto;
import net.thrymr.dto.response.LoginResponse;
import net.thrymr.model.AppUser;
import net.thrymr.repository.AppUserRepo;
import net.thrymr.security.JwtUtil;
import net.thrymr.security.LoggedInUser;
import net.thrymr.service.LoginUserService;
import net.thrymr.utils.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class LoginUserServiceImpl implements LoginUserService {
    @Autowired
    AppUserRepo appUserRepo;

    @Autowired
    Environment environment;

    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtUtil jwtUtil;

    @Override
    public String signUpUser(AppUserDto request) {
        return null;
    }

    @Override
    public String loginUser(LoginDto request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmpId(),request.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtil.generateJwtToken(authentication);
        System.out.println("JWT::"+jwt);
        LoggedInUser loggedInUser = (LoggedInUser) authentication.getPrincipal();
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setId(loggedInUser.getId());
        loginResponse.setUserName(loggedInUser.getUsername());
        loginResponse.setContactNumber(loggedInUser.getContactNumber());
        loginResponse.setEmail(loggedInUser.getEmail());
        loginResponse.setUserRole(loggedInUser.getRole());
        loginResponse.setAccessToken(jwt);
        return "Login successfully";
    }

    @Override
    public String changePassword(ChangePasswordDto changePasswordDto) {
        String response = validateChangePasswordRequest(changePasswordDto);
        if (Validator.isObjectValid(response)) {
            return response;
        }
        LoggedInUser loggedInUser = getCurrentUserLogin();
        if (loggedInUser != null) {
            Optional<AppUser> optionalAppUser = appUserRepo.findById(loggedInUser.getId());
            if (optionalAppUser.isPresent()) {
                AppUser appUser = optionalAppUser.get();
                appUser.setPassword(new BCryptPasswordEncoder().encode(changePasswordDto.getNewPassword()));
                appUserRepo.save(appUser);
                return "Password Changed Successfully";
            } else {
                return "Logged in User Details Missing,please do a login again";
            }
        } else {
            return "Please login to do this action";
        }
    }

    private String validateChangePasswordRequest(ChangePasswordDto changePasswordDto) {
        if (!Validator.isObjectValid(changePasswordDto)) {
            return "Please Provide Valid Request";
        }
        if (!Validator.isValid(changePasswordDto.getNewPassword())) {
            return  "Please Provide New Password";
        }
        if (!Validator.isValid(changePasswordDto.getConfirmPassword())) {
            return "Please Confirm Password";
        }
        if (!changePasswordDto.getNewPassword().equals(changePasswordDto.getConfirmPassword())) {
            return "Password and Confirm Password Should be same";
        }
        return null;
    }
    public static LoggedInUser getCurrentUserLogin() {
        LoggedInUser loggedInUser = null;
        Object loggedInUserResponse = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (loggedInUserResponse != null && !loggedInUserResponse.equals("anonymousUser")) {
            loggedInUser = (LoggedInUser) loggedInUserResponse;
        }
        return loggedInUser;
    }

    @Override
    public String getOtp(LoginDto request) {
        return null;
    }

    @Override
    public String validateOtp(LoginDto request) {
        return null;
    }

    @Override
    public String logoutUser() {
        return "User Logged out Successfully";
    }

    @Override
    public LoginResponse getLoggedInUser() {
        Object loggedInUserResponse = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (loggedInUserResponse != null && !loggedInUserResponse.equals("anonymousUser")) {
            System.out.println(loggedInUserResponse);
            LoginResponse loginResponse = new LoginResponse();
            LoggedInUser loggedInUser = (LoggedInUser) loggedInUserResponse;
            loginResponse.setId(loggedInUser.getId());
            loginResponse.setUserName(loggedInUser.getUsername());
            loginResponse.setContactNumber(loggedInUser.getUsername());
            loginResponse.setEmail(loggedInUser.getEmail());
            loginResponse.setUserRole(loggedInUser.getRole());
            return loginResponse;
        } else {
            return new LoginResponse();
        }
    }

}