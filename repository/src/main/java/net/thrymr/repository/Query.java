package net.thrymr.repository;

import net.thrymr.model.AppUser;
import net.thrymr.model.master.MtMoodIntensity;
import net.thrymr.model.master.MtRoles;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Component;

import graphql.kickstart.tools.GraphQLQueryResolver;
import net.thrymr.model.master.Category;
import net.thrymr.model.master.Course;
import net.thrymr.model.master.MtMoodInfo;

import java.util.List;

@Component
public class Query implements GraphQLQueryResolver {

	

	private final MoodInfoRepository repository;


    private final MoodIntensityRepo moodIntensityRepo;


    private final AppUserRepo appUserRepo;

    private final RolesRepo rolesRepo;
    
    private final CategoryRepo categoryRepo;

    private final CourseRepo courseRepo;

    public Query(MoodInfoRepository repository, MoodIntensityRepo moodIntensityRepo, AppUserRepo appUserRepo, RolesRepo rolesRepo,CategoryRepo categoryRepo,CourseRepo courseRepo) {
        this.repository = repository;
        this.moodIntensityRepo = moodIntensityRepo;
        this.appUserRepo = appUserRepo;
        this.rolesRepo = rolesRepo;
        this.categoryRepo=categoryRepo;
        this.courseRepo=courseRepo;
    }

    @QueryMapping
    public MtMoodInfo moodInfoById(Long id) {
        return repository.findById(id).orElse(null);
    }

   @QueryMapping
     public List<MtMoodInfo> getAllMoodInfo(){
        return repository.findAll();
}

    @QueryMapping
    public List<MtMoodIntensity> getAllMoodIntensity(){
        return moodIntensityRepo.findAll();
    }


    @QueryMapping
    public AppUser getAppUserById(Long id){
        return   appUserRepo.findById(id).orElse(null);
    }

    @QueryMapping
    public List<AppUser> getAllAppUsers(){
        return   appUserRepo.findAll();
    }

    @QueryMapping
    public List<MtRoles> getAllMtRoles(){
        return rolesRepo.findAll();
    }

    @QueryMapping
    public MtRoles mtRoleById(Long id){
        return rolesRepo.findById(id).orElse(null);
    }
    
    @QueryMapping
    public List<Category> getAllCategory() {
        return categoryRepo.findAll();
    }

    @QueryMapping
    public List<Course> getAllCourse() {
        return courseRepo.findAll();
    }
}