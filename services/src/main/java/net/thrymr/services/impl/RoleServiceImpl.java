package net.thrymr.services.impl;


import net.thrymr.model.Roles;
import net.thrymr.repository.RoleRepository;
import net.thrymr.services.RoleService;
import net.thrymr.utils.ApiResponse;
import net.thrymr.utils.Validator;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class RoleServiceImpl implements RoleService {

    private  final Logger logger = LoggerFactory.getLogger(RoleServiceImpl.class);

    private final RoleRepository roleRepository;

    private final Environment environment;

    public RoleServiceImpl(RoleRepository roleRepository, Environment environment) {
        this.roleRepository = roleRepository;
        this.environment = environment;
    }

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
        return new ApiResponse(HttpStatus.OK, environment.getProperty("ROLES_SAVED_SUCCESS"));
    }

    @Override
    public ApiResponse getAllUserRoles() {
        List<Roles> rolesList = roleRepository.findAll();
        List<String> rolesString;
        rolesString = rolesList.stream().map(Roles::getName).collect(Collectors.toList());
        return new ApiResponse(HttpStatus.OK, environment.getProperty("ROLES_FOUND"), rolesString);
    }

}










