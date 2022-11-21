package net.thrymr.services.impl;


import net.thrymr.constant.Constants;
import net.thrymr.dto.*;
import net.thrymr.dto.response.UserAppointmentResponse;
import net.thrymr.enums.Roles;
import net.thrymr.enums.Alerts;
import net.thrymr.enums.SlotStatus;
import net.thrymr.model.*;
import net.thrymr.model.master.Course;
import net.thrymr.model.master.MtOptions;
import net.thrymr.repository.*;
import net.thrymr.services.AppUserService;
import net.thrymr.utils.ApiResponse;
import net.thrymr.utils.DateUtils;
import net.thrymr.utils.Validator;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.criteria.Predicate;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class AppUserServiceImpl implements AppUserService {
    private final Logger logger = LoggerFactory.getLogger(AppUserServiceImpl.class);

    private final AppUserRepo appUserRepo;

    private final Environment environment;

    private final UserCourseRepo userCourseRepo;

    private final CourseRepo courseRepo;

    private final OptionsRepo optionsRepo;

    private final SiteRepo siteRepo;

    private final AppointmentRepo appointmentRepo;

    private final CounsellorRepo counsellorRepo;

    public AppUserServiceImpl(AppUserRepo appUserRepo, Environment environment, UserCourseRepo userCourseRepo, CourseRepo courseRepo, OptionsRepo optionsRepo, SiteRepo siteRepo, CounsellorSlotRepo counsellorSlotRepo, AppointmentRepo appointmentRepo, CounsellorRepo counsellorRepo) {
        this.appUserRepo = appUserRepo;
        this.environment = environment;
        this.userCourseRepo = userCourseRepo;
        this.courseRepo = courseRepo;
        this.optionsRepo = optionsRepo;
        this.siteRepo = siteRepo;
        this.appointmentRepo = appointmentRepo;
        this.counsellorRepo = counsellorRepo;
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }


    private ApiResponse validateAddUserRequest(MultipartFile request) {
        if (!Validator.isObjectValid(request)) {
            return new ApiResponse(HttpStatus.BAD_REQUEST, environment.getProperty("INVALID_REQUEST"));
        }
        return null;
    }

    @Override
    public ApiResponse addUsersByExcel(MultipartFile file) {

        try {
            ApiResponse apiResponse = validateAddUserRequest(file);
            List<AppUser> appUsers = new ArrayList<>();
            XSSFWorkbook workbook;
            try {
                workbook = new XSSFWorkbook(file.getInputStream());
            } catch (
                    IOException e) {
                return new ApiResponse(HttpStatus.BAD_REQUEST, environment.getProperty("USERS_IMPORT_FORMAT_FAILED"));
            }
            XSSFSheet worksheet = workbook.getSheetAt(0);
            if (worksheet.getLastRowNum() < 1) {
                return new ApiResponse(HttpStatus.BAD_REQUEST, environment.getProperty("USERS_IMPORT_FORMAT_INVALID_DATA"));
            }
            XSSFRow row;
            for (int index = 1; index <= worksheet.getLastRowNum(); index++) {
                if (index > 0) {
                    try {
                        row = worksheet.getRow(index);
                        AppUser appUser = new AppUser();
                        if (row.getCell(1) != null) {
                            appUser.setEmpId(getCellValue(row.getCell(1)));
                        }
                        if (row.getCell(2) != null) {
                            appUser.setUserName(getCellValue(row.getCell(2)));
                        }
                        if (row.getCell(3) != null) {
                            appUser.setMobile(getCellValue(row.getCell(3)));
                        }
                        if (row.getCell(4) != null) {
                            appUser.setEmail(getCellValue(row.getCell(4)));
                        }
                        if (row.getCell(5) != null) {
                            appUser.setDateOfJoining(DateUtils.toFormatStringToDate(getCellValue(row.getCell(5)), Constants.DATE_FORMAT));
                        }

                        if (row.getCell(6) != null) {
                            Optional<Site> optionalSite = siteRepo.findById(Long.valueOf(getCellValue(row.getCell(6))));
                            optionalSite.ifPresent(appUser::setSite);
                        }
                        if (row.getCell(7) != null) {
                            appUser.setAlerts(Alerts.valueOf(getCellValue(row.getCell(7))));
                        }
                        if (row.getCell(8) != null) {
                            appUser.setRoles(Roles.valueOf(getCellValue(row.getCell(8))));
                        }
                        setUserSearchKey(appUser);
                        appUsers.add(appUser);
                    } catch (Exception e) {
                        logger.error("Exception{} ", e);
                        return new ApiResponse(HttpStatus.BAD_REQUEST, environment.getProperty("USERS_IMPORT_FORMAT_FAILED"));
                    }
                }
            }
            if (Validator.isObjectValid(appUsers)) {
                appUserRepo.saveAll(appUsers);
                return new ApiResponse(HttpStatus.OK, environment.getProperty("USER_IMPORT_SUCCESS"), apiResponse);
            }
        } catch (Exception e) {
            return new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR, environment.getProperty("UN_HANDLED_ERROR_MESSAGE"));
        }

        return new ApiResponse(HttpStatus.BAD_REQUEST, environment.getProperty("USER_IMPORT_FAILED"));
    }

    private String getCellValue(XSSFCell cell) {
        String value;
        if (cell.getCellType().equals(CellType.NUMERIC)) {
            value = NumberToTextConverter.toText(cell.getNumericCellValue());
        } else if (cell.getCellType().equals(CellType.STRING)) {
            value = cell.getStringCellValue();
        } else if (cell.getCellType().equals(CellType.BOOLEAN)) {
            value = String.valueOf(cell.getBooleanCellValue());
        } else if (cell.getCellType().equals(CellType.BLANK) || cell.getCellType().equals(CellType.ERROR)) {
            value = "";
        } else {
            value = cell.getRawValue();
        }
        return value;
    }

    private void setUserSearchKey(AppUser appUser) {
        String searchKey = "";
        if (Validator.isValid(appUser.getUserName())) {
            searchKey = searchKey + appUser.getUserName();
        }
        appUser.setSearchKey(searchKey);
    }

    @Override
    public String createAppUser(AppUserDto request) throws ParseException {
        AppUser user = new AppUser();
        user.setUserName(request.getUserName());
        user.setEmpId(request.getEmpId());
        if (request.getDateOfJoining() != null) {
            user.setDateOfJoining(DateUtils.toFormatStringToDate(String.valueOf(request.getDateOfJoining()), Constants.DATE_FORMAT));
        }
        if (Validator.isValid(request.getSiteId())) {
            Optional<Site> optionalSite = siteRepo.findById(request.getSiteId());
            if (optionalSite.isPresent()) {
                user.setSite(optionalSite.get());
            }
        }
        user.setRoles(Roles.valueOf(request.getRoles()));
        user.setEmail(request.getEmail());
        user.setMobile(request.getMobile());
        appUserRepo.save(user);
        return "User Saved successfully";
    }

    @Override
    public String updateAppUser(AppUserDto request) throws ParseException {
        if(Validator.isValid(request.getId())) {
            Optional<AppUser> optionalAppUser = appUserRepo.findById(request.getId());
            if (optionalAppUser.isPresent()) {
                AppUser user = optionalAppUser.get();
                if (Validator.isValid(request.getUserName())) {
                    user.setUserName(request.getUserName());
                }
                if (Validator.isValid(request.getEmpId())) {
                    user.setEmpId(request.getEmpId());
                }
                if (Validator.isObjectValid(request.getDateOfJoining())) {
                    user.setDateOfJoining(DateUtils.toFormatStringToDate(String.valueOf(request.getDateOfJoining()), Constants.DATE_FORMAT));
                }
                if (Validator.isValid(request.getSiteId())) {
                    Optional<Site> optionalSite = siteRepo.findById(request.getSiteId());
                    if (optionalSite.isPresent()) {
                        user.setSite(optionalSite.get());
                    }
                }
                if (Validator.isValid(request.getMobile())) {
                    user.setMobile(request.getMobile());
                }

                if (Validator.isValid(request.getEmail())) {
                    user.setEmail(request.getEmail());
                }
                if (Validator.isValid(request.getRoles())) {
                    user.setRoles(Roles.valueOf(request.getRoles()));
                }
                appUserRepo.save(user);
                return "User updated successfully";
            }
        }
        return "This id not present in database";
    }

    @Override
    public String deleteAppUserById(Long id) {
        AppUser appUser;
        if (Validator.isValid(id)) {
            Optional<AppUser> optionalAppUser = appUserRepo.findById(id);
            if (optionalAppUser.isPresent()) {
                appUser = optionalAppUser.get();
                appUser.setIsActive(Boolean.FALSE);
                appUser.setIsDeleted(Boolean.TRUE);
                appUserRepo.save(appUser);
                return "User deleted successfully";
            }

        }
        return "this id not present in database";
    }

    @Override
    public String createUserCourse(UserCourseDto request) throws ParseException {
        // AppUser user= CommonUtil.getAppUser();
        UserCourse userCourse = new UserCourse();
        //userCourse.setUser(new AppUser());
        if (Validator.isValid(request.getAppUserId())) {
            Optional<AppUser> optionalAppUser = appUserRepo.findById(request.getAppUserId());
            optionalAppUser.ifPresent(userCourse::setUser);
        }
        if (Validator.isValid(request.getCourseId())) {
            Optional<Course> optionalCourse = courseRepo.findById(request.getCourseId());
            optionalCourse.ifPresent(userCourse::setCourse);
        }
        List<MtOptions> mtOptionsList = optionsRepo.findAllByIdIn(request.getMtOptionsIds());
        if (Validator.isValid(mtOptionsList)) {
            userCourse.setMtOptions(new HashSet<>(mtOptionsList));
        }
        if (Validator.isValid(request.getStartedDate())) {
            userCourse.setStartedDate(DateUtils.toFormatStringToDate(request.getStartedDate(), Constants.DATE_FORMAT));
        }
        if (Validator.isValid(request.getCompletedDate())) {
            userCourse.setCompletedDate(DateUtils.toFormatStringToDate(request.getCompletedDate(), Constants.DATE_FORMAT));
        }
        if (request.getStatus() != null) {
            userCourse.setStatus(request.getStatus());
        }
        if (request.getIsActive() != null && request.getIsActive().equals(Boolean.TRUE)) {
            userCourse.setIsActive(request.getIsActive());
        }
        userCourseRepo.save(userCourse);
        return "Saved Successfully";
    }


    @Override
    public AppUser getAppUserById(Long id) {
        AppUser appUser;
        if (Validator.isValid(id)) {
            Optional<AppUser> optionalAppUser = appUserRepo.findById(id);
            if (optionalAppUser.isPresent() && optionalAppUser.get().getIsActive().equals(Boolean.TRUE)) {
                appUser = optionalAppUser.get();
                return appUser;
            }
        }
        return new AppUser();
    }

    @Override
    public List<AppUser> getAllAppUsers(){
        List<AppUser> appUserList = appUserRepo.findAll();
        if (!appUserList.isEmpty()) {
            return appUserList.stream().filter(obj -> obj.getIsActive().equals(Boolean.TRUE)).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    @Override
    public List<Roles> getAllEnumRoles() {
        List<Roles> rolesList = Arrays.asList(Roles.ADMIN, Roles.COUNSELLOR, Roles.DIRECTOR, Roles.EMPLOYEE, Roles.NONE, Roles.OP_STREAM, Roles.TEAM_LEADER, Roles.TEAM_MANAGER, Roles.VENDOR
                , Roles.WELL_BEING_MANGER, Roles.SITE_MANAGER);
        return rolesList;
    }

    @Override
    public UserAppointmentResponse getUserAppointmentCountById(Long id) {
        UserAppointmentResponse userAppointmentResponse = new UserAppointmentResponse();
        userAppointmentResponse.setRescheduledAppointmentCount(0);
        userAppointmentResponse.setCanceledAppointmentCount(0);
        userAppointmentResponse.setAvailableAppointmentCount(0);
        userAppointmentResponse.setTotalAppointmentsCount(0);
        userAppointmentResponse.setBlockedAppointmentCount(0);
        Optional<AppUser> optionalAppUser = appUserRepo.findById(id);
        if (optionalAppUser.isPresent() && optionalAppUser.get().getIsActive().equals(Boolean.TRUE)) {
            userAppointmentResponse.setAppUser(optionalAppUser.get());
            List<UserAppointment> userAppointmentList = appointmentRepo.findAll();
            for (UserAppointment userAppointment : userAppointmentList) {
                if (Objects.equals(userAppointment.getAppUser().getId(), id)) {
                    if (userAppointment.getSlotStatus().equals(SlotStatus.DELETED)) {
                        userAppointmentResponse.setCanceledAppointmentCount(userAppointmentResponse.getCanceledAppointmentCount() + 1);
                    }
                    if (userAppointment.getSlotStatus().equals(SlotStatus.BOOKED)) {
                        userAppointmentResponse.setTotalAppointmentsCount(userAppointmentResponse.getTotalAppointmentsCount() + 1);
                    }
                    if (userAppointment.getSlotStatus().equals(SlotStatus.AVAILABLE)) {
                        userAppointmentResponse.setAvailableAppointmentCount(userAppointmentResponse.getAvailableAppointmentCount() + 1);
                    }
                    if (userAppointment.getSlotStatus().equals(SlotStatus.BLOCKED)) {
                        userAppointmentResponse.setBlockedAppointmentCount(userAppointmentResponse.getBlockedAppointmentCount() + 1);
                    }
                    if (userAppointment.getSlotStatus().equals(SlotStatus.RESCHEDULED)) {
                        userAppointmentResponse.setRescheduledAppointmentCount(userAppointmentResponse.getRescheduledAppointmentCount() + 1);
                    }
                }
            }
            return userAppointmentResponse;
        }
        return new UserAppointmentResponse();
    }

    @Override
    public List<AppUser> getAllAppUserPagination(AppUserDto response) {
        Pageable pageable;
        if (Validator.isValid(response.getPageSize())) {
            pageable = PageRequest.of(response.getPageNumber(), response.getPageSize());
        }
        pageable = PageRequest.of(response.getPageNumber(), response.getPageSize(), Sort.Direction.ASC, "createdOn");
        if (Validator.isValid(response.getAddedOn())) {
            pageable = PageRequest.of(response.getPageNumber(), response.getPageSize(), Sort.Direction.ASC, "createdOn");
        }

        Specification<AppUser> appUserSpecification = ((root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> addVendorPredicate = new ArrayList<>();
            if (response.getUserName() != null) {
                Predicate userName = criteriaBuilder.and(root.get("userName").in(response.getUserName()));
                addVendorPredicate.add(userName);
            }
            if (response.getEmpId() != null && !response.getEmpId().isEmpty()) {
                Predicate empId = criteriaBuilder.and(root.get("empId").in(response.getEmpId()));
                addVendorPredicate.add(empId);
            }
            if (response.getRoles() != null) {
                Predicate roles = criteriaBuilder.and(root.get("roles").in(response.getRoles()));
                addVendorPredicate.add(roles);
            }
            if (response.getAlerts() != null) {
                Predicate alerts = criteriaBuilder.and(root.get("alerts").in(response.getAlerts()));
                addVendorPredicate.add(alerts);
            }
            if (response.getTeamId() != null) {
                Predicate teamId = criteriaBuilder.and(root.get("team").in(response.getTeamId()));
                addVendorPredicate.add(teamId);
            }

            if (response.getRoles() != null && response.getRoles().equalsIgnoreCase(Roles.COUNSELLOR.toString())) {
                Predicate roles = criteriaBuilder.and(root.get("userName").in(response.getCounsellorId()));
                addVendorPredicate.add(roles);
            }
            return criteriaBuilder.and(addVendorPredicate.toArray(new Predicate[0]));
        });
        Page<AppUser> appUserObjectives = appUserRepo.findAll(appUserSpecification, pageable);
        List<AppUser> appUserList = null;
        if (appUserObjectives.getContent() != null) {
            appUserList = appUserObjectives.stream().filter(obj -> obj.getIsActive().equals(Boolean.TRUE)).toList();
        }
        return appUserList;
    }


}
