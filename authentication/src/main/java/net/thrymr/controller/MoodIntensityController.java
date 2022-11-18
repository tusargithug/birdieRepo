package net.thrymr.controller;
import net.thrymr.dto.request.MoodSourceIntensityRequestDto;
import net.thrymr.model.UserMoodCheckIn;
import net.thrymr.model.master.MtMoodIntensity;
import net.thrymr.services.MoodIntensityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
public class MoodIntensityController {
    private final Logger logger = LoggerFactory.getLogger(MoodIntensityController.class);
    @Autowired
    MoodIntensityService moodIntensityService;


    @QueryMapping(name = "getMoodIntensitiesById")
    public MtMoodIntensity getMoodIntensitiesById(@Argument Long id) {
        return moodIntensityService.getMoodIntensitiesById(id);
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
    public String updateMoodIntensity(@Argument(name = "input") MoodSourceIntensityRequestDto request) {
        return moodIntensityService.updateMoodIntensity(request);
    }


    @MutationMapping(name = "createUserMoodCheckIn")
    public String createUserMoodCheckIn(@Argument(name = "input") MoodSourceIntensityRequestDto request) {
        return moodIntensityService.createUserMoodCheckIn(request);
    }

    @QueryMapping(name = "getAllMoodCheckIn")
    public List<UserMoodCheckIn> getAllMoodCheckIn() {
        return moodIntensityService.getAllMoodCheckIn();
    }

    @MutationMapping(name = "deleteUserMoodCheckInById")
    public String deleteUserMoodCheckInById(@Argument Long id) {
        return moodIntensityService.deleteUserMoodCheckInById(id);
    }

}
