package net.thrymr.services.impl;

import net.thrymr.dto.AssessmentDto;
import net.thrymr.model.master.MtAssessment;
import net.thrymr.repository.AssessmentRepo;
import net.thrymr.services.AssessmentService;
import net.thrymr.utils.Validator;
import org.apache.poi.sl.draw.geom.GuideIf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AssessmentServiceImpl implements AssessmentService {


    @Autowired
    AssessmentRepo assessmentRepo;

    @Override
    public List<MtAssessment> getAllAssessment() {
        List<MtAssessment> mtAssessmentList = assessmentRepo.findAll();
        if(!mtAssessmentList.isEmpty()){
            return mtAssessmentList.stream().filter(obj->obj.getIsActive().equals(Boolean.TRUE)).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    @Override
    public String createAssessment(AssessmentDto request) {
        MtAssessment mtAssessment = dtoToAssessmentEntity(request);
        assessmentRepo.save(dtoToAssessmentEntity(request));
        return "create Assessment successfully";
    }
    private MtAssessment dtoToAssessmentEntity(AssessmentDto request) {
        MtAssessment assessment=new MtAssessment();
        assessment.setName(request.getName());
        assessment.setDescription(request.getDescription());
        assessment.setInstructions(request.getInstructions());
        assessment.setFrequencyType(request.getFrequencyType());
        assessment.setHigh(request.getHigh());
        assessment.setModerate(request.getModerate());
        assessment.setLow(request.getLow());
        return assessment;
    }

    @Override
    public String updateAssessmentById(AssessmentDto request) {
        MtAssessment assessment=null;
        if(Validator.isValid(request.getId())){
        Optional<MtAssessment> optionalAssessment=assessmentRepo.findById(request.getId());
            if(optionalAssessment.isPresent()){
                assessment=optionalAssessment.get();
                if(Validator.isValid(request.getName())) {
                    assessment.setName(request.getName());
                }
                if (Validator.isValid(request.getDescription())) {
                    assessment.setDescription(request.getDescription());
                }
                if(Validator.isValid(request.getInstructions())) {
                    assessment.setInstructions(request.getInstructions());
                }
                if (Validator.isValid(String.valueOf(request.getFrequencyType()))) {
                    assessment.setFrequencyType(request.getFrequencyType());
                }
                if(Validator.isValid(request.getHigh())) {
                    assessment.setHigh(request.getHigh());
                }
                if(Validator.isValid(request.getModerate())) {
                    assessment.setModerate(request.getModerate());
                }
                if(Validator.isValid(request.getLow())) {
                    assessment.setLow(request.getLow());
                }
                assessmentRepo.save(assessment);
                return "Assessment update successfully";
            }
        }
        return "Assessment id not found";
    }

    @Override
    public String deleteAssessmentId(Long id) {
        MtAssessment assessment=null;
        if(Validator.isValid(id)) {
            Optional<MtAssessment> optionalAssessment = assessmentRepo.findById(id);
            if(optionalAssessment.isPresent()){
                assessment=optionalAssessment.get();
                assessment.setIsActive(Boolean.FALSE);
                assessment.setIsDeleted(Boolean.TRUE);
                assessmentRepo.save(assessment);
                return "Assessment deleted successfully";
            }
        }
        return "Assessment id not found";
    }

    @Override
    public MtAssessment getAssessmentById(Long id) {
        MtAssessment assessment=null;
        if(Validator.isValid(id)) {
            Optional<MtAssessment> optionalAssessment = assessmentRepo.findById(id);
            if (optionalAssessment.isPresent() && optionalAssessment.get().getIsActive().equals(Boolean.TRUE)) {
                return optionalAssessment.get();
            }
        }
        return new MtAssessment();
    }

}
