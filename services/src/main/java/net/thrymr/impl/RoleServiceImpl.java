package net.thrymr.impl;


import net.thrymr.model.Roles;
import net.thrymr.repository.RoleRepository;
import net.thrymr.service.RoleService;
import net.thrymr.utils.ApiResponse;

import net.thrymr.utils.Validator;
import org.hibernate.cfg.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/*
 *@author Chanda Veeresh
 *@version 1.0
 *@since  08-07-2022
 */
@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    RoleRepository roleRepository;
    @Autowired
    private org.springframework.core.env.Environment environment;

    private ApiResponse validateRoleRequest(Roles request) {
        if (!Validator.isObjectValid(request)) {
            return new ApiResponse(HttpStatus.BAD_REQUEST, environment.getProperty("INVALID_REQUEST"));
        }
        return null;
    }

    @Override
    public ApiResponse saveRole() {

        List<String> stringList = Arrays.asList("Content Moderator", "WellBeing Manager", "Operations Team", "Admin", "On-site counsellor ", "Vendor", "Director");
        for (String role : stringList) {
            Roles saveRole = new Roles(role);
            roleRepository.save(saveRole);
            ApiResponse apiResponse = validateRoleRequest(saveRole);

        }
        return new ApiResponse(HttpStatus.OK, "Roles Saved ");
    }

    @Override
    public ApiResponse getAllUserRoles() {
        List<Roles> rolesList = roleRepository.findAll();
        List<String> rolesString;
        rolesString = rolesList.stream().map(Roles::getName).collect(Collectors.toList());
        return new ApiResponse(HttpStatus.OK, environment.getProperty("ROLES.FOUND"), rolesString);
    }

}










