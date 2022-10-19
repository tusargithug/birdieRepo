package net.thrymr.controller;



import net.thrymr.dto.AppUserDto;

import net.thrymr.dto.UserCourseDto;
import net.thrymr.enums.Roles;
import net.thrymr.model.AppUser;

import net.thrymr.repository.AppUserRepo;
import net.thrymr.services.AppUserService;
import net.thrymr.services.AssignmentService;
import net.thrymr.services.RoleService;
import net.thrymr.utils.ApiResponse;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.graphql.data.method.annotation.*;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

import java.util.List;



@RestController
//@RequestMapping("/api/v1/user")
public class AppUserController {
    private final Logger logger = LoggerFactory.getLogger(AppUserController.class);

    private final RoleService roleService;
    private final AssignmentService assignmentService;
    private final AppUserService appUserService;

    public AppUserController(RoleService roleService, AssignmentService assignmentService, AppUserService appUserService) {
        this.roleService = roleService;
        this.assignmentService = assignmentService;
        this.appUserService = appUserService;
    }

    @GetMapping("/roles")
    public ApiResponse getAllRoles() {
        logger.info("get all roles service started");
        ApiResponse apiResponse = roleService.getAllUserRoles();
        logger.info("get all roles service completed");
        return new ApiResponse(HttpStatus.OK, "All Roles", apiResponse);
    }


    //User attempt assignment

    @GetMapping("/get/all/assignments")
    public ApiResponse getAllAssignment() {
        logger.info("get all assignment service started");
        ApiResponse apiResponse = assignmentService.getAllAssignment();
        logger.info("get all assignment service completed");
        return new ApiResponse(HttpStatus.OK, "All Roles", apiResponse);
    }

    // get user by id
    @QueryMapping
    public AppUser getAppUserById(@Argument Long id) {
        return appUserService. getAppUserById(id);

    }

    // get all Users
    @QueryMapping("getAllAppUsers")
    public List<AppUser> getAllAppUsers() {
        return appUserService. getAllAppUsers();
    }

    @MutationMapping(name = "createAppUser")
    public String createAppUser(@Argument(name = "input") AppUserDto request) {
        return appUserService.createAppUser(request);
    }

    @MutationMapping(name = "updateAppUser")
    public String updateAppUser(@Argument(name = "input") AppUserDto request) {
        return appUserService.updateAppUser(request);

    }

    @MutationMapping(name = "deleteAppUserById")
    public String deleteAppUserById(@Argument Long id) {
        return appUserService.deleteAppUserById(id);

    }

    // user course update for logged in user
    @MutationMapping(name = "createUserCourse")
    public String createUserCourse(@Argument(name = "input") UserCourseDto request) throws ParseException {
        return appUserService.createUserCourse(request);
    }

    @QueryMapping(name="getAllEnumRoles")
    public List<Roles> getAllEnumRoles(){
        List<Roles> rolesList= appUserService.getAllEnumRoles();
        return rolesList;
    }
}
