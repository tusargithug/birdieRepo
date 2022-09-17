package net.thrymr.controller;

import net.thrymr.dto.MoodIntensityDto;

import net.thrymr.services.MoodIntensityService;
import net.thrymr.utils.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/*
 *@author Chanda Veeresh
 *@version 1.0
 *@since  19-07-2022
 */
@RestController
@RequestMapping("/api")
public class MoodInfoController {
    private final Logger logger = LoggerFactory.getLogger(MasterController.class);


    private final MoodIntensityService moodIntensityService;

    public MoodInfoController(MoodIntensityService moodIntensityService) {
        this.moodIntensityService = moodIntensityService;
    }

    @GetMapping("/mood-info/intensities/{id}")
    public ApiResponse getMoodIntensityByMoodInfoId(@PathVariable Long id) {
        logger.info("get all mood intensity service started");
      ApiResponse apiResponse=   moodIntensityService.getMoodIntensityByMoodInfoId(id);
        logger.info("get all mood intensity completed");
        return new ApiResponse(HttpStatus.OK, apiResponse);
    }

    @GetMapping("/mood-info/intensities/save")
    public ApiResponse moodIntensitySave(@RequestBody MoodIntensityDto request) {
        logger.info("save mood intensity service started");
        ApiResponse apiResponse=   moodIntensityService.moodIntensitySave(request);
        logger.info("save mood intensity service completed");
        return new ApiResponse(HttpStatus.OK, apiResponse);
    }


}
