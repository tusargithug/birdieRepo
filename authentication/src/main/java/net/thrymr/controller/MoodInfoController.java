package net.thrymr.controller;

import net.thrymr.dto.MoodIntensityDto;

import net.thrymr.services.MoodIntensityService;
import net.thrymr.utils.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        logger.info("get all roles service started");
      ApiResponse apiResponse=   moodIntensityService.getMoodIntensityByMoodInfoId(id);
        logger.info("get all roles service completed");
        return new ApiResponse(HttpStatus.OK, "Get all mood intensity",apiResponse);
    }
}
