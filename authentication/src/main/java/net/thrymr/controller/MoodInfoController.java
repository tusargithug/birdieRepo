package net.thrymr.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.thrymr.model.master.MtMoodInfo;

import net.thrymr.services.MoodInfoService;
import net.thrymr.utils.ApiResponse;
import org.springframework.graphql.data.method.annotation.QueryMapping;

import java.util.List;


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

    @QueryMapping
    public MtMoodInfo moodInfoById(@Argument long id) {
        return moodInfoService.moodInfoById(id);

    }

    @QueryMapping
    public List<MtMoodInfo> getAllMoodInfo() {
        logger.info("get all mood info service started");
        return moodInfoService.getAllMoodInfo();
    }
}
