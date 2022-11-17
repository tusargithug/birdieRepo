package net.thrymr.controller;


import net.thrymr.dto.MoodInfoDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
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
public class MoodInfoController {
    @Autowired
    MoodInfoService moodInfoService;
    @QueryMapping(name = "getMoodInfoById")
    public MtMoodInfo getMoodInfoById(@Argument Long id) {
        return moodInfoService.getMoodInfoById(id);
    }

    @MutationMapping(name ="updateMoodInfoById")
    public String updateMoodInfoById(@Argument(name = "input") MoodInfoDto request){
        return moodInfoService.updateMoodInfoById(request);
    }
    @MutationMapping(name = "deleteMoodInfoById")
    public String deleteMoodInfoById(@Argument Long id){
        return moodInfoService.deleteMoodInfoById(id);
    }
    @QueryMapping(name = "getAllMoodInfo")
    public List<MtMoodInfo> getAllMoodInfo() {
        return moodInfoService.getAllMoodInfo();
    }
}
