package net.thrymr.services;

import net.thrymr.dto.AssessmentDto;
import net.thrymr.model.master.MtAssessment;

import java.text.ParseException;
import java.util.List;

public interface AssessmentService {
    List<MtAssessment> getAllAssessment();
    String createAssessment(AssessmentDto request) throws ParseException;

    String updateAssessmentById(AssessmentDto request) throws ParseException;

    String deleteAssessmentId(Long id);

    MtAssessment getAssessmentById(Long id);
}
