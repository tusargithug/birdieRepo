package net.thrymr.services;

import net.thrymr.dto.AppUserDto;
import net.thrymr.dto.UserCourseDto;
import net.thrymr.dto.response.UserAppointmentResponse;
import net.thrymr.enums.Roles;
import net.thrymr.model.AppUser;
import net.thrymr.utils.ApiResponse;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.util.List;


public interface AppUserService {
    ApiResponse saveUser(AppUserDto appUser);

    ApiResponse addUsersByExcel(MultipartFile file);

    ApiResponse getAllRoles();

    ApiResponse deleteUserById(Long id);

    ApiResponse getUserById(Long id);

    ApiResponse getAllUsers();

    AppUser getAppUserById(Long id);

    String createAppUser(AppUserDto request) throws ParseException;

    String updateAppUser(AppUserDto request) throws ParseException;

    String deleteAppUserById(Long id);

    String createUserCourse(UserCourseDto request) throws ParseException;

    List<AppUser> getAllAppUsers() throws ParseException;

    List<Roles> getAllEnumRoles();

    UserAppointmentResponse getUserAppointmentCountById(Long id);
}
