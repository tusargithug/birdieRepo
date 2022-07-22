package net.thrymr.controller;

import net.thrymr.dto.MoodIntensityDto;
import net.thrymr.service.MoodIntensityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/*
 *@author Chanda Veeresh
 *@version 1.0
 *@since  19-07-2022
 */
@RestController
@RequestMapping("/api")
public class MoodInfoController {
    private final Logger logger;

    {
        logger = LoggerFactory.getLogger(MasterController.class);
    }
    @Autowired
    MoodIntensityService moodIntensityService;
    @GetMapping("/mood-info/intensities/{id}")
    public List<MoodIntensityDto> getMoodIntensityByMoodInfoId(@PathVariable Long id) {
        logger.info("get all roles service started");
      moodIntensityService.getMoodIntensityByMoodInfoId(id);
        logger.info("get all roles service completed");
        return  moodIntensityService.getMoodIntensityByMoodInfoId(id);
    }
}
