package net.thrymr.services;

import net.thrymr.model.master.MtRoles;
import net.thrymr.utils.ApiResponse;

import java.util.List;


public interface RoleService {

ApiResponse saveRole();
 ApiResponse getAllUserRoles();


    MtRoles mtRoleById(Long id);

    List<MtRoles> getAllMtRoles();
}
