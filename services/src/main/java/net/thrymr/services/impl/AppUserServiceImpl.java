package net.thrymr.services.impl;


import net.thrymr.constant.Constants;
import net.thrymr.dto.*;
import net.thrymr.dto.response.UserAppointmentResponse;
import net.thrymr.enums.Roles;
import net.thrymr.enums.SlotStatus;
import net.thrymr.model.*;
import net.thrymr.model.master.*;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.graphql.data.method.annotation.Argument;
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
    Logger logger = LoggerFactory.getLogger(AppUserServiceImpl.class);

    @Autowired
    AppUserRepo appUserRepo;
    @Autowired
    Environment environment;
    @Autowired
    RoleRepo roleRepo;
    @Autowired
    UserCourseRepo userCourseRepo;
    @Autowired
    CourseRepo courseRepo;
    @Autowired
    OptionsRepo optionsRepo;
    @Autowired
    SiteRepo siteRepo;
    @Autowired
    ShiftTimingsRepo shiftTimingsRepo;
    @Autowired
    TeamRepo teamRepo;
    @Autowired
    AppointmentRepo appointmentRepo;
    @Autowired
    CounsellorRepo counsellorRepo;


    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Override
    public ApiResponse saveUser(AppUserDto appUser) {
        AppUser model = dtoToEntity(appUser);
        appUserRepo.save(model);
        return new ApiResponse(HttpStatus.OK, environment.getProperty("USER_SAVED"));
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
            for (int index = 1; index <= worksheet.getLastRowNum(); index++) {
                if (index > 0) {
                    try {
                        XSSFRow row = worksheet.getRow(index);
                        AppUser appUser = new AppUser();

                        if (row.getCell(1) != null) {
                            appUser.setFirstName(getCellValue(row.getCell(1)));
                        }
                        if (row.getCell(2) != null) {
                            appUser.setLastName(getCellValue(row.getCell(2)));
                        }
                        if (row.getCell(3) != null) {
                            appUser.setEmail(getCellValue(row.getCell(3)));
                        }
                        if (row.getCell(4) != null) {
                            appUser.setUserName(getCellValue(row.getCell(4)));
                        }
                        if (row.getCell(5) != null) {
                            appUser.setMobile(getCellValue(row.getCell(5)));
                        }
                        if (row.getCell(6) != null) {
                            appUser.setAlternateMobile(getCellValue(row.getCell(6)));
                        }
                        if (row.getCell(7) != null) {
                            appUser.setPassword(getCellValue(row.getCell(7)));

                        }
                        if (row.getCell(8) != null) {
                            Optional<MtRoles> optionalRoles = roleRepo.findById(Long.valueOf(getCellValue(row.getCell(8))));
                            // logger.info("optionalRole{}: " , CommonUtil.getStringFromObject(optionalRoles));
                            optionalRoles.ifPresent(role -> appUser.setMtRoles(role));
                        }
                        if (row.getCell(9) != null) {
                            appUser.setEmpId(getCellValue(row.getCell(9)));
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
                appUsers = appUserRepo.saveAll(appUsers);
                return new ApiResponse(HttpStatus.OK, environment.getProperty("USER_IMPORT_SUCCESS"), apiResponse);
            }
        } catch (Exception e) {
            return new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR, environment.getProperty("UN_HANDLED_ERROR_MESSAGE"));
        }

        return new ApiResponse(HttpStatus.BAD_REQUEST, environment.getProperty("USER_IMPORT_FAILED"));
    }

    @Override
    public ApiResponse getAllRoles() {
        List<MtRoles> mtRolesOptional = roleRepo.findAll();
        List<RolesDto> rolesDtoList;
        rolesDtoList = mtRolesOptional.stream().map(this::entityToDto).collect(Collectors.toList());

        return new ApiResponse(HttpStatus.OK, environment.getProperty("ROLES_FOUND"), rolesDtoList);
    }

    @Override
    public ApiResponse deleteUserById(Long id) {
        //TODO validate id
        Optional<AppUser> optionalAppUser = appUserRepo.findById(id);
        optionalAppUser.ifPresent(appUserRepo::delete);

        return new ApiResponse(HttpStatus.OK, environment.getProperty("USER_DELETED"));

    }

    @Override
    public ApiResponse getUserById(Long id) {
        //TODO validate id
        Optional<AppUser> optionalAppUser = appUserRepo.findById(id);

        if (optionalAppUser.isPresent()) {
            AppUser user = optionalAppUser.get();
            AppUserDto dto = entityToDto(user);
            return new ApiResponse(HttpStatus.OK, environment.getProperty("SUCCESS"), dto);
        } else {
            return new ApiResponse(HttpStatus.OK, environment.getProperty("USER_NOT_FOUND"));
        }


    }

    @Override
    public ApiResponse getAllUsers() {

        List<AppUser> appUserList = appUserRepo.findAll();
        List<AppUserDto> appUserDtoList = appUserList.stream().map(this::entityToDto).toList();
        return new ApiResponse(HttpStatus.OK, "", appUserDtoList);
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

    private RolesDto entityToDto(MtRoles request) {
        RolesDto dto = new RolesDto();
        dto.setName(request.getName());

        dto.setUsersDto(request.getUsers().stream().map(this::entityToDto).toList());
        return dto;
    }

    private AppUser dtoToEntity(AppUserDto request) {
        AppUser appUser = new AppUser();
        if (Validator.isValid(request.getId())) {
            appUser = appUserRepo.findById(request.getId()).orElse(new AppUser());
        }
        if (Validator.isValid(request.getUserName())) {
            appUser.setUserName(request.getUserName());
        }
        appUser.setId(request.getId());
        appUser.setFirstName(request.getFirstName());
        appUser.setLastName(request.getLastName());
        appUser.setMobile(request.getMobile());
        appUser.setEmail(request.getEmail());
        appUser.setAlternateMobile(request.getAlternateMobile());
        appUser.setPassword(bCryptPasswordEncoder().encode(request.getPassword()));
        if (request.getRolesDto() != null && !request.getRolesDto().getName().isEmpty()) {
            Optional<MtRoles> optionalRoles = roleRepo.findByName(request.getRolesDto().getName());
            appUser.setMtRoles(optionalRoles.get());
        }
        return appUser;
    }

    private AppUserDto entityToDto(AppUser user) {
        AppUserDto dto = new AppUserDto();
        dto.setId(user.getId());
        dto.setUserName(user.getUserName());
        dto.setMobile(user.getMobile());
        dto.setEmail(user.getEmail());

        return dto;
    }

    @Override
    public String createAppUser(AppUserDto request) throws ParseException {
        AppUser user = new AppUser();
        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setUserName(request.getFirstName() + " " + request.getLastName());
        user.setMobile(request.getMobile());
        user.setAlternateMobile(request.getAlternateMobile());
        user.setEmpId(request.getEmpId());
        user.setRoles(Roles.valueOf(request.getRoles()));
        if (request.getDateOfJoining() != null) {
            user.setDateOfJoining(DateUtils.toFormatStringToDate(String.valueOf(request.getDateOfJoining()), Constants.DATE_FORMAT));
        }
        if (request.getIsActive() != null && request.getIsActive().equals(Boolean.TRUE)) {
            user.setIsActive(request.getIsActive());
        }
        if (Validator.isValid(request.getSiteId())) {
            Optional<MtSite> optionalSite = siteRepo.findById(request.getSiteId());
            if (optionalSite.isPresent()) {
                user.setMtSite(optionalSite.get());
            }
        }
        if (Validator.isValid(request.getShiftTimingsId())) {
            Optional<MtShiftTimings> optionalShiftTimings = shiftTimingsRepo.findById(request.getShiftTimingsId());
            if (optionalShiftTimings.isPresent()) {
                user.setMtShiftTimings(optionalShiftTimings.get());
            }
        }

        if (Validator.isValid(request.getTeamId())) {
            Optional<MtTeam> optionalTeamId = teamRepo.findById(request.getTeamId());
            if (optionalTeamId.isPresent()) {
                user.setMtTeam(optionalTeamId.get());
            }
        }
        appUserRepo.save(user);
        return "User Saved successfully";
    }

    @Override
    public String updateAppUser(AppUserDto request) throws ParseException {
        if (Validator.isValid(request.getId())) {
            Optional<AppUser> optionalAppUser = appUserRepo.findById(request.getId());
            if (optionalAppUser.isPresent()) {
                AppUser user = optionalAppUser.get();
                if (Validator.isValid(request.getEmpId())) {
                    user.setEmpId(request.getEmpId());
                }
                if (Validator.isValid(request.getMobile())) {
                    user.setMobile(request.getMobile());
                }
                if (Validator.isValid(request.getAlternateMobile())) {
                    user.setAlternateMobile(request.getAlternateMobile());
                }

                // user.setPassword(request.getPassword());
                if (Validator.isValid(request.getLastName())) {
                    user.setLastName(request.getLastName());
                }
                if (Validator.isValid(request.getFirstName())) {
                    user.setFirstName(request.getFirstName());
                }
                if (Validator.isValid(request.getEmail())) {
                    user.setEmail(request.getEmail());
                }
                if (Validator.isValid(request.getRoles())) {
                    user.setRoles(Roles.valueOf(request.getRoles()));
                }
                if (Validator.isValid(request.getDateOfJoining())) {
                    user.setDateOfJoining(DateUtils.toFormatStringToDate(String.valueOf(request.getDateOfJoining()), Constants.DATE_FORMAT));
                }
                if (request.getIsActive() != null && request.getIsActive().equals(Boolean.TRUE) || request.getIsActive().equals(Boolean.FALSE)) {
                    user.setIsActive(request.getIsActive());
                }
                if (Validator.isValid(request.getSiteId())) {
                    Optional<MtSite> optionalSite = siteRepo.findById(request.getSiteId());
                    if (optionalSite.isPresent()) {
                        user.setMtSite(optionalSite.get());
                    }
                }
                if (Validator.isValid(request.getShiftTimingsId())) {
                    Optional<MtShiftTimings> optionalShiftTimings = shiftTimingsRepo.findById(request.getShiftTimingsId());
                    if (optionalShiftTimings.isPresent()) {
                        user.setMtShiftTimings(optionalShiftTimings.get());
                    }
                }
                if (Validator.isValid(request.getTeamId())) {
                    Optional<MtTeam> optionalTeamId = teamRepo.findById(request.getTeamId());
                    if (optionalTeamId.isPresent()) {
                        user.setMtTeam(optionalTeamId.get());
                    }
                }
                appUserRepo.save(user);
                return "User updated successfully";
            }
        }
        return "This id not present in database";
    }

    @Override
    public String deleteAppUserById(Long id) {
        AppUser appUser = null;
        if (Validator.isValid(id)) {
            Optional<AppUser> optionalAppUser = appUserRepo.findById(id);
            //optionalAppUser.ifPresent(appUserRepo::delete);
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
        AppUser appUser = null;
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
    public List<AppUser> getAllAppUsers() throws ParseException {
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
        Pageable pageable = null;
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
            if (response.getShiftTimingsId() != null) {
                Predicate shiftTimings = criteriaBuilder.and(root.get("shiftTimings").in(response.getShiftTimingsId()));
                addVendorPredicate.add(shiftTimings);
            }
            if (response.getTeamId() != null) {
                Predicate teamId = criteriaBuilder.and(root.get("team").in(response.getTeamId()));
                addVendorPredicate.add(teamId);
            }

//            List<Counsellor> counsellorList = counsellorRepo.findAll();
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
