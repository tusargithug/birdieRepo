package net.thrymr.controller;

import net.thrymr.dto.MoodIntensityDto;
import net.thrymr.dto.request.MoodSourceIntensityRequestDto;
import net.thrymr.services.MoodIntensityService;
import net.thrymr.utils.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/mood-intensity")
public class MoodIntensityController {
    private final Logger logger = LoggerFactory.getLogger(MoodIntensityController.class);

    private final MoodIntensityService moodIntensityService;

    public MoodIntensityController(MoodIntensityService moodIntensityService) {
        this.moodIntensityService = moodIntensityService;
    }
   // save mood intensity
    @PostMapping("/save")
    public ApiResponse moodIntensitySave(@RequestBody MoodIntensityDto request) {
        logger.info("save mood intensity service started");
        ApiResponse apiResponse=   moodIntensityService.moodIntensitySave(request);
        logger.info("save mood intensity service completed");
        return new ApiResponse(HttpStatus.OK, "",apiResponse);
    }
     // get mood intensity by id
    @GetMapping("/get/{id}")
    public ApiResponse getMoodIntensitiesById(@PathVariable Long id) {
        logger.info("Get mood intensity service started");
        ApiResponse apiResponse=   moodIntensityService.getMoodIntensitiesById(id);
        logger.info("Get mood intensity service completed");
        return new ApiResponse(HttpStatus.OK,"", apiResponse);
    }
    // deleted by id
    @DeleteMapping("/delete/{id}")
    public ApiResponse deleteMoodIntensitiesById(@PathVariable Long id) {
        logger.info("delete mood intensity service started");
        ApiResponse apiResponse=   moodIntensityService.deleteMoodIntensitiesById(id);
        logger.info("delete mood intensity service completed");
        return new ApiResponse(HttpStatus.OK, "",apiResponse);
    }
    // get all mood-intensity
    @GetMapping("/get/all")
    public ApiResponse getAllMoodIntensities() {
        logger.info("Get all mood intensity service started");
        ApiResponse apiResponse=   moodIntensityService.getAllMoodIntensities();
        logger.info("Get all mood intensity service completed");
        return new ApiResponse(HttpStatus.OK,"", apiResponse);
    }

//get all mood intensity based on mood info id

    @GetMapping("/get/all/{id}")
    public ApiResponse getAllMoodIntensitiesByMoodInfoId(@PathVariable Long id) {
        logger.info("Get all mood intensity service started");
        ApiResponse apiResponse=   moodIntensityService.getAllMoodIntensitiesByMoodInfoId(id);
        logger.info("Get all mood intensity service completed");
        return new ApiResponse(HttpStatus.OK,"", apiResponse);
    }
     //update mood intensity    on daily basis in userCheckedIn  table

    @PutMapping("/update")
    public ApiResponse updateMoodIntensity(@RequestBody MoodSourceIntensityRequestDto request) {
        logger.info("Get update mood service started");
        ApiResponse apiResponse=   moodIntensityService.updateMoodIntensity(request);
        logger.info("Get update mood service completed");
        return new ApiResponse(HttpStatus.OK,"", apiResponse);
    }
}
