package net.thrymr.services.impl;


import net.thrymr.dto.RolesDto;
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
import java.util.Optional;
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
    public List<MtRoles> getAllMtRoles() {
        return roleRepo.findAll();
    }

    @Override
    public String createRole(RolesDto request) {
        Optional<MtRoles>optionalMtRoles=roleRepo.findByName(request.getName());
        if(optionalMtRoles.isEmpty()){
            return "Role already existed";
        }else {
            MtRoles mtRoles=new MtRoles();
            mtRoles.setName(request.getName());
            return "Role saved successfully";
        }

    }

    @Override
    public String updateRole(RolesDto request) {
        Optional<MtRoles>optionalMtRoles=roleRepo.findById(request.getId());
        if(optionalMtRoles.isPresent()){
            MtRoles mtRoles=optionalMtRoles.get();
            mtRoles.setName(request.getName());

        }
        return "Role updated successfully";
    }

    @Override
    public String deleteRoleById(Long id) {
        Optional<MtRoles>optionalMtRoles=roleRepo.findById(id);
        optionalMtRoles.ifPresent(roleRepo::delete);
        return "Role deleted successfully";
    }

    @Override
    public MtRoles mtRoleById(Long id) {
        return roleRepo.findById(id).orElse(null);
    }
}










