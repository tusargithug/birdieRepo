package net.thrymr.service.impl;

import net.thrymr.dto.AppUserDto;
import net.thrymr.dto.ChangePasswordDto;
import net.thrymr.dto.LoginDto;
import net.thrymr.dto.response.LoginResponse;
import net.thrymr.dto.response.SendMessageDto;
import net.thrymr.model.AppUser;
import net.thrymr.repository.AppUserRepo;
import net.thrymr.repository.RoleRepository;
import net.thrymr.security.JwtUtil;
import net.thrymr.security.LoggedInUser;
import net.thrymr.service.LoginService;
import net.thrymr.utils.ApiResponse;
import net.thrymr.utils.BaseCommonUtil;
import net.thrymr.utils.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    AppUserRepo appUserRepo;

    @Autowired
    Environment environment;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtUtil jwtUtils;

    @Override
    public ApiResponse signUpUser(AppUserDto appUserDto) {
        if (Validator.isValid(appUserDto.getEmail())) {
            Optional<AppUser> optionalAppUser = appUserRepo.findByEmail(appUserDto.getEmail());
            if (optionalAppUser.isPresent()) {
                return new ApiResponse(HttpStatus.BAD_REQUEST, environment.getProperty("USER_EXIST_EMAIL"), appUserDto.getEmail());
            }
        }
        AppUser appUser = dtoToModel(appUserDto);
        appUserRepo.save(appUser);
        return new ApiResponse(HttpStatus.OK, "SignUp Successfully Completed");
    }
    private AppUser dtoToModel(AppUserDto request) {
        AppUser appUser = new AppUser();
        if (request.getId() != null && request.getId() > 0) {
            appUser = appUserRepo.findById(request.getId()).orElse(new AppUser());
        }
        appUser.setFirstName(request.getFirstName());
        appUser.setLastName(request.getLastName());
        appUser.setMobile(request.getMobile());
        appUser.setEmail(request.getEmail());
        appUser.setUserName(request.getUserName());
        appUser.setPassword(new BCryptPasswordEncoder().encode(request.getPassword()));
        appUser.setAlternateMobile(request.getAlternateMobile());
        appUser.setRoles(request.getRoles());

        return appUser;
    }

    @Override
    public ApiResponse getOtp(LoginDto request) {
        if (request != null && request.getMobile() != null) {
            Optional<AppUser> optionalAppUser = appUserRepo.findByMobileAndIsActiveAndIsDeleted(request.getMobile(), Boolean.TRUE, Boolean.FALSE);
            if (optionalAppUser.isEmpty()) {
                return new ApiResponse(HttpStatus.BAD_REQUEST, "Please Signup ");
            } else {
                SendMessageDto dto = new SendMessageDto();
                dto.setOtp(BaseCommonUtil.generateOTP(6));
                dto.setUserName(optionalAppUser.get().getUserName());
                return new ApiResponse(HttpStatus.OK, "OTP Generated Successfully", dto.getOtp());
            }
        } else {
            return new ApiResponse(HttpStatus.BAD_REQUEST, "Please provide Valid Mobile number");
        }
    }

    @Override
    public ApiResponse loginUser(LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        LoggedInUser loggedInUser = (LoggedInUser) authentication.getPrincipal();
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setId(loggedInUser.getId());
        loginResponse.setFirstName(loggedInUser.getFirstName());
        loginResponse.setLastName(loggedInUser.getLastName());
        loginResponse.setContactNumber(loggedInUser.getContactNumber());
        loginResponse.setEmail(loggedInUser.getEmail());
        loginResponse.setUserRole(loggedInUser.getRole().getName());

        loginResponse.setAccessToken(jwt);
        return new ApiResponse(HttpStatus.OK, "USER LOGIN SUCCESSFULLY", loginResponse);
    }

    @Override
    public ApiResponse changePassword(ChangePasswordDto changePasswordDto) {
        ApiResponse apiResponse = validateChangePasswordRequest(changePasswordDto);
        if (Validator.isObjectValid(apiResponse)) {
            return apiResponse;
        }
        LoggedInUser loggedInUser = getCurrentUserLogin();
        if (loggedInUser != null) {
            Optional<AppUser> optionalAppUser = appUserRepo.findById(loggedInUser.getId());
            if (optionalAppUser.isPresent()) {
                AppUser appUser = optionalAppUser.get();
                appUser.setPassword(new BCryptPasswordEncoder().encode(changePasswordDto.getNewPassword()));
                appUserRepo.save(appUser);
                return new ApiResponse(HttpStatus.OK, "Password Changed Successfully");
            } else {
                return new ApiResponse(HttpStatus.BAD_REQUEST, "Logged in User Details Missing,please do a login again");
            }
        } else {
            return new ApiResponse(HttpStatus.BAD_REQUEST, "Please login to do this action");
        }
    }

    private ApiResponse validateChangePasswordRequest(ChangePasswordDto changePasswordDto) {
        if (!Validator.isObjectValid(changePasswordDto)) {
            return new ApiResponse(HttpStatus.BAD_REQUEST, "Please Provide Valid Request");
        }
        if (!Validator.isValid(changePasswordDto.getNewPassword())) {
            return new ApiResponse(HttpStatus.BAD_REQUEST, "Please Provide New Password");
        }
        if (!Validator.isValid(changePasswordDto.getConfirmPassword())) {
            return new ApiResponse(HttpStatus.BAD_REQUEST, "Please Confirm Password");
        }
        if (!changePasswordDto.getNewPassword().equals(changePasswordDto.getConfirmPassword())) {
            return new ApiResponse(HttpStatus.BAD_REQUEST, "Password and Confirm Password Should be same");
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
    public ApiResponse logoutUser() {
        return new ApiResponse(HttpStatus.OK, "User Logged out Successfully");
    }

    @Override
    public ApiResponse getLoggedInUser() {
        Object loggedInUserResponse = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (loggedInUserResponse != null && !loggedInUserResponse.equals("anonymousUser")) {
            System.out.println(loggedInUserResponse);
            LoginResponse loginResponse = new LoginResponse();
            LoggedInUser loggedInUser = (LoggedInUser) loggedInUserResponse;
            loginResponse.setId(loggedInUser.getId());
            loginResponse.setFirstName(loggedInUser.getFirstName());
            loginResponse.setLastName(loggedInUser.getLastName());

            loginResponse.setContactNumber(loggedInUser.getUsername());
            loginResponse.setEmail(loggedInUser.getEmail());
            //loginResponse.setUserRole(loggedInUser.getUserRole().name());
            return new ApiResponse(HttpStatus.OK, "Logged in User Details Found Successfully", loginResponse);
        } else {
            return new ApiResponse(HttpStatus.BAD_REQUEST, "Please login !!", null);
        }
    }

    @Override
    public ApiResponse updateUser(AppUserDto appUserDto) {
        return null;
    }
}

