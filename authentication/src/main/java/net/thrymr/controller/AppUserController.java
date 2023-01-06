package net.thrymr.controller;
import net.thrymr.dto.AppUserDto;
import net.thrymr.dto.CounsellorEmployeeDto;
import net.thrymr.dto.UserCourseDto;
import net.thrymr.dto.response.PaginationResponse;
import net.thrymr.dto.response.UserAppointmentResponse;
import net.thrymr.enums.Roles;
import net.thrymr.model.AppUser;
import net.thrymr.services.AppUserService;
import net.thrymr.services.AssessmentService;
import net.thrymr.services.RoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.graphql.data.method.annotation.*;
import org.springframework.web.bind.annotation.RestController;
import java.text.ParseException;
import java.util.List;



@RestController
//@RequestMapping("/api/v1/user")
public class AppUserController {
    private final Logger logger = LoggerFactory.getLogger(AppUserController.class);

    private final RoleService roleService;
    private final AssessmentService assessmentService;
    private final AppUserService appUserService;
    public AppUserController(RoleService roleService, AssessmentService assessmentService, AppUserService appUserService) {
        this.roleService = roleService;
        this.assessmentService = assessmentService;
        this.appUserService = appUserService;
    }

    // get user by id
    @QueryMapping("getAppUserById")
    public AppUser getAppUserById(@Argument Long id) {
        return appUserService. getAppUserById(id);

    }

    @MutationMapping(name = "createAppUser")
    public String createAppUser(@Argument(name = "input") AppUserDto request) throws ParseException {
        return appUserService.createAppUser(request);
    }

    @MutationMapping(name = "updateAppUser")
    public String updateAppUser(@Argument(name = "input") AppUserDto request) throws ParseException {
        return appUserService.updateAppUser(request);

    }

    @MutationMapping(name = "updateAppUsers")
    public String updateAppUsers(@Argument(name = "input") List<AppUserDto> request) throws ParseException {
        return appUserService.updateAppUsers(request);

    }

    @MutationMapping(name = "deleteAppUserById")
    public String deleteAppUserById(@Argument Long id) {
        return appUserService.deleteAppUserById(id);

    }

    @MutationMapping(name = "createUserCourse")
    public String createUserCourse(@Argument(name = "input") UserCourseDto request) throws ParseException {
        return appUserService.createUserCourse(request);
    }

    @QueryMapping(name = "getAllEnumRoles")
    public List<Roles> getAllEnumRoles(){
        return appUserService.getAllEnumRoles();
    }

    @QueryMapping(name="getUserAppointmentCountById")
    public UserAppointmentResponse getUserAppointmentCountById(@Argument Long id){
        return appUserService.getUserAppointmentCountById(id);
    }

    @QueryMapping(name = "getAllAppUserPagination")
    public PaginationResponse getAllAppUserPagination(@Argument(name = "input") AppUserDto request) {
        return appUserService.getAllAppUserPagination(request);
    }

    @MutationMapping(name = "saveCounsellorEmployeeInfo")
    public String saveCounsellorEmployeeInfo(@Argument(name = "input") CounsellorEmployeeDto request) {
        return appUserService.saveCounsellorEmployeeInfo(request);

    }
}
