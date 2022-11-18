package net.thrymr.services;

import net.thrymr.dto.RolesDto;
import net.thrymr.model.master.MtRoles;
import net.thrymr.utils.ApiResponse;

import java.util.List;


public interface RoleService {

   ApiResponse saveRole();

    MtRoles mtRoleById(Long id);

    List<MtRoles> getAllMtRoles();

    String createRole(RolesDto request);

    String updateRole(RolesDto request);

    String deleteRoleById(Long id);
}
