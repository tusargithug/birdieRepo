package net.thrymr.controller;

import net.thrymr.dto.MeditationDto;
import net.thrymr.dto.response.PaginationResponse;
import net.thrymr.model.master.MtMeditation;
import net.thrymr.services.MeditationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MeditationController {

    @Autowired
    MeditationService meditationService;

    @QueryMapping("getAllMeditation")
    List<MtMeditation> getAllMeditation() {
        return meditationService.getAllMeditation();
    }

    @QueryMapping("getMeditationById")
    MtMeditation getMeditationById(@Argument Long id) {
        return meditationService.getMeditationById(id);
    }

    @MutationMapping("createMeditation")
    String createMeditation(@Argument(name = "input") MeditationDto request) {
        return meditationService.createMeditation(request);
    }

    @MutationMapping("updateMeditationById")
    String updateMeditationById(@Argument(name = "input") MeditationDto request) {
        return meditationService.updateMeditationById(request);
    }

    @MutationMapping("deleteMeditationById")
    String deleteMeditationById(@Argument Long id) {
        return meditationService.deleteMeditationById(id);
    }

    @QueryMapping("getAllMeditationPagination")
    public PaginationResponse getAllMeditationPagination(@Argument (name = "input") MeditationDto response) {
        return meditationService.getAllMeditationPagination(response);
    }
}
