package net.thrymr.controller;

import net.thrymr.dto.PsychoEducationDto;
import net.thrymr.model.master.MtPsychoEducation;
import net.thrymr.services.PsychoEducationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PsychoEducationController {

    @Autowired
    private PsychoEducationService psychoEducationService;


    @QueryMapping("getAllPsychoEducation")
    private List<MtPsychoEducation> getAllPsychoEducation() {
        return psychoEducationService.getAllPsychoEducation();
    }

    @QueryMapping("getPsychoEducationById")
    private MtPsychoEducation getPsychoEducationById(@Argument Long id) {
        return psychoEducationService.getPsychoEducationById(id);
    }

    @MutationMapping("createPsychoEducation")
    private String createPsychoEducation(@Argument(name = "input") PsychoEducationDto request) {
        return psychoEducationService.createPsychoEducation(request);
    }
    @MutationMapping("updatePsychoEducationById")
    private String updatePsychoEducationById(@Argument(name = "input") PsychoEducationDto request) {
        return psychoEducationService.updatePsychoEducationById(request);
    }

    @MutationMapping("deletePsychoEducationById")
    private String deletePsychoEducationById(@Argument Long id) {
        return psychoEducationService.deletePsychoEducationById(id);
    }

}