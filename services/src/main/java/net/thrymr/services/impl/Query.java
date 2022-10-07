package net.thrymr.services.impl;
        import net.thrymr.model.AppUser;
        import net.thrymr.model.CounsellorSlot;
        import net.thrymr.model.master.*;

        import net.thrymr.repository.CategoryRepo;
        import net.thrymr.repository.CourseRepo;
        import net.thrymr.services.*;

        import org.springframework.graphql.data.method.annotation.Argument;
        import org.springframework.graphql.data.method.annotation.QueryMapping;
        import org.springframework.stereotype.Component;

        import graphql.kickstart.tools.GraphQLQueryResolver;

        import java.util.List;

@Component
public class Query implements GraphQLQueryResolver {


    private final MoodInfoService moodInfoService;

    private final MoodIntensityService moodIntensityService;

    private final AppUserService appUserService;

    private final RoleService roleService;


    private final CategoryRepo categoryRepo;

    private final CounsellorSlotService counsellorSlotService;

    private final CourseRepo courseRepo;
    public Query(AppUserService appUserService, RoleService roleService, MoodInfoService moodInfoService, MoodIntensityService moodIntensityService, CategoryRepo categoryRepo, CounsellorSlotService counsellorSlotService, CourseRepo courseRepo) {

        this.appUserService = appUserService;
        this.roleService = roleService;

        this.moodInfoService = moodInfoService;
        this.moodIntensityService = moodIntensityService;
        this.categoryRepo = categoryRepo;
        this.counsellorSlotService = counsellorSlotService;
        this.courseRepo = courseRepo;
    }

    @QueryMapping
    public MtMoodInfo moodInfoById(Long id) {
        return moodInfoService.moodInfoById(id);

    }

    @QueryMapping
    public List<MtMoodInfo> getAllMoodInfo(){
        return moodInfoService.getAllMoodInfo();

    }

    @QueryMapping("getAllMoodIntensity")
    public List<MtMoodIntensity> getAllMoodIntensity(){
        return moodIntensityService.getAllMoodIntensity();

    }

    //get user by id
    @QueryMapping
    public AppUser getAppUserById(@Argument Long id) {
        return appUserService. getAppUserById(id);

    }

    // get all Users
    @QueryMapping("getAllAppUsers")
    public List<AppUser> getAllAppUsers() {
        return appUserService. getAllAppUsers();

    }



    @QueryMapping("getAllMtRoles")
    public List<MtRoles> getAllMtRoles(){
        return roleService. getAllMtRoles();

    }

    @QueryMapping("mtRoleById")
    public MtRoles mtRoleById(Long id){
        return roleService. mtRoleById(id);
    }

    @QueryMapping("getAllCategory")
    public List<Category> getAllCategory() {
        return categoryRepo.findAll();
    }

    @QueryMapping("getAllCourse")
    public List<Course> getAllCourse() {
        return courseRepo.findAll();
    }

//    @QueryMapping("getCounsellorSlot")
//    public List<CounsellorSlot> getCounsellorSlot() {
//        return counsellorSlotService.getCounsellorSlot();
//    }
}