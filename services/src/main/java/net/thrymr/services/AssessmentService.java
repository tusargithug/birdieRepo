package net.thrymr.services;

import net.thrymr.dto.AssessmentDto;
import net.thrymr.model.master.MtAssessment;

import java.util.List;

public interface AssessmentService {
    List<MtAssessment> getAllAssessment();

    String createAssessment(AssessmentDto request);

    String updateAssessmentById(AssessmentDto request);

    String deleteAssessmentId(Long id);

    MtAssessment getAssessmentById(Long id);
}
