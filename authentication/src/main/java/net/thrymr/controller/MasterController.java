package net.thrymr.controller;
import net.thrymr.dto.*;
import net.thrymr.model.master.Category;
import net.thrymr.model.master.Course;
import net.thrymr.model.master.MtRoles;
import net.thrymr.repository.CategoryRepo;
import net.thrymr.repository.CourseRepo;
import net.thrymr.repository.RolesRepo;
import net.thrymr.services.*;
import net.thrymr.utils.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/v1/master")
public class MasterController {
    private final Logger logger=LoggerFactory.getLogger(MasterController.class);


    private final RoleService roleService;

    private final AppUserService appUserService;

    private final MoodInfoService moodInfoService;

    private final MoodIntensityService moodIntensityService;

    private final MoodSourceService moodSourceService;

    private final LearningVideoService learningVideoService;

    private final RolesRepo rolesRepo;

    private final CategoryRepo categoryRepo;


    private final CourseRepo courseRepo;

    private final CategoryService categoryService;

    private final CourseService courseService;

    public MasterController(RoleService roleService, AppUserService appUserService, MoodInfoService moodInfoService, MoodIntensityService moodIntensityService, MoodSourceService moodSourceService, LearningVideoService learningVideoService, RolesRepo rolesRepo, CategoryRepo categoryRepo, CourseRepo courseRepo, CategoryService categoryService, CourseService courseService) {
        this.roleService = roleService;
        this.appUserService = appUserService;
        this.moodInfoService = moodInfoService;
        this.moodIntensityService = moodIntensityService;
        this.moodSourceService = moodSourceService;
        this.learningVideoService = learningVideoService;
        this.rolesRepo = rolesRepo;
        this.categoryRepo = categoryRepo;
        this.courseRepo = courseRepo;
        this.categoryService = categoryService;
        this.courseService = courseService;
    }


    @GetMapping("/role/save")
        public ResponseEntity<ApiResponse> saveRole() {
            logger.info("save role service started");
            ApiResponse apiResponse = roleService.saveRole();
            logger.info("save role service completed");
            return new ResponseEntity<>(apiResponse.getStatus());     //apiResponse.getStatus()
        }

        @GetMapping("/role/get")
        public ApiResponse getAllRoles() {
            logger.info("get all roles service started");
            ApiResponse apiResponse = roleService.getAllUserRoles();
            logger.info("get all roles service completed");
            return new ApiResponse(HttpStatus.OK, "All Roles",apiResponse);
        }

        @PostMapping("/user/save")
        public ResponseEntity<ApiResponse> saveUser(@RequestBody AppUserDto request) {
            logger.info("Save user service started");
            ApiResponse apiResponse = appUserService.saveUser(request);
            logger.info("Save user service completed");
            return new ResponseEntity<>(apiResponse, apiResponse.getStatus());
        }

        @DeleteMapping("/user/delete/{id}")
        public ResponseEntity<ApiResponse> deleteUserById(@PathVariable Long id) {
            logger.info("Delete user service started");
            ApiResponse apiResponse = appUserService.deleteUserById(id);
            logger.info("Delete user service completed");
            return new ResponseEntity<>(apiResponse, apiResponse.getStatus());
        }

        @GetMapping("/user/get/{id}")
        public ResponseEntity<ApiResponse> getUserById(@PathVariable Long id) {
            logger.info("get user service started");
            ApiResponse apiResponse = appUserService.getUserById(id);
            logger.info("get user service completed");
            return new ResponseEntity<>(apiResponse, apiResponse.getStatus());
        }
        @GetMapping("/user/get/all")
        public ResponseEntity<ApiResponse> getAllUsers() {
            logger.info("get all user service started");
            ApiResponse apiResponse = appUserService.getAllUsers();
            logger.info("get all user service completed");
            return new ResponseEntity<>(apiResponse, apiResponse.getStatus());
        }

        @PostMapping("/user/upload")
        public ResponseEntity<ApiResponse> importUsersData(@RequestParam("file") MultipartFile file) {
            logger.info("Import  Users  service started");
            ApiResponse apiResponse = appUserService.addUsersByExcel(file);
            logger.info("Import Users  service completed");
            return new ResponseEntity<>(apiResponse, apiResponse.getStatus());
        }
        // mood-info uploaded by excel sheet
        @PostMapping("/mood-info/save")
        public ResponseEntity<ApiResponse> importMoodInfo(@RequestParam("file") MultipartFile file) {
            logger.info("Import  moodInfo  service started");
            ApiResponse apiResponse = moodInfoService.saveMoodInfo(file);
            logger.info("Import moodInfo  service completed");
            return new ResponseEntity<>(apiResponse, apiResponse.getStatus());
        }

        // mood-intensity uploaded by excel sheet
        @PostMapping("/mood-intensity/save")
        public ResponseEntity<ApiResponse> importMoodIntensitiesInfo(@RequestParam("file") MultipartFile file) {
            logger.info("Import  moodIntensity  service started");
            ApiResponse apiResponse = moodIntensityService.saveintensity(file);
            logger.info("Import moodIntensity  service completed");
            return new ResponseEntity<>(apiResponse, apiResponse.getStatus());
        }



        @PostMapping("/mood-source/save")
        public ResponseEntity<ApiResponse> importMoodSourceInfo(@RequestParam("file") MultipartFile file) {
            logger.info("Import  Mood Source  service started");
            ApiResponse apiResponse = moodSourceService.addMoodSourceByExcel(file);
            logger.info("Import Mood Source  service completed");
            return new ResponseEntity<>(apiResponse, apiResponse.getStatus());
        }
        // save Learning video
        @PostMapping("/learning/video/save")
        public ResponseEntity<ApiResponse> saveLearningVideo(@RequestBody LearningVideoDto request) {
            logger.info("Save video service started");
            ApiResponse apiResponse = learningVideoService.saveLearningVideo(request);
            logger.info("Save video Service Completed");
            return new ResponseEntity<>(apiResponse, apiResponse.getStatus());
        }

        @QueryMapping("getAllMtRoles")
        public List<MtRoles> getAllMtRoles(){
            return roleService. getAllMtRoles();

        }

        @QueryMapping("mtRoleById")
        public MtRoles mtRoleById(Long id){
            return roleService. mtRoleById(id);

        }

        @QueryMapping
        public List<Category> getAllCategory() {
            logger.info("get all Category service started");
            return categoryRepo.findAll();
        }

        @MutationMapping(name = "createCategory")
        public String createCategory(@Argument(name = "input")CategoryDto request) {
         return categoryService.createCategory(request);
        }


    @MutationMapping("updateCategory")
    public Category updateCategory( @Argument(name = "input") CategoryDto request){
        return categoryService.updateCategory(request);

    }

    @MutationMapping
    public String deleteCategoryById(@Argument Long id){
        return categoryService.deleteCategoryById(id);

    }
   
   @QueryMapping
   public List<Course> getAllCourse() {
       return courseRepo.findAll();
   }

   @MutationMapping(name = "updateCourse")
   public Course updateCourse(@Argument(name = "input") CourseDto request){
        return courseService.updateCourse(request);
   }


    @MutationMapping(name = "createCourse")
    public String createCourse(@Argument(name = "input") CourseDto request) {
        return courseService.createCourse(request);

    }


    @MutationMapping
    public String deleteCourseById(@Argument Long id){

        return categoryService.deleteCourseById(id);

    }

}
