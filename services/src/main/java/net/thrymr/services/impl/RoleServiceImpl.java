package net.thrymr.services.impl;


import net.thrymr.model.master.MtRoles;
import net.thrymr.repository.RoleRepo;
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

    private final RoleRepo roleRepo;

    private final Environment environment;

    public RoleServiceImpl(RoleRepo roleRepo, Environment environment) {
        this.roleRepo = roleRepo;
        this.environment = environment;
    }

    private ApiResponse validateRoleRequest(MtRoles request) {
        if (!Validator.isObjectValid(request)) {
            return new ApiResponse(HttpStatus.BAD_REQUEST, environment.getProperty("INVALID_REQUEST"));
        }
        return null;
    }

    @Override
    public ApiResponse saveRole() {

        List<String> stringList = Arrays.asList("Content Moderator", "WellBeing Manager", "Operations Team", "Admin", "On-site counsellor ", "Vendor", "Director");
        for (String role : stringList) {
            MtRoles saveRole = new MtRoles(role);
            roleRepo.save(saveRole);
            ApiResponse apiResponse = validateRoleRequest(saveRole);

        }
        return new ApiResponse(HttpStatus.OK, environment.getProperty("ROLES_SAVED_SUCCESS"));
    }

    @Override
    public ApiResponse getAllUserRoles() {
        List<MtRoles> mtRolesList = roleRepo.findAll();
        List<String> rolesString = mtRolesList.stream().map(MtRoles::getName).collect(Collectors.toList());
        return new ApiResponse(HttpStatus.OK, environment.getProperty("ROLES_FOUND"), rolesString);
    }

    @Override
    public List<MtRoles> getAllMtRoles() {
        return roleRepo.findAll();
    }

    @Override
    public MtRoles mtRoleById(Long id) {
        return roleRepo.findById(id).orElse(null);
    }
}










