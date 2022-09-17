package net.thrymr.controller;
import net.thrymr.dto.AppUserDto;
import net.thrymr.services.AppUserService;
import net.thrymr.services.*;
import net.thrymr.utils.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;



/*
 *@author Chanda Veeresh
 *@version 1.0
 *@since  08-07-2022
 */
@RestController
@RequestMapping("/api/v1")
public class MasterController {
    private final Logger logger=LoggerFactory.getLogger(MasterController.class);


    private final RoleService roleService;

    private final AppUserService appUserService;

    private final  MoodInfoService moodInfoService;

    private final  MoodIntensityService moodIntensityService;

    private final MoodSourceService moodSourceService;

    public MasterController(RoleService roleService, AppUserService appUserService, MoodInfoService moodInfoService, MoodIntensityService moodIntensityService, MoodSourceService moodSourceService) {
        this.roleService = roleService;
        this.appUserService = appUserService;
        this.moodInfoService = moodInfoService;
        this.moodIntensityService = moodIntensityService;
        this.moodSourceService = moodSourceService;
    }


    @GetMapping("/master/role/save")
    public ResponseEntity<ApiResponse> saveRole() {
        logger.info("save role service started");
        ApiResponse apiResponse = roleService.saveRole();
        logger.info("save role service completed");
        return new ResponseEntity<>(apiResponse.getStatus());     //apiResponse.getStatus()
    }

    @GetMapping("/master/role/get")
    public ApiResponse getAllRoles() {
        logger.info("get all roles service started");
        ApiResponse apiResponse = roleService.getAllUserRoles();
        logger.info("get all roles service completed");
        return new ApiResponse(HttpStatus.OK, "All Roles",apiResponse);
    }

    @PostMapping("/master/user/save")
    public ResponseEntity<ApiResponse> saveUser(@RequestBody AppUserDto request) {
        logger.info("Save user Service Started");
        ApiResponse apiResponse = appUserService.saveUser(request);
        logger.info("Save user Service Completed");
        return new ResponseEntity<>(apiResponse, apiResponse.getStatus());
    }

    @DeleteMapping("/master/user/delete/{id}")
    public ResponseEntity<ApiResponse> deleteUserById(@PathVariable Long id) {
        logger.info("Delete user service Started");
        ApiResponse apiResponse = appUserService.deleteUserById(id);
        logger.info("Delete user Service Completed");
        return new ResponseEntity<>(apiResponse, apiResponse.getStatus());
    }

    @GetMapping("/master/user/get/{id}")
    public ResponseEntity<ApiResponse> getUserById(@PathVariable Long id) {
        logger.info("get user service Started");
        ApiResponse apiResponse = appUserService.getUserById(id);
        logger.info("get user Service Completed");
        return new ResponseEntity<>(apiResponse, apiResponse.getStatus());
    }
    @GetMapping("/master/user/get/all")
    public ResponseEntity<ApiResponse> getAllUsers() {
        logger.info("get all user service Started");
        ApiResponse apiResponse = appUserService.getAllUsers();
        logger.info("get all user Service Completed");
        return new ResponseEntity<>(apiResponse, apiResponse.getStatus());
    }

    @PostMapping("/master/user/upload")
    public ResponseEntity<ApiResponse> importUsersData(@RequestParam("file") MultipartFile file) {
        logger.info("Import  Users Data Service Started");
        ApiResponse apiResponse = appUserService.addUsersByExcel(file);
        logger.info("Import Users Data Service Completed");
        return new ResponseEntity<>(apiResponse, apiResponse.getStatus());
    }

    @PostMapping("master/mood-info/save")
    public ResponseEntity<ApiResponse> importMoodInfo(@RequestParam("file") MultipartFile file) {
        logger.info("Import  Users Data Service Started");
        ApiResponse apiResponse = moodInfoService.saveMoodInfo(file);
        logger.info("Import Users Data Service Completed");
        return new ResponseEntity<>(apiResponse, apiResponse.getStatus());
    }
    @PostMapping("master/mood-intensity/save")
    public ResponseEntity<ApiResponse> importMoodIntensitiesInfo(@RequestParam("file") MultipartFile file) {
        logger.info("Import  Users Data Service Started");
        ApiResponse apiResponse = moodIntensityService.saveintensity(file);
        logger.info("Import Users Data Service Completed");
        return new ResponseEntity<>(apiResponse, apiResponse.getStatus());
    }
    @GetMapping("/master/mood-info/get")
    public ApiResponse getAllMoodNames() {
        logger.info("get all roles service started");
        ApiResponse apiResponse = moodInfoService.getAllMoods();
        logger.info("get all roles service completed");
        return new ApiResponse(HttpStatus.OK, "All MoodInfo details",apiResponse);
    }

    @PostMapping("/master/mood-source/save")
    public ResponseEntity<ApiResponse> importMoodSourceInfo(@RequestParam("file") MultipartFile file) {
        logger.info("Import  Mood Source Data Service Started");
        ApiResponse apiResponse = moodSourceService.addMoodSourceByExcel(file);
        logger.info("Import Mood Source Data Service Completed");
        return new ResponseEntity<>(apiResponse, apiResponse.getStatus());
    }

    @GetMapping("/master/mood-source/get")
    public ApiResponse getAllMoodSources() {
        logger.info("get all mood sources service started");
        ApiResponse apiResponse = moodSourceService.getAllMoodSources();
        logger.info("get all mood sources service completed");
        return new ApiResponse(HttpStatus.OK, "All MoodSources details",apiResponse);
    }
}
