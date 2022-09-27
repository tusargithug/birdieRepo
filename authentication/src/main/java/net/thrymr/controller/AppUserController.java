package net.thrymr.controller;

import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.thrymr.dto.AppUserDto;
import net.thrymr.model.AppUser;
import net.thrymr.repository.AppUserRepo;
import net.thrymr.services.AssignmentService;
import net.thrymr.services.RoleService;
import net.thrymr.utils.ApiResponse;
import net.thrymr.utils.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.Arguments;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/v1/user")
public class AppUserController {
    private final Logger logger = LoggerFactory.getLogger(AppUserController.class);


    private final  RoleService roleService;

    private final AssignmentService assignmentService;

    private final AppUserRepo appUserRepo;

    public AppUserController(RoleService roleService, AssignmentService assignmentService, AppUserRepo appUserRepo) {
        this.roleService = roleService;
        this.assignmentService = assignmentService;
        this.appUserRepo = appUserRepo;
    }

    @GetMapping("/roles")
    public ApiResponse getAllRoles() {
        logger.info("get all roles service started");
       ApiResponse apiResponse = roleService.getAllUserRoles();
        logger.info("get all roles service completed");
        return new ApiResponse(HttpStatus.OK, "All Roles",apiResponse);
    }



    //User attempt assignment

    @GetMapping("/get/all/assignments")
    public ApiResponse getAllAssignment() {
        logger.info("get all assignment service started");
        ApiResponse apiResponse = assignmentService.getAllAssignment();
        logger.info("get all assignment service completed");
        return new ApiResponse(HttpStatus.OK, "All Roles",apiResponse);
    }

    // get user by id
    @QueryMapping
    public AppUser getAppUserById(@Argument Long id){
        return   appUserRepo.findById(id).orElse(null);
    }

    // get all Users
    @QueryMapping
    public List<AppUser> getAllAppUsers(){
        return   appUserRepo.findAll();
    }

    @MutationMapping
    public String createAppUser(@Argument String firstName,@Argument String lastName,@Argument String email,@Argument String mobile,@Argument String alternateMobile ,@Argument String empId ){
        AppUser user=new AppUser();
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setMobile(mobile);
        user.setAlternateMobile(alternateMobile);
        user.setEmpId(empId);
        appUserRepo.save(user);
        return "User Saved successfully";
    }
    @MutationMapping
    public String  updateAppUser(@Arguments AppUserDto request){
        Optional<AppUser>optionalAppUser=appUserRepo.findById(request.getId());
        if(optionalAppUser.isPresent()){
            AppUser user= optionalAppUser.get();
            if(request.getEmpId()!=null){
                user.setEmpId(request.getEmpId());
            }
            if(request.getMobile()!=null) {
                user.setMobile(request.getMobile());
            }

            // user.setPassword(request.getPassword());
            if(request.getLastName()!=null) {
                user.setLastName(request.getLastName());
            }
            if(request.getFirstName()!=null) {
                System.out.println("ente"+request.getFirstName());
                user.setFirstName(request.getFirstName());
            }
            if(request.getEmail()!=null) {
                user.setEmail(request.getEmail());
            }
            if(request.getAlternateMobile()!=null) {
                user.setAlternateMobile(request.getAlternateMobile());
            }



            appUserRepo.save(user);
            return "User updated successfully";
        }
       return "No data found";
    }

    @MutationMapping
    public String deleteAppUserById(@Argument Long id){
        Optional<AppUser>optionalAppUser=appUserRepo.findById(id);
        optionalAppUser.ifPresent(appUserRepo::delete);
        return "User deleted successfully";
    }
}
