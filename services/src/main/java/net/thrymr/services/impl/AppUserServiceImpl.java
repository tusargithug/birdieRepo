package net.thrymr.services.impl;


import net.thrymr.constant.Constants;
import net.thrymr.dto.AppUserDto;
import net.thrymr.dto.RolesDto;
import net.thrymr.dto.UserCourseDto;
import net.thrymr.enums.Roles;
import net.thrymr.model.*;
import net.thrymr.model.master.Course;
import net.thrymr.model.master.MtOptions;
import net.thrymr.model.master.MtRoles;
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
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class AppUserServiceImpl implements AppUserService {
    private final Logger logger = LoggerFactory.getLogger(AppUserServiceImpl.class);

    private final AppUserRepo appUserRepo;

    private final Environment environment;

    private final RoleRepo roleRepo;

    private final UserCourseRepo userCourseRepo;

    private final CourseRepo courseRepo;

    private final MtOptionsRepo mtOptionsRepo;

    private final SiteRepo siteRepo;

    private final ShiftTimingsRepo shiftTimingsRepo;

    private final TeamRepo teamRepo;

    public AppUserServiceImpl(AppUserRepo appUserRepo, Environment environment, RoleRepo roleRepo, UserCourseRepo userCourseRepo, CourseRepo courseRepo, MtOptionsRepo mtOptionsRepo, SiteRepo siteRepo, ShiftTimingsRepo shiftTimingsRepo, CounsellorSlotRepo counsellorSlotRepo, TeamRepo teamRepo) {
        this.appUserRepo = appUserRepo;
        this.environment = environment;
        this.roleRepo = roleRepo;
        this.userCourseRepo = userCourseRepo;
        this.courseRepo = courseRepo;
        this.mtOptionsRepo = mtOptionsRepo;
        this.siteRepo = siteRepo;
        this.shiftTimingsRepo = shiftTimingsRepo;
        this.teamRepo = teamRepo;
    }

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
        System.out.println(request);
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
    public String createAppUser(AppUserDto request) {
        AppUser user = new AppUser();
        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setUserName(request.getFirstName() + " " + request.getLastName());
        user.setMobile(request.getMobile());
        user.setAlternateMobile(request.getAlternateMobile());
        user.setEmpId(request.getEmpId());
        user.setRoles(Roles.valueOf(request.getRoles()));
        user.setLanguages(request.getLanguages());
        Optional<Site> optionalSite = siteRepo.findById(request.getSiteId());
        if (optionalSite.isPresent()) {
            user.setSite(optionalSite.get());
        }
        user.setEducationDetails(request.getEducationDetails());
        Optional<ShiftTimings> optionalShiftTimings = shiftTimingsRepo.findById(request.getShiftTimingsId());
        if (optionalShiftTimings.isPresent()) {
            user.setShiftTimings(optionalShiftTimings.get());
        }
        if (request.getTeamId() != null) {
            Optional<Team> optionalTeamId = teamRepo.findById(request.getTeamId());
            if (optionalTeamId.isPresent()) {
                user.setTeam(optionalTeamId.get());
            }
        }
        user.setBio(request.getBio());
        appUserRepo.save(user);
        return "User Saved successfully";
    }

    @Override
    public String updateAppUser(AppUserDto request) {
        Optional<AppUser> optionalAppUser = appUserRepo.findById(request.getId());
        if (optionalAppUser.isPresent()) {
            AppUser user = optionalAppUser.get();
            if (request.getEmpId() != null) {
                user.setEmpId(request.getEmpId());
            }
            if (request.getMobile() != null) {
                user.setMobile(request.getMobile());
            }

            // user.setPassword(request.getPassword());
            if (request.getLastName() != null) {
                user.setLastName(request.getLastName());
            }
            if (request.getFirstName() != null) {
                user.setFirstName(request.getFirstName());
            }
            if (request.getEmail() != null) {
                user.setEmail(request.getEmail());
            }

            if (request.getAlternateMobile() != null) {
                user.setAlternateMobile(request.getAlternateMobile());
            }
            appUserRepo.save(user);
            return "User updated successfully";
        }
        return "No data found";
    }

    @Override
    public String deleteAppUserById(Long id) {
        Optional<AppUser> optionalAppUser = appUserRepo.findById(id);
        optionalAppUser.ifPresent(appUserRepo::delete);
        return "User deleted successfully";
    }

    @Override
    public String createUserCourse(UserCourseDto request) throws ParseException {
        // AppUser user= CommonUtil.getAppUser();
        UserCourse userCourse = new UserCourse();
        //userCourse.setUser(new AppUser());


        Optional<Course> optionalCourse = courseRepo.findById(request.getCourseId());
        optionalCourse.ifPresent(userCourse::setCourse);
        List<MtOptions> mtOptionsList = mtOptionsRepo.findAllByIdIn(request.getMtOptionsIds());
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
        userCourseRepo.save(userCourse);
        return "Saved Successfully";
    }


    @Override
    public AppUser getAppUserById(Long id) {
        return appUserRepo.findById(id).orElse(null);
    }

    @Override
    public List<AppUser> getAllAppUsers() {
        return appUserRepo.findAll();
    }

    @Override
    public List<Roles> getAllEnumRoles() {
        List<Roles> rolesList = Arrays.asList(Roles.ADMIN,
                Roles.COUNSELLOR,
                Roles.DIRECTOR,
                Roles.EMPLOYEE,
                Roles.NONE,
                Roles.OP_STREAM,
                Roles.TEAM_LEADER,
                Roles.TEAM_MANAGER,
                Roles.VENDOR,
                Roles.WELL_BEING_MANGER);
        return rolesList;
    }
}
