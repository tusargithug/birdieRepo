package net.thrymr.controller;

import net.thrymr.services.MoodInfoService;
import net.thrymr.utils.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/mood-info")
public class MoodInfoController {
    private final Logger logger = LoggerFactory.getLogger(MoodInfoController.class);


    private final MoodInfoService moodInfoService;

    public MoodInfoController(MoodInfoService moodInfoService) {
        this.moodInfoService = moodInfoService;
    }

    // get mood info by id
    @GetMapping("/get/{id}")
    public ApiResponse getMoodInfoById(@PathVariable Long id) {
        logger.info("get  mood info service started");
      ApiResponse apiResponse=   moodInfoService.getMoodInfoById(id);
        logger.info("get  mood info completed");
        return new ApiResponse(HttpStatus.OK,"", apiResponse);
    }
    // get all mood info
    @GetMapping("/get/all")
    public ApiResponse getAllMoodNames() {
        logger.info("get all mood info service started");
        ApiResponse apiResponse = moodInfoService.getAllMoods();
        logger.info("get all mood info service completed");
        return new ApiResponse(HttpStatus.OK, "All MoodInfo details",apiResponse);
    }

    @DeleteMapping("/get/all")
    public ApiResponse deleteMoodInfoById(@PathVariable Long id) {
        logger.info("delete mood info service started");
        ApiResponse apiResponse = moodInfoService.deleteMoodInfoById(id);
        logger.info("delete mood info service completed");
        return new ApiResponse(HttpStatus.OK, "Delete  mood info");
    }
}
