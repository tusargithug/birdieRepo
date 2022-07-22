package net.thrymr.controller;





import net.thrymr.dto.AppUserDto;

import net.thrymr.service.AppUserService;
import net.thrymr.service.MoodInfoService;
import net.thrymr.service.MoodIntensityService;
import net.thrymr.service.RoleService;
import net.thrymr.utils.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/api")
public class MasterController {
    private final Logger logger;

    {
        logger = LoggerFactory.getLogger(MasterController.class);
    }

    @Autowired
    RoleService roleService;

    @Autowired
    AppUserService appUserService;
    @Autowired
    MoodInfoService moodInfoService;
    @Autowired
    MoodIntensityService moodIntensityService;


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
}
