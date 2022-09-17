package net.thrymr.services.impl;


import net.thrymr.dto.AppUserDto;
import net.thrymr.dto.RolesDto;
import net.thrymr.model.AppUser;
import net.thrymr.model.Roles;
import net.thrymr.repository.AppUserRepo;
import net.thrymr.repository.RoleRepository;
import net.thrymr.services.AppUserService;

import net.thrymr.utils.ApiResponse;
import net.thrymr.utils.CommonUtil;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class AppUserServiceImpl implements AppUserService {
    private  final Logger logger = LoggerFactory.getLogger(AppUserServiceImpl.class);

    private final AppUserRepo appUserRepo;

    private final Environment environment;

    private final RoleRepository roleRepository;

    public AppUserServiceImpl(AppUserRepo appUserRepo, Environment environment, RoleRepository roleRepository) {
        this.appUserRepo = appUserRepo;
        this.environment = environment;
        this.roleRepository = roleRepository;
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
                            Optional<Roles> optionalRoles = roleRepository.findById(Long.valueOf(getCellValue(row.getCell(8))));
                            logger.info("optionalRole{}: " , CommonUtil.getStringFromObject(optionalRoles));
                            optionalRoles.ifPresent(role -> appUser.setRoles(role));
                        }
                        if (row.getCell(9) != null) {
                            appUser.setEmpId(getCellValue(row.getCell(9)));
                        }
                        setUserSearchKey(appUser);
                        appUsers.add(appUser);
                    } catch (Exception e) {
                        logger.error("Exception{} " , e);
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
        List<Roles> rolesOptional = roleRepository.findAll();
        List<RolesDto> rolesDtoList;
        rolesDtoList = rolesOptional.stream().map(this::entityToDto).collect(Collectors.toList());

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

        if(optionalAppUser.isPresent()){
         AppUser user=optionalAppUser.get();
            AppUserDto dto=entityToDto(user);
            return new ApiResponse(HttpStatus.OK,dto);
        }else {
            return new ApiResponse(HttpStatus.OK, environment.getProperty("USER_NOT_FOUND"));
        }


    }

    @Override
    public ApiResponse getAllUsers() {

        List<AppUser>appUserList=appUserRepo.findAll();
        List<AppUserDto>appUserDtoList=appUserList.stream().map(this::entityToDto).toList();
        return new ApiResponse(HttpStatus.OK,appUserDtoList);
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
    private RolesDto entityToDto(Roles request) {
        RolesDto dto = new RolesDto();
        dto.setName(request.getName());
        dto.setUsers(request.getUsers());
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
        if (request.getRoles() != null && !request.getRoles().getName().isEmpty()) {
            appUser.setRoles(request.getRoles());
        }
        return appUser;
    }

    private AppUserDto entityToDto(AppUser user){
        AppUserDto dto=new AppUserDto();
        dto.setId(user.getId());
        dto.setUserName(user.getUserName());
        dto.setMobile(user.getMobile());
        dto.setEmail(user.getEmail());

        return dto;
    }
}
