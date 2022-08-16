package net.thrymr.controller;

import net.thrymr.service.RoleService;
import net.thrymr.utils.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;


/*
 *@author Chanda Veeresh
 *@version 1.0
 *@since  19-07-2022
 */
@RestController
@RequestMapping("/api/v1")
public class AppUserController {
    private final Logger logger;

    {
        logger = LoggerFactory.getLogger(MasterController.class);
    }
    @Autowired
    RoleService roleService;
    @GetMapping("/master/user/roles")
    public ApiResponse getAllRoles() {
        logger.info("get all roles service started");
       ApiResponse apiResponse = roleService.getAllUserRoles();
        logger.info("get all roles service completed");
        return new ApiResponse(HttpStatus.OK, "All Roles",apiResponse);
    }
}
