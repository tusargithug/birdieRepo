package net.thrymr.service;
import net.thrymr.dto.AppUserDto;
import net.thrymr.utils.ApiResponse;
import org.springframework.web.multipart.MultipartFile;

/*
 *@author Chanda Veeresh
 *@version 1.0
 *@since  11-07-2022
 */
public interface AppUserService {
    ApiResponse saveUser(AppUserDto appUser);
    ApiResponse addUsersByExcel(MultipartFile file);
    ApiResponse getAllRoles();
}
