package net.thrymr.controller;

import net.thrymr.dto.CounsellorDto;
import net.thrymr.dto.EducationDto;
import net.thrymr.dto.LanguageDto;
import net.thrymr.dto.response.PaginationResponse;
import net.thrymr.model.Counsellor;
import net.thrymr.model.master.EducationDetails;
import net.thrymr.model.master.LanguageDetails;
import net.thrymr.services.CounsellorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CounsellorController {
    @Autowired
    CounsellorService counsellorService;

    @MutationMapping(name = "createCounsellor")
    public String createCounsellor(@Argument(name = "input") CounsellorDto counsellorDto) {
        return counsellorService.createCounsellor(counsellorDto);
    }

    @MutationMapping(name = "updateCounsellorById")
    public String updateCounsellorById(@Argument(name = "input") CounsellorDto counsellorDto) {
        return counsellorService.updateCounsellorById(counsellorDto);
    }

    @MutationMapping(name = "updateCounsellors")
    public String updateCounsellors(@Argument(name = "input") List<CounsellorDto> counsellorDtoList) {
        return counsellorService.updateCounsellors(counsellorDtoList);
    }

    @MutationMapping(name = "deleteCounsellorById")
    public String deleteCounsellorById(@Argument Long id) {
        return counsellorService.deleteCounsellorById(id);
    }

    @QueryMapping(name = "getAllCounsellorPagination")
    public PaginationResponse getAllCounsellorPagination(@Argument(name = "input") CounsellorDto response) {
        return counsellorService.getAllCounsellorPagination(response);
    }

    @QueryMapping(name = "getCounsellorById")
    public Counsellor getCounsellorById(@Argument Long id) {
        return counsellorService.getCounsellorById(id);
    }
    @MutationMapping(name = "addNewLanguage")
    public String addNewLanguage(@Argument(name = "input") LanguageDto request){
        return counsellorService.addNewLanguage(request);
    }
    @MutationMapping(name = "addNewEducation")
    public String addNewEducation(@Argument(name = "input") EducationDto request){
        return counsellorService.addNewEducation(request);
    }
    @MutationMapping(name = "updateEducationDetailsById")
    public String updateEducationDetailsById(@Argument(name = "input") EducationDto request){
        return counsellorService.updateEducationDetailsById(request);
    }
    @MutationMapping(name = "updateLanguageDetailsById")
    public String updateLanguageDetailsById(@Argument(name = "input") LanguageDto request){
        return counsellorService.updateLanguageDetailsById(request);
    }
    @QueryMapping(name = "getEducationalDetailsById")
    public EducationDetails getEducationalDetailsById(@Argument Long id) {
        return counsellorService.getEducationalDetailsById(id);
    }
    @QueryMapping(name = "getLanguageDetailsById")
    public LanguageDetails getLanguageDetailsById(@Argument Long id) {
        return counsellorService.getLanguageDetailsById(id);
    }
    @QueryMapping(name = "getAllEducationalDetails")
    public List<EducationDetails> getAllEducationalDetails() {
        return counsellorService.getAllEducationalDetails();
    }
    @QueryMapping(name = "getAllLanguagesDetails")
    public List<LanguageDetails> getAllLanguagesDetails() {
        return counsellorService.getAllLanguagesDetails();
    }

    @MutationMapping(name = "deleteAllEducation")
    public String deleteAllEducation(){
        return counsellorService.deleteAllEducation();
    }

    @QueryMapping(name = "getTotalCounsellorsCount")
    public Long getTotalCounsellorsCount(@Argument Long vendorId) {
        return counsellorService.getTotalCounsellorsCount(vendorId);
    }
}
