package net.thrymr.controller;

import net.thrymr.dto.MoodIntensityDto;
import net.thrymr.dto.MoodSourceDto;
import net.thrymr.services.MoodSourceService;
import net.thrymr.utils.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
}
