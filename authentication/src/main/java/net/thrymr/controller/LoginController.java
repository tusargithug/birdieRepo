package net.thrymr.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.thrymr.dto.AppUserDto;
import net.thrymr.dto.ChangePasswordDto;
import net.thrymr.dto.LoginDto;
import net.thrymr.service.LoginService;
import net.thrymr.utils.ApiResponse;


@RestController
@RequestMapping("/api/v1")

public class LoginController {
    private final Logger logger = LoggerFactory.getLogger( LoginController.class );

    @Autowired
    private LoginService loginService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse> signUpUser(@RequestBody AppUserDto appUserDto){
        logger.info( "Signup Service Started" );
        ApiResponse apiResponse = loginService.signUpUser(appUserDto);
        logger.info( "Signup Service Completed" );
        return new ResponseEntity<>( apiResponse, apiResponse.getStatus() );
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> loginUser(@RequestBody LoginDto loginDto){
        logger.info( "Login Service Started" );
        ApiResponse apiResponse=loginService.loginUser(loginDto);
        logger.info( "Login Service Completed" );
        return new ResponseEntity<>( apiResponse, apiResponse.getStatus());

    }

    @PostMapping("/change-password")
    public ResponseEntity<ApiResponse> changePassword(@RequestBody ChangePasswordDto changePasswordDto ){
        logger.info( "Change Password Service Started" );
        ApiResponse apiResponse = loginService.changePassword(changePasswordDto);
        logger.info( "Change Password Service Completed" );
        return new ResponseEntity<>( apiResponse, apiResponse.getStatus());
    }

    @PostMapping("/get-otp")
    public ResponseEntity<ApiResponse> getOtp(@RequestBody LoginDto request) {
        logger.info( "Get Otp Service Started" );
        ApiResponse apiResponse = loginService.getOtp( request );
        logger.info( "Get Otp Service Completed" );
        return new ResponseEntity<>( apiResponse, apiResponse.getStatus() );
    }

    @GetMapping("/logout")
    public ApiResponse logoutUser(){
        return loginService.logoutUser();
    }

    @GetMapping("/logged-in-user")
    public ApiResponse getLoggedInUser(){
        return loginService.getLoggedInUser();
    }

    @PostMapping("/update-user")
    public ApiResponse updateUser(@RequestBody AppUserDto appUserDto){
        return loginService.updateUser(appUserDto);
    }
}
