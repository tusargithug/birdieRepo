package net.thrymr.controller;

import net.thrymr.dto.MoodSourceDto;
import net.thrymr.dto.request.MoodSourceIntensityRequestDto;
import net.thrymr.model.AppUser;
import net.thrymr.model.UserMoodSourceCheckedIn;
import net.thrymr.model.master.MtMoodSource;
import net.thrymr.repository.MoodSourceRepo;
import net.thrymr.repository.UserMoodSourceCheckInRepo;
import net.thrymr.services.MoodSourceService;
import net.thrymr.utils.ApiResponse;
import net.thrymr.utils.CommonUtil;
import net.thrymr.utils.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/mood-source")
public class MoodSourceController {
    private final Logger logger = LoggerFactory.getLogger(MoodSourceController.class);

    private final MoodSourceService moodSourceService;


    public MoodSourceController(MoodSourceService moodSourceService) {
        this.moodSourceService = moodSourceService;

    }

    // save mood sources
    @PostMapping("/save")
    public ApiResponse moodSourceSave(@RequestBody MoodSourceDto request) {
        logger.info("save mood source service started");
        ApiResponse apiResponse = moodSourceService.moodSourceSave(request);
        logger.info("save mood source service completed");
        return new ApiResponse(HttpStatus.OK, "", apiResponse);
    }

    // get all mood sources
    @QueryMapping(name = "getAllMoodSources")
    public List<MtMoodSource> getAllMoodSources() {
        return moodSourceService.getAllMoodSources();
    }


    @QueryMapping("getMoodSourceById")
    public MtMoodSource getMoodSourceById(@Argument Long id) {
        return moodSourceService.getMoodSourceById(id);
    }


    @MutationMapping(name = "updateMoodSourceById")
    public String updateMoodSourceById(@Argument(name = "input") MoodSourceDto request) {
        return moodSourceService.updateMoodSourceById(request);
    }

    @MutationMapping(name = "deleteMoodSourceById")
    public String deleteMoodSourceById(@Argument Long id) {
        return moodSourceService.deleteMoodSourceById(id);
    }


    //update mood source on daily basis in userMoodSourceCheckedIn  table

  /*  @PutMapping("/update")
    public ApiResponse updateMoodSource(@RequestBody MoodSourceIntensityRequestDto request) {
        logger.info("Get update mood service started");
        ApiResponse apiResponse = moodSourceService.updateMoodSource(request);
        logger.info("Get update mood service completed");
        return new ApiResponse(HttpStatus.OK, "", apiResponse);
    }*/

    @MutationMapping(name = "createUserMoodSourceCheckIn")
    public String createUserMoodSourceCheckIn(@Argument(name = "input") MoodSourceIntensityRequestDto request) {
        return moodSourceService.createUserMoodSourceCheckIn(request);

    }

    @MutationMapping(name = "deleteUserMoodSourceCheckInById")
    public String deleteUserMoodSourceCheckInById(@Argument Long id) {
        return moodSourceService.deleteUserMoodSourceCheckInById(id);
    }
}
