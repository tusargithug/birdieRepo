package net.thrymr.controller;
import net.thrymr.dto.request.MoodSourceIntensityRequestDto;
import net.thrymr.model.UserMoodCheckIn;
import net.thrymr.model.master.MtMoodIntensity;
import net.thrymr.services.MoodIntensityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
public class MoodIntensityController {
    private final Logger logger = LoggerFactory.getLogger(MoodIntensityController.class);

    private final MoodIntensityService moodIntensityService;


    public MoodIntensityController(MoodIntensityService moodIntensityService) {
        this.moodIntensityService = moodIntensityService;
    }

    @QueryMapping(name = "getMoodIntensitiesById")
    public MtMoodIntensity getMoodIntensitiesById(@Argument Long id) {
        return  moodIntensityService.getMoodIntensitiesById(id);
    }

    @MutationMapping(name = "deleteMoodIntensitiesById")
    public String deleteMoodIntensitiesById(@Argument Long id) {
        return moodIntensityService.deleteMoodIntensitiesById(id);
    }
   @QueryMapping(name = "getAllMoodIntensities")
    public List<MtMoodIntensity> getAllMoodIntensities() {
        return moodIntensityService.getAllMoodIntensities();
    }

    @MutationMapping(name = "updateMoodIntensity")
    public String UpdateUserMoodCheckIn(@Argument(name = "input") MoodSourceIntensityRequestDto request) {
        return moodIntensityService.UpdateUserMoodCheckIn(request);
    }


    @MutationMapping(name = "createUserMoodCheckIn")
    public String createUserMoodCheckIn(@Argument(name = "input") MoodSourceIntensityRequestDto request){
        return moodIntensityService.createUserMoodCheckIn(request);
    }
    @QueryMapping(name = "getAllMoodCheckIn")
    public List<UserMoodCheckIn> getAllMoodCheckIn(){
        return moodIntensityService.getAllMoodCheckIn();
    }

    @MutationMapping(name = "deleteUserMoodCheckInById")
    public String deleteUserMoodCheckInById(@Argument Long id){
        return moodIntensityService.deleteUserMoodCheckInById(id);
    }

    @QueryMapping(name = "getAllMoodIntensitiesByMoodInfoId")
    public List<MtMoodIntensity> getAllMoodIntensitiesByMoodInfoId(@Argument Long id) {
        return moodIntensityService.getAllMoodIntensitiesByMoodInfoId(id);
    }

}
