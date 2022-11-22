package net.thrymr.controller;

import net.thrymr.dto.AssessmentDto;
import net.thrymr.model.master.MtAssessment;
import net.thrymr.services.AssessmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.List;

@RestController
public class AssessmentController {
    @Autowired
    AssessmentService assessmentService;

    @QueryMapping("getAllAssessment")
    public List<MtAssessment> getAllAssessment() {
        return  assessmentService.getAllAssessment();
    }
    @QueryMapping("getAssessmentById")
    public MtAssessment getAssessmentById(@Argument Long id) {
        return  assessmentService.getAssessmentById(id);
    }

    @MutationMapping("createAssessment")
    public String createAssessment(@Argument(name = "input") AssessmentDto request) throws ParseException {
        return assessmentService.createAssessment(request);
    }

    @MutationMapping("updateAssessmentById")
    public String updateAssessmentById(@Argument(name = "input") AssessmentDto request) throws ParseException {
        return assessmentService.updateAssessmentById(request);
    }

    @MutationMapping("deleteAssessmentId")
    public String deleteAssessmentId(@Argument Long id){
        return assessmentService.deleteAssessmentId(id);
    }

}
