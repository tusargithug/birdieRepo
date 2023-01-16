package net.thrymr.controller;

import net.thrymr.dto.MoodSourceDto;
import net.thrymr.dto.request.MoodSourceIntensityRequestDto;
import net.thrymr.model.master.MtMoodSource;
import net.thrymr.services.MoodSourceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@RestController
@RequestMapping("/api/v1/mood-source")
public class MoodSourceController {
    private final Logger logger = LoggerFactory.getLogger(MoodSourceController.class);

    private final MoodSourceService moodSourceService;


    public MoodSourceController(MoodSourceService moodSourceService) {
        this.moodSourceService = moodSourceService;

    }
    @QueryMapping(name = "getAllMoodSources")
    public List<MtMoodSource> getAllMoodSources() {
        return moodSourceService.getAllMoodSources();
    }

    @QueryMapping("getMoodSourceById")
    public MtMoodSource getMoodSourceById(@Argument Long id) {
        return moodSourceService.getMoodSourceById(id);
    }


    @MutationMapping(name = "updateMoodSourceById")
    public String updateMoodSourceById(@Argument(name = "input") MoodSourceDto request) {
        return moodSourceService.updateMoodSourceById(request);
    }

    @MutationMapping(name = "deleteMoodSourceById")
    public String deleteMoodSourceById(@Argument Long id) {
        return moodSourceService.deleteMoodSourceById(id);
    }

    @MutationMapping(name = "deleteUserMoodSourceCheckInById")
    public String deleteUserMoodSourceCheckInById(@Argument Long id) {
        return moodSourceService.deleteUserMoodSourceCheckInById(id);
    }

    @QueryMapping(name = "getAllMoodSourceById")
    public List<MtMoodSource> getAllMoodSourceById(@Argument(name = "input") MoodSourceIntensityRequestDto request) {
        return moodSourceService.getAllMoodSourceById(request);
    }
}
