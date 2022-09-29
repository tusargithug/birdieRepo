package net.thrymr.repository;

import graphql.kickstart.tools.GraphQLMutationResolver;
import net.thrymr.dto.AppUserDto;
import net.thrymr.dto.request.MoodSourceIntensityRequestDto;
import net.thrymr.model.AppUser;
import net.thrymr.model.UserMoodCheckIn;
import net.thrymr.model.UserMoodSourceCheckedIn;
import net.thrymr.model.master.Category;
import net.thrymr.model.master.Course;
import net.thrymr.model.master.MtMoodIntensity;
import net.thrymr.model.master.MtMoodSource;
import net.thrymr.utils.CommonUtil;
import net.thrymr.utils.Validator;
import org.springframework.core.env.Environment;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.Arguments;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Component;


import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
public class MutationResolver implements GraphQLMutationResolver {

private final AppUserRepo appUserRepo;

private final MoodIntensityRepo moodIntensityRepo;

private final UserMoodCheckInRepo userMoodCheckInRepo;

    private final Environment environment;

    private final MoodSourceRepo moodSourceRepo;

    private final UserMoodSourceCheckInRepo userMoodSourceCheckInRepo;
    
    private final CategoryRepo categoryRepo;

    private final CourseRepo courseRepo;

    public MutationResolver(AppUserRepo appUserRepo, MoodIntensityRepo moodIntensityRepo, UserMoodCheckInRepo userMoodCheckInRepo, Environment environment, MoodSourceRepo moodSourceRepo, UserMoodSourceCheckInRepo userMoodSourceCheckInRepo,CategoryRepo categoryRepo,CourseRepo courseRepo) {
        this.appUserRepo = appUserRepo;
        this.moodIntensityRepo = moodIntensityRepo;
        this.userMoodCheckInRepo = userMoodCheckInRepo;
        this.environment = environment;
        this.moodSourceRepo = moodSourceRepo;
        this.userMoodSourceCheckInRepo = userMoodSourceCheckInRepo;
        this.categoryRepo=categoryRepo;
        this.courseRepo=courseRepo;
    }

    @MutationMapping
    public String createAppUser(String firstName,String lastName,String email,String mobile,String alternateMobile ,String empId ){
        AppUser user=new AppUser();
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setMobile(mobile);
        user.setAlternateMobile(alternateMobile);
        user.setEmpId(empId);
        appUserRepo.save(user);
        return "User Saved successfully";
    }

    @MutationMapping
    public String  updateAppUser(@Arguments AppUserDto request){
        Optional<AppUser>optionalAppUser=appUserRepo.findById(request.getId());
        if(optionalAppUser.isPresent()){
            AppUser user= optionalAppUser.get();
            if(request.getEmpId()!=null){
                user.setEmpId(request.getEmpId());
            }
            if(request.getMobile()!=null) {
                user.setMobile(request.getMobile());
            }

            // user.setPassword(request.getPassword());
            if(request.getLastName()!=null) {
                user.setLastName(request.getLastName());
            }
            if(request.getFirstName()!=null) {
                user.setFirstName(request.getFirstName());
            }
            if(request.getEmail()!=null) {
                user.setEmail(request.getEmail());
            }

            if(request.getAlternateMobile()!=null) {
                user.setAlternateMobile(request.getAlternateMobile());
            }



            appUserRepo.save(user);
            return "User updated successfully";
        }
        return "No data found";
    }



    //delete App user by id
    @MutationMapping
    public String deleteAppUserById(Long id){
        Optional<AppUser>optionalAppUser=appUserRepo.findById(id);
        optionalAppUser.ifPresent(appUserRepo::delete);
        return "User deleted successfully";
    }


    @MutationMapping
    public String createUserMoodCheckIn(MoodSourceIntensityRequestDto request){
        AppUser user= CommonUtil.getAppUser();
        Optional<MtMoodIntensity>optionalMoodIntensity=moodIntensityRepo.findById(request.getIntensityId());
        UserMoodCheckIn userMoodCheckIn=new UserMoodCheckIn();
        userMoodCheckIn.setAppUser(user);
        if(optionalMoodIntensity.isPresent()){
            if(Validator.isValid(request.getIntensityDescription())){
                optionalMoodIntensity.get().setDescription(request.getIntensityDescription());
            }
            userMoodCheckIn.setIntensities(optionalMoodIntensity.stream().toList());
        }
        if(Validator.isValid(request.getDescription())){
            userMoodCheckIn.setDescription(request.getDescription());
        }

        userMoodCheckInRepo.save(userMoodCheckIn);

        return environment.getProperty("USER_CHECKED_IN_SAVE");
    }
    @MutationMapping
    public String deleteUserMoodCheckInById(@Argument Long id){
        Optional<MtMoodIntensity>optionalMtMoodIntensity=moodIntensityRepo.findById(id);
        optionalMtMoodIntensity.ifPresent(moodIntensityRepo::delete);
        return "Intensity deleted successfully";
    }

    @MutationMapping
    public String createUserMoodSourceCheckIn(MoodSourceIntensityRequestDto request) {
        AppUser user= CommonUtil.getAppUser();

        List<MtMoodSource> mtMoodSourceList = moodSourceRepo.findAllByIdIn(request.getSourceIds());
        UserMoodSourceCheckedIn checkedIn = new UserMoodSourceCheckedIn();
        checkedIn.setAppUser(user);
        if (!mtMoodSourceList.isEmpty()) {
            checkedIn.setSources(mtMoodSourceList);
        }
        if (Validator.isValid(request.getDescription())) {
            checkedIn.setDescription(request.getDescription());
        }
        userMoodSourceCheckInRepo.save(checkedIn);
        return environment.getProperty("MOOD_SOURCE_UPDATED");
    }

    @MutationMapping
    public String deleteUserMoodSourceCheckInById(@Argument Long id){
        Optional<MtMoodSource>optionalMtMoodSource=moodSourceRepo.findById(id);
        optionalMtMoodSource.ifPresent(moodSourceRepo::delete);
        return "Source deleted successfully";
    }
    
    @MutationMapping
    public String createCategory(Long id,String name,String description,Integer sequence) {
    	Category category=new Category();
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

