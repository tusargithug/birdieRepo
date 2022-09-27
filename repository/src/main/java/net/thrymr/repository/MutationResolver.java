package net.thrymr.repository;

import graphql.kickstart.tools.GraphQLMutationResolver;
import net.thrymr.dto.AppUserDto;
import net.thrymr.dto.request.MoodSourceIntensityRequestDto;
import net.thrymr.model.AppUser;
import net.thrymr.model.UserMoodCheckIn;
import net.thrymr.model.UserMoodSourceCheckedIn;
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

@Component
public class MutationResolver implements GraphQLMutationResolver {

private final AppUserRepo appUserRepo;

private final MoodIntensityRepo moodIntensityRepo;

private final UserMoodCheckInRepo userMoodCheckInRepo;

    private final Environment environment;

    private final MoodSourceRepo moodSourceRepo;

    private final UserMoodSourceCheckInRepo userMoodSourceCheckInRepo;


    public MutationResolver(AppUserRepo appUserRepo, MoodIntensityRepo moodIntensityRepo, UserMoodCheckInRepo userMoodCheckInRepo, Environment environment, MoodSourceRepo moodSourceRepo, UserMoodSourceCheckInRepo userMoodSourceCheckInRepo) {
        this.appUserRepo = appUserRepo;
        this.moodIntensityRepo = moodIntensityRepo;
        this.userMoodCheckInRepo = userMoodCheckInRepo;
        this.environment = environment;
        this.moodSourceRepo = moodSourceRepo;
        this.userMoodSourceCheckInRepo = userMoodSourceCheckInRepo;
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
    }
