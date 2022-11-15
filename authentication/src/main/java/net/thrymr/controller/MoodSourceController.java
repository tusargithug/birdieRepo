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
    @GetMapping("/get/all")
    public ApiResponse getAllMoodSources() {
        logger.info("get all mood sources service started");
        ApiResponse apiResponse = moodSourceService.getAllMoodSources();
        logger.info("get all mood sources service completed");
        return new ApiResponse(HttpStatus.OK, "All MoodSources details", apiResponse);
    }


    //update mood source on daily basis in userMoodSourceCheckedIn  table

    @PutMapping("/update")
    public ApiResponse updateMoodSource(@RequestBody MoodSourceIntensityRequestDto request) {
        logger.info("Get update mood service started");
        ApiResponse apiResponse = moodSourceService.updateMoodSource(request);
        logger.info("Get update mood service completed");
        return new ApiResponse(HttpStatus.OK, "", apiResponse);
    }

    @MutationMapping(name = "createUserMoodSourceCheckIn")
    public String createUserMoodSourceCheckIn(@Argument(name = "input") MoodSourceIntensityRequestDto request) {
        return moodSourceService.createUserMoodSourceCheckIn(request);

    }

    @MutationMapping(name = "deleteUserMoodSourceCheckInById")
    public String deleteUserMoodSourceCheckInById(@Argument Long id) {
        return moodSourceService.deleteUserMoodSourceCheckInById(id);
    }
}
