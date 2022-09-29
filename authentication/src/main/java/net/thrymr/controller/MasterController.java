package net.thrymr.controller;
import net.thrymr.dto.AppUserDto;
import net.thrymr.dto.LearningVideoDto;
import net.thrymr.model.master.Category;
import net.thrymr.model.master.Course;
import net.thrymr.model.master.MtRoles;
import net.thrymr.repository.CategoryRepo;
import net.thrymr.repository.CourseRepo;
import net.thrymr.repository.RolesRepo;
import net.thrymr.services.AppUserService;
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

import java.util.List;
import java.util.Optional;
import java.util.Set;


@RestController
@RequestMapping("/api/v1/master")
public class MasterController {
    private final Logger logger=LoggerFactory.getLogger(MasterController.class);


    private final RoleService roleService;

    private final AppUserService appUserService;

    private final  MoodInfoService moodInfoService;

    private final  MoodIntensityService moodIntensityService;

    private final MoodSourceService moodSourceService;

    private final LearningVideoService learningVideoService;

    private final RolesRepo rolesRepo;
    
    private final CategoryRepo categoryRepo;

    private final CourseRepo courseRepo;
    
    public MasterController(RoleService roleService, AppUserService appUserService, MoodInfoService moodInfoService, MoodIntensityService moodIntensityService, MoodSourceService moodSourceService, LearningVideoService learningVideoService, RolesRepo rolesRepo,CategoryRepo categoryRepo,CourseRepo courseRepo) {
        this.roleService = roleService;
        this.appUserService = appUserService;
        this.moodInfoService = moodInfoService;
        this.moodIntensityService = moodIntensityService;
        this.moodSourceService = moodSourceService;
        this.learningVideoService = learningVideoService;
        this.rolesRepo = rolesRepo;
        this.categoryRepo=categoryRepo;
        this.courseRepo=courseRepo;
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
        logger.info("Save user Service Started");
        ApiResponse apiResponse = appUserService.saveUser(request);
        logger.info("Save user Service Completed");
        return new ResponseEntity<>(apiResponse, apiResponse.getStatus());
    }

    @DeleteMapping("/user/delete/{id}")
    public ResponseEntity<ApiResponse> deleteUserById(@PathVariable Long id) {
        logger.info("Delete user service Started");
        ApiResponse apiResponse = appUserService.deleteUserById(id);
        logger.info("Delete user Service Completed");
        return new ResponseEntity<>(apiResponse, apiResponse.getStatus());
    }

    @GetMapping("/user/get/{id}")
    public ResponseEntity<ApiResponse> getUserById(@PathVariable Long id) {
        logger.info("get user service Started");
        ApiResponse apiResponse = appUserService.getUserById(id);
        logger.info("get user Service Completed");
        return new ResponseEntity<>(apiResponse, apiResponse.getStatus());
    }
    @GetMapping("/user/get/all")
    public ResponseEntity<ApiResponse> getAllUsers() {
        logger.info("get all user service Started");
        ApiResponse apiResponse = appUserService.getAllUsers();
        logger.info("get all user Service Completed");
        return new ResponseEntity<>(apiResponse, apiResponse.getStatus());
    }

    @PostMapping("/user/upload")
    public ResponseEntity<ApiResponse> importUsersData(@RequestParam("file") MultipartFile file) {
        logger.info("Import  Users Data Service Started");
        ApiResponse apiResponse = appUserService.addUsersByExcel(file);
        logger.info("Import Users Data Service Completed");
        return new ResponseEntity<>(apiResponse, apiResponse.getStatus());
    }
    // mood-info uploaded by excel sheet
    @PostMapping("/mood-info/save")
    public ResponseEntity<ApiResponse> importMoodInfo(@RequestParam("file") MultipartFile file) {
        logger.info("Import  Users Data Service Started");
        ApiResponse apiResponse = moodInfoService.saveMoodInfo(file);
        logger.info("Import Users Data Service Completed");
        return new ResponseEntity<>(apiResponse, apiResponse.getStatus());
    }

    // mood-intensity uploaded by excel sheet
    @PostMapping("/mood-intensity/save")
    public ResponseEntity<ApiResponse> importMoodIntensitiesInfo(@RequestParam("file") MultipartFile file) {
        logger.info("Import  Users Data Service Started");
        ApiResponse apiResponse = moodIntensityService.saveintensity(file);
        logger.info("Import Users Data Service Completed");
        return new ResponseEntity<>(apiResponse, apiResponse.getStatus());
    }



    @PostMapping("/mood-source/save")
    public ResponseEntity<ApiResponse> importMoodSourceInfo(@RequestParam("file") MultipartFile file) {
        logger.info("Import  Mood Source Data Service Started");
        ApiResponse apiResponse = moodSourceService.addMoodSourceByExcel(file);
        logger.info("Import Mood Source Data Service Completed");
        return new ResponseEntity<>(apiResponse, apiResponse.getStatus());
    }
    // save Learning video
    @PostMapping("/learning/video/save")
    public ResponseEntity<ApiResponse> saveLearningVideo(@RequestBody LearningVideoDto request) {
        logger.info("Save video Service Started");
        ApiResponse apiResponse = learningVideoService.saveLearningVideo(request);
        logger.info("Save video Service Completed");
        return new ResponseEntity<>(apiResponse, apiResponse.getStatus());
    }

    @QueryMapping
    public List<MtRoles> getAllMtRoles(){
        logger.info("get all MtRoles service started");
        return rolesRepo.findAll();
}

   @QueryMapping
    public MtRoles mtRoleById(@Argument Long id){
    return rolesRepo.findById(id).orElse(null);
}

   @QueryMapping
   public List<Category> getAllCategory() {
       logger.info("get all Category service started");
       return categoryRepo.findAll();
   }
   
   @MutationMapping
   public String createCategory(@Argument Long id,@Argument String name,@Argument String description,@Argument Integer sequence) {
   	Category category=new Category();
   	category.setId(id);
   	category.setName(name);
   	category.setDescription(description);
   	category.setSequence(sequence);
   	categoryRepo.save(category);
    return "Course Created successfully";
   	
   }
   
   @MutationMapping
   public Category updateCategory(@Argument Long id,@Argument String name,@Argument String description,@Argument Integer sequence){
       final Optional<Category> category = categoryRepo.findById(id).stream()
               .filter(c -> c.getId() == id)
               .findFirst();
           if (!category.isPresent()) {
               return null;
           }
           category.get()
               .setName(name);
           category.get()
               .setDescription(description);
           category.get()
               .setSequence(sequence);
           return categoryRepo.save(category.get());
   }
   
   @MutationMapping
   public String deleteCategoryById(@Argument Long id){
   	Optional<Category> category = categoryRepo.findById(id);
   	category.ifPresent(categoryRepo::delete);
       return "Intensity deleted successfully";
   }
   
   @QueryMapping
   public List<Course> getAllCourse() {
       return courseRepo.findAll();
   }
   
   @MutationMapping
   public String createCourse(@Argument Long id,@Argument String name,@Argument String description,@Argument Integer sequence,@Argument String code) {
   	Course course=new Course();
   	course.setName(name);
   	course.setDecription(description);
   	course.setCode(code);
   	course.setSequence(sequence);
   	courseRepo.save(course);
       return "Course Created successfully";
   	
   }
   
   @MutationMapping
   public Course updateCourse(@Argument Long id,@Argument String name,@Argument String description,@Argument Integer sequence,@Argument String code){
       final Optional<Course> course = courseRepo.findById(id).stream()
               .filter(c -> c.getId() == id)
               .findFirst();
           if (!course.isPresent()) {
               return null;
           }
           course.get()
               .setName(name);
           course.get()
               .setDecription(description);
           course.get()
               .setSequence(sequence);
           course.get().setCode(code);
           return courseRepo.save(course.get());
   }
   
   @MutationMapping
   public String deleteCourseById(@Argument Long id){
   	Optional<Course> category = courseRepo.findById(id);
   	category.ifPresent(courseRepo::delete);
       return "Intensity deleted successfully";
   }
}
