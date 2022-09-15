package net.thrymr.controller;

import net.thrymr.services.RoleService;
import net.thrymr.utils.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/api/v1")
public class AppUserController {
    private final Logger logger = LoggerFactory.getLogger(AppUserController.class);


    private final  RoleService roleService;

    public AppUserController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping("/master/user/roles")
    public ApiResponse getAllRoles() {
        logger.info("get all roles service started");
       ApiResponse apiResponse = roleService.getAllUserRoles();
        logger.info("get all roles service completed");
        return new ApiResponse(HttpStatus.OK, "All Roles",apiResponse);
    }
}
