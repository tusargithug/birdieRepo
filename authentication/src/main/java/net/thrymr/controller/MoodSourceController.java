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

    private final MoodSourceRepo moodSourceRepo;

    private final Environment environment;

    private final UserMoodSourceCheckInRepo userMoodSourceCheckInRepo;

    public MoodSourceController(MoodSourceService moodSourceService, MoodSourceRepo moodSourceRepo, Environment environment, UserMoodSourceCheckInRepo userMoodSourceCheckInRepo) {
        this.moodSourceService = moodSourceService;
        this.moodSourceRepo = moodSourceRepo;
        this.environment = environment;
        this.userMoodSourceCheckInRepo = userMoodSourceCheckInRepo;
    }

    // save mood sources
    @PostMapping("/save")
    public ApiResponse moodSourceSave(@RequestBody MoodSourceDto request) {
        logger.info("save mood source service started");
        ApiResponse apiResponse=   moodSourceService.moodSourceSave(request);
        logger.info("save mood source service completed");
        return new ApiResponse(HttpStatus.OK, "",apiResponse);
    }

    // get all mood sources
    @GetMapping("/get/all")
    public ApiResponse getAllMoodSources() {
        logger.info("get all mood sources service started");
        ApiResponse apiResponse = moodSourceService.getAllMoodSources();
        logger.info("get all mood sources service completed");
        return new ApiResponse(HttpStatus.OK, "All MoodSources details",apiResponse);
    }


    //update mood source on daily basis in userMoodSourceCheckedIn  table

    @PutMapping("/update")
    public ApiResponse updateMoodSource(@RequestBody MoodSourceIntensityRequestDto request) {
        logger.info("Get update mood service started");
        ApiResponse apiResponse=   moodSourceService.updateMoodSource(request);
        logger.info("Get update mood service completed");
        return new ApiResponse(HttpStatus.OK,"", apiResponse);
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
        Optional<MtMoodSource> optionalMtMoodSource=moodSourceRepo.findById(id);
        optionalMtMoodSource.ifPresent(moodSourceRepo::delete);
        return "Source deleted successfully";
    }
}
