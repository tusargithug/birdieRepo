package net.thrymr.services.impl;
        import graphql.kickstart.tools.GraphQLMutationResolver;
        import net.thrymr.dto.*;
        import net.thrymr.dto.request.MoodSourceIntensityRequestDto;
        import net.thrymr.dto.slotRequest.TimeSlotDto;
        import net.thrymr.model.master.Category;
        import net.thrymr.model.master.Course;
        import net.thrymr.services.*;
        import org.springframework.graphql.data.method.annotation.Argument;
        import org.springframework.graphql.data.method.annotation.MutationMapping;
        import org.springframework.stereotype.Component;
        import java.text.ParseException;

@Component
public class MutationResolver implements GraphQLMutationResolver {


    private final MoodSourceService moodSourceService;

    private final RoleService roleService;

    private final AppUserService appUserService;

    private final MoodIntensityService moodIntensityService;

    private final CategoryService categoryService;

    private final CourseService courseService;

    private final CounsellorSlotService counsellorSlotService;


    public MutationResolver(MoodSourceService moodSourceService, RoleService roleService, AppUserService appUserService, MoodIntensityService moodIntensityService, CategoryService categoryService, CourseService courseService, CounsellorSlotService counsellorSlotService) {
        this.moodSourceService = moodSourceService;
        this.roleService = roleService;
        this.appUserService = appUserService;
        this.moodIntensityService = moodIntensityService;
        this.categoryService = categoryService;
        this.courseService = courseService;
        this.counsellorSlotService = counsellorSlotService;
    }

    @MutationMapping(name = "createAppUser")
    public String createAppUser(AppUserDto request){
        return  appUserService.createAppUser(request);
    }

    @MutationMapping(name = "updateAppUser")
    public String  updateAppUser(AppUserDto request){
        return appUserService.updateAppUser(request);
    }



    //delete App user by id
    @MutationMapping("deleteAppUserById")
    public String deleteAppUserById(Long id){
        return appUserService.deleteAppUserById(id);
    }


    @MutationMapping("createUserMoodCheckIn")
    public String createUserMoodCheckIn(MoodSourceIntensityRequestDto request){
        return moodIntensityService.createUserMoodCheckIn(request);

    }
    @MutationMapping("deleteUserMoodCheckInById")
    public String deleteUserMoodCheckInById( Long id){
        return moodIntensityService.deleteUserMoodCheckInById(id);


    }

    @MutationMapping("createUserMoodSourceCheckIn")
    public String createUserMoodSourceCheckIn(MoodSourceIntensityRequestDto request) {
        return   moodSourceService.createUserMoodSourceCheckIn(request);
    }

    @MutationMapping("deleteUserMoodSourceCheckInById")
    public String deleteUserMoodSourceCheckInById( Long id){
        return   moodSourceService.deleteUserMoodSourceCheckInById( id);
    }

    @MutationMapping("createUserCourse")
    public String createUserCourse( UserCourseDto request) throws ParseException {
        return appUserService.createUserCourse(request);


    }
    @MutationMapping("updateCategory")
    public Category updateCategory( CategoryDto request){
        return categoryService.updateCategory(request);

    }

    @MutationMapping
    public String deleteCategoryById(@Argument Long id){

    return categoryService.deleteCategoryById(id);

    }
    @MutationMapping("createCategory")
    public String createCategory(CategoryDto request) {
        return categoryService.createCategory(request);

    }



    @MutationMapping("createCourse")
    public String createCourse( CourseDto request) {
      return courseService.createCourse(request);

    }

    @MutationMapping
    public Course updateCourse(CourseDto request){
        return courseService. updateCourse(request);

    }

    @MutationMapping
    public String deleteCourseById( Long id){

        return categoryService.deleteCourseById(id);

    }

    @MutationMapping("createRole")
    public String createRole( RolesDto request){
        return roleService.createRole(request);
    }

    @MutationMapping( "updateRole")
    public String updateRole( RolesDto request){
        return roleService.updateRole(request);
    }

    @MutationMapping
    public String deleteRoleById( Long id){

        return roleService.deleteRoleById(id);

    }

//    @MutationMapping(name ="createCounsellorSlot")
//    private String createCounsellorSlot( TimeSlotDto request){
//        return counsellorSlotService.createCounsellorSlot(request);
//    }

}


