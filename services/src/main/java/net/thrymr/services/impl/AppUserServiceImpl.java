package net.thrymr.services.impl;

import net.thrymr.constant.Constants;
import net.thrymr.dto.*;
import net.thrymr.dto.response.PaginationResponse;
import net.thrymr.dto.response.UserAppointmentResponse;
import net.thrymr.enums.Gender;
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
import org.springframework.beans.factory.annotation.Autowired;
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

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class AppUserServiceImpl implements AppUserService {

    private final Logger logger = LoggerFactory.getLogger(AppUserServiceImpl.class);

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
    FileRepo fileRepo;

    @Autowired
    CounsellorRepo counsellorRepo;

    @Autowired
    CounsellorEmployeeRepo counsellorEmployeeRepo;
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
                        if (row.getCell(9) != null) {
                            appUser.setEmpId(getCellValue(row.getCell(9)));
                        }
                        if (row.getCell(10) != null) {
                            appUser.setUserName(getCellValue(row.getCell(10)));
                        }
                        if (row.getCell(11) != null) {
                            appUser.setMobile(getCellValue(row.getCell(11)));
                        }
                        if (row.getCell(12) != null) {
                            appUser.setEmail(getCellValue(row.getCell(12)));
                        }
                        if (row.getCell(13) != null) {
                            appUser.setDateOfJoining(DateUtils.toFormatStringToDate(getCellValue(row.getCell(13)), Constants.DATE_FORMAT));
                        }

                        if (row.getCell(14) != null) {
                            Optional<Site> optionalSite = siteRepo.findById(Long.valueOf(getCellValue(row.getCell(14))));
                            optionalSite.ifPresent(appUser::setSite);
                        }
//                        if (row.getCell(15) != null) {
//                            appUser.setAlerts(Alerts.valueOf(getCellValue(row.getCell(15))));
//                        }
                        if (row.getCell(16) != null) {
                            appUser.setRoles(Roles.valueOf(getCellValue(row.getCell(16))));
                        }
                        if (row.getCell(17) != null) {
                            appUser.setShiftEndAt(DateUtils.toStringToLocalTime(getCellValue(row.getCell(17)), Constants.TIME_FORMAT_12_HOURS));
                        }
                        if (row.getCell(18) != null) {
                            appUser.setShiftEndAt(DateUtils.toStringToLocalTime(getCellValue(row.getCell(18)), Constants.TIME_FORMAT_12_HOURS));
                        }
                        if (row.getCell(19) != null) {
                            appUser.setCountryCode(getCellValue(row.getCell(19)));
                        }
                        if (row.getCell(20) != null) {
                            Optional<FileEntity> optionalFileEntity = fileRepo.findById(Long.valueOf(getCellValue(row.getCell(20))));
                            optionalFileEntity.ifPresent(appUser::setUploadPicture);
                        }
                            appUser.setSearchKey(getAppUserSearchKey(appUser));

                            getAppUserSearchKey(appUser);
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


    @Override
    public String createAppUser(AppUserDto request) throws ParseException {
        AppUser user = new AppUser();
        user.setUserName(request.getUserName());
        if(request.getEmpId() != null && !appUserRepo.existsByEmpId(request.getEmpId())) {
            user.setEmpId(request.getEmpId());
        }else {
            return "this employee id is already Existed";
        }
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
        if(request.getEmail() != null && !appUserRepo.existsByEmail(request.getEmail())) {
            user.setEmail(request.getEmail());
        }else {
            return "This email already existed";
        }
        user.setCountryCode(request.getCountryCode());
        if(request.getMobile() != null && !appUserRepo.existsByMobile(request.getMobile())) {
                user.setMobile(request.getMobile());
        }else {
            return "This mobile number already existed";
        }
        user.setGender(Gender.valueOf(request.getGender()));
        user.setShiftStartAt(DateUtils.toStringToLocalTime(request.getShiftStartAt(), Constants.TIME_FORMAT_12_HOURS));
        user.setShiftEndAt(DateUtils.toStringToLocalTime(request.getShiftEndAt(), Constants.TIME_FORMAT_12_HOURS));
        user.setShiftTimings(request.getShiftStartAt() + " - " + request.getShiftEndAt());
        if (Validator.isValid(request.getPictureId())) {
            Optional<FileEntity> optionalFileEntity = fileRepo.findByFileId(request.getPictureId());
            if (optionalFileEntity.isPresent()) {
                user.setUploadPicture(optionalFileEntity.get());
            }
        }
        user.setSearchKey(getAppUserSearchKey(user));
        appUserRepo.save(user);
        return "User Saved successfully";
    }

    @Override
    public String updateAppUser(AppUserDto request) throws ParseException {
        if (Validator.isValid(request.getId())) {
            Optional<AppUser> optionalAppUser = appUserRepo.findById(request.getId());
            if (optionalAppUser.isPresent()) {
                AppUser user = optionalAppUser.get();
                if (Validator.isValid(request.getUserName())) {
                    user.setUserName(request.getUserName());
                }
                if (user.getEmpId().equals(request.getEmpId()) || !appUserRepo.existsByEmpId(request.getEmpId())) {
                    user.setEmpId(request.getEmpId());
                }else {
                    return "This employee id already exist";
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
                if (Validator.isValid(request.getCountryCode())) {
                    user.setCountryCode(request.getCountryCode());
                }
                if (user.getMobile().equals(request.getMobile()) || !appUserRepo.existsByMobile(request.getMobile())) {
                        user.setMobile(request.getMobile());
                }else {
                   return  "This mobile number already exist";
                }
                if (user.getEmail().equals(request.getEmail()) || !appUserRepo.existsByEmail(request.getEmail())) {
                    user.setEmail(request.getEmail());
                }else{
                    return "This mail already existed";
                }
                if (Validator.isValid(request.getRoles())) {
                    user.setRoles(Roles.valueOf(request.getRoles()));
                }
                if (Validator.isValid(request.getGender())) {
                    user.setGender(Gender.valueOf(request.getGender()));
                }
                if (Validator.isObjectValid(request.getShiftStartAt())) {
                    user.setShiftStartAt(DateUtils.toStringToLocalTime(request.getShiftStartAt(), Constants.TIME_FORMAT_12_HOURS));
                }
                if (Validator.isObjectValid(request.getShiftEndAt())) {
                    user.setShiftEndAt(DateUtils.toStringToLocalTime(request.getShiftEndAt(), Constants.TIME_FORMAT_12_HOURS));
                }
                if (Validator.isValid(request.getPictureId())) {
                    Optional<FileEntity> optionalFileEntity = fileRepo.findByFileId(request.getPictureId());
                    if (optionalFileEntity.isPresent()) {
                        user.setUploadPicture(optionalFileEntity.get());
                    }
                }
                user.setShiftTimings(request.getShiftStartAt() + " - " + request.getShiftEndAt());
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
    public List<Roles> getAllEnumRoles() {
        List<Roles> rolesList = Arrays.asList(Roles.ADMIN, Roles.COUNSELLOR, Roles.DIRECTOR, Roles.EMPLOYEE, Roles.NONE, Roles.TEAM_LEADER, Roles.TEAM_MANAGER, Roles.VENDOR
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

//    @Override
//    public PaginationResponse getAllAppUserPagination(AppUserDto request) {
//        Pageable pageable = null;
//        if (Validator.isValid(request.getPageSize())) {
//            pageable = PageRequest.of(request.getPageNumber(), request.getPageSize());
//        }
//        if (request.getSortUserName() != null && request.getSortUserName().equals(Boolean.TRUE)) {
//            pageable = PageRequest.of(request.getPageNumber(), request.getPageSize(), Sort.Direction.ASC, "userName");
//        } else if (request.getSortUserName() != null && request.getSortUserName().equals(Boolean.FALSE)) {
//            pageable = PageRequest.of(request.getPageNumber(), request.getPageSize(), Sort.Direction.DESC, "userName");
//        }
//
//        Specification<CounsellorEmployee> appUserSpecification = ((root, criteriaQuery, criteriaBuilder) -> {
//            List<Predicate> addVendorPredicate = new ArrayList<>();
//            Join<CounsellorEmployee, AppUser> appUserJoin = root.join("appUser");
//            Join<CounsellorEmployee, Counsellor> counsellorJoin = root.join("counsellor");
//            if (request.getUserName() != null) {
//                Predicate userName = criteriaBuilder.and(appUserJoin.get("userName").in(request.getUserName()));
//                addVendorPredicate.add(userName);
//            }
//            if (request.getEmpId() != null && !request.getEmpId().isEmpty()) {
//                Predicate empId = criteriaBuilder.and(appUserJoin.get("empId").in(request.getEmpId()));
//                addVendorPredicate.add(empId);
//            }
//            if (request.getRoles() != null) {
//                Predicate roles = criteriaBuilder.and(appUserJoin.get("roles").in(request.getRoles()));
//                addVendorPredicate.add(roles);
//            }
//            if (request.getAlertList() != null && !request.getAlertList().isEmpty()) {
//                Predicate alerts = criteriaBuilder.and(appUserJoin.get("alerts").in(request.getAlertList()));
//                addVendorPredicate.add(alerts);
//            }
//
//            if (request.getCounsellorId() != null) {
//                Predicate counsellorName = criteriaBuilder.and(counsellorJoin.get("id").in(request.getCounsellorId()));
//                addVendorPredicate.add(counsellorName);
//            }
//            if (request.getShiftTimingsList() != null && !request.getShiftTimingsList().isEmpty()) {
//                Predicate shiftTimings = criteriaBuilder.and(root.get("shiftTimings").in(request.getShiftTimingsList()));
//                addVendorPredicate.add(shiftTimings);
//            }
//            if (Validator.isValid(request.getSearchKey())) {
//                Predicate searchPredicate = criteriaBuilder.like(
//                        criteriaBuilder.lower(root.get("searchKey")),
//                        "%" + request.getSearchKey().toLowerCase() + "%");
//                addVendorPredicate.add(searchPredicate);
//            }
//            return criteriaBuilder.and(addVendorPredicate.toArray(new Predicate[0]));
//        });
//        Page<CounsellorEmployee> appUserObjectives = counsellorEmployeeRepo.findAll(appUserSpecification, pageable);
//        if (appUserObjectives.getContent() != null) {
//            PaginationResponse paginationResponse = new PaginationResponse();
//            paginationResponse.setCounsellorEmployeeList(appUserObjectives.getContent());
//            paginationResponse.setTotalPages(appUserObjectives.getTotalPages());
//            paginationResponse.setTotalElements(appUserObjectives.getTotalElements());
//            return paginationResponse;
//        }
//        return new PaginationResponse();
//    }

    @Override
    public PaginationResponse getAllAppUserPagination(AppUserDto request) {
        Pageable pageable = null;
        if (request.getPageSize() != null && request.getPageNumber() != null) {
            pageable = PageRequest.of(request.getPageNumber(), request.getPageSize());
        }
        if (request.getPageSize() != null && request.getPageNumber() != null) {
            pageable = PageRequest.of(request.getPageNumber(), request.getPageSize(), Sort.Direction.DESC, "createdOn");
        }
        if (request.getSortUserName() != null && request.getSortUserName().equals(Boolean.TRUE)) {
            pageable = PageRequest.of(request.getPageNumber(), request.getPageSize(), Sort.Direction.ASC, "userName");
        } else if (request.getSortUserName() != null && request.getSortUserName().equals(Boolean.FALSE)) {
            pageable = PageRequest.of(request.getPageNumber(), request.getPageSize(), Sort.Direction.DESC, "userName");
        }

        Specification<AppUser> appUserSpecification = ((root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> addUserPredicate = new ArrayList<>();
            Join<AppUser, Site> siteJoin =root.join("site");
            if (request.getUserName() != null) {
                Predicate userName = criteriaBuilder.and(root.get("userName").in(request.getUserName()));
                addUserPredicate.add(userName);
            }
            if (request.getEmpId() != null && !request.getEmpId().isEmpty()) {
                Predicate empId = criteriaBuilder.and(root.get("empId").in(request.getEmpId()));
                addUserPredicate.add(empId);
            }
            if (request.getRoles() != null && request.getRoles().isEmpty()) {
                Predicate roles = criteriaBuilder.and(root.get("roles").in(request.getRoles()));
                addUserPredicate.add(roles);
            }
            if (request.getAlertList() != null && !request.getAlertList().isEmpty()) {
                Predicate alerts = criteriaBuilder.and(root.get("alerts").in(request.getAlertList()));
                addUserPredicate.add(alerts);
            }

            if (request.getRoles() != null && request.getRoles().equalsIgnoreCase(Roles.COUNSELLOR.toString())) {
                Predicate roles = criteriaBuilder.and(root.get("counsellorName").in(request.getCounsellorId()));
                addUserPredicate.add(roles);
            }
            if (request.getShiftTimingsList() != null && !request.getShiftTimingsList().isEmpty()) {
                Predicate shiftTimings = criteriaBuilder.and(root.get("shiftTimings").in(request.getShiftTimingsList()));
                addUserPredicate.add(shiftTimings);
            }
            if(request.getSiteId() != null){
                Predicate site = criteriaBuilder.and(siteJoin.get("id").in(request.getSiteId()));
                addUserPredicate.add(site);
            }
            Predicate isDeletedPredicate = criteriaBuilder.equal(root.get("isDeleted"), Boolean.FALSE);
            addUserPredicate.add(isDeletedPredicate);
            if (Validator.isValid(request.getSearchKey())) {
                Predicate searchPredicate = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("searchKey")),
                        "%" + request.getSearchKey().toLowerCase() + "%");
                addUserPredicate.add(searchPredicate);
            }
            return criteriaBuilder.and(addUserPredicate.toArray(new Predicate[0]));
        });
        PaginationResponse paginationResponse = new PaginationResponse();
        if (request.getPageSize() != null && request.getPageNumber() != null) {
            Page<AppUser> appUserObjectives = appUserRepo.findAll(appUserSpecification, pageable);
            if (appUserObjectives.getContent() != null) {
                paginationResponse.setAppUserList(appUserObjectives.getContent());
                paginationResponse.setTotalPages(appUserObjectives.getTotalPages());
                paginationResponse.setTotalElements(appUserObjectives.getTotalElements());
                return paginationResponse;
            }
        } else {
            List<AppUser> appUserList = appUserRepo.findAll(appUserSpecification);
            paginationResponse.setAppUserList(appUserList.stream().filter(team -> team.getIsDeleted().equals(Boolean.FALSE)).collect(Collectors.toList()));
            return paginationResponse;
        }
        return new PaginationResponse();
    }

    @Override
    public String saveCounsellorEmployeeInfo(CounsellorEmployeeDto request) {
        CounsellorEmployee counsellorEmployee = new CounsellorEmployee();
        if (Validator.isValid(request.getAppUserId())) {
            Optional<AppUser> optionalAppUser= appUserRepo.findById(request.getAppUserId());
            if(optionalAppUser.isPresent()) {
                counsellorEmployee.setAppUser(optionalAppUser.get());
            }
        }
        if(Validator.isValid(request.getCounsellorId())) {
            Optional<Counsellor> counsellorOptional = counsellorRepo.findById(request.getCounsellorId());
            if (counsellorOptional.isPresent()) {
                counsellorEmployee.setCounsellor(counsellorOptional.get());

            }
        }
        counsellorEmployeeRepo.save(counsellorEmployee);
        return "counsellor employee information saved successfully";
    }

    public String getAppUserSearchKey(AppUser appUser) {
        String searchKey = "";
        if (appUser.getUserName() != null) {
            searchKey = searchKey + " " + appUser.getUserName();
        }
        if (appUser.getRoles() != null) {
            searchKey = searchKey + " " + appUser.getRoles();
        }
        if (appUser.getGender() != null) {
            searchKey = searchKey + " " + appUser.getGender();
        }
        if (appUser.getShiftTimings() != null) {
            searchKey = searchKey + " " + appUser.getShiftTimings();
        }
        if (appUser.getIsActive() != null) {
            searchKey = searchKey + " " + appUser.getIsActive();
        }
        if (appUser.getCreatedOn() != null) {
            searchKey = searchKey + " " + appUser.getCreatedOn();
        }
        if (appUser.getEmpId() != null) {
            searchKey = searchKey + " " + appUser.getEmpId();
        }
        if (appUser.getEmail() != null) {
            searchKey = searchKey + " " + appUser.getEmail();
        }
        if (appUser.getDateOfJoining() != null) {
            searchKey = searchKey + " " + appUser.getDateOfJoining();
        }
        if (appUser.getShiftEndAt() != null) {
            searchKey = searchKey + " " + appUser.getShiftEndAt();
        }
        if (appUser.getIsActive() != null) {
            searchKey = searchKey + " " + appUser.getShiftEndAt();
        }
        if (appUser.getMobile() != null) {
            searchKey = searchKey + " " + appUser.getUserName();
        }
        if (appUser.getCountryCode() != null) {
            searchKey = searchKey + " " + appUser.getCountryCode();
        }
        if (appUser.getSite() != null) {
            searchKey = searchKey + " " + appUser.getSite().getSiteName();
        }
        return searchKey;
    }
}
