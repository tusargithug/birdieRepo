package net.thrymr.services.impl;

import net.thrymr.constant.Constants;
import net.thrymr.dto.AssessmentDto;
import net.thrymr.dto.OptionsDto;
import net.thrymr.dto.QuestionDto;
import net.thrymr.enums.FrequencyType;
import net.thrymr.model.master.MtAssessment;
import net.thrymr.model.master.MtOptions;
import net.thrymr.model.master.MtQuestion;
import net.thrymr.repository.AssessmentRepo;
import net.thrymr.repository.OptionsRepo;
import net.thrymr.repository.QuestionRepo;
import net.thrymr.services.AssessmentService;
import net.thrymr.utils.DateUtils;
import net.thrymr.utils.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AssessmentServiceImpl implements AssessmentService {


    @Autowired
    AssessmentRepo assessmentRepo;

    @Autowired
    QuestionRepo questionRepo;

    @Autowired
    OptionsRepo optionsRepo;

    @Override
    public List<MtAssessment> getAllAssessment() {
        List<MtAssessment> mtAssessmentList = assessmentRepo.findAll();
        if (!mtAssessmentList.isEmpty()) {
            return mtAssessmentList.stream().filter(obj -> obj.getIsActive().equals(Boolean.TRUE)).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    @Override
    public String createAssessment(AssessmentDto request) throws ParseException {
        MtAssessment mtAssessment = new MtAssessment();
        if (Validator.isValid(request.getName())) {
            mtAssessment.setName(request.getName());
        }
        mtAssessment.setDescription(request.getDescription());
        if (Validator.isValid(request.getInstructions())) {
            mtAssessment.setInstructions(request.getInstructions());
        }
        mtAssessment.setDateOfPublishing(DateUtils.toFormatStringToDate(request.getDateOfPublishing(), Constants.DATE_FORMAT));
        mtAssessment.setFrequencyType(FrequencyType.valueOf(request.getFrequencyType()));
        if (Validator.isValid(request.getHigh())) {
            mtAssessment.setHigh(request.getHigh());
        } else if (Validator.isValid(request.getModerate())) {
            mtAssessment.setModerate(request.getModerate());
        } else {
            mtAssessment.setLow(request.getLow());
        }
        Set<MtQuestion> mtQuestionSet = new HashSet<>();
        Set<MtOptions> mtOptionsSet = new HashSet<>();
        if (Validator.isValid(request.getQuestionDtoList())) {
            for (QuestionDto questionDto : request.getQuestionDtoList()) {
                MtQuestion mtQuestion = new MtQuestion();
                mtQuestion.setQuestion(questionDto.getQuestion());
                mtQuestion.setSequence(questionDto.getSequence());
                mtQuestionSet.add(mtQuestion);
                for (OptionsDto optionsDto : request.getOptionsDtoList()) {
                    MtOptions mtOptions = new MtOptions();
                    mtOptions.setTextAnswer(optionsDto.getTextAnswer());
                    mtOptionsSet.add(mtOptions);
                }
                mtQuestion.setMtOptions(mtOptionsSet);
                mtQuestionSet.add(mtQuestion);
            }
            mtAssessment.setQuestionList(mtQuestionSet);
            mtAssessment = assessmentRepo.save(mtAssessment);
            MtAssessment finalMtAssessment = mtAssessment;
            mtQuestionSet.stream().map(o -> {
                o.setAssessment(finalMtAssessment);
                return null;
            }).collect(Collectors.toSet());
//            mtQuestionSet = new HashSet<>(questionRepo.saveAll(mtQuestionSet));
//            Set<MtQuestion> finalMtQuestionSet = mtQuestionSet;
//            mtOptionsSet.stream().map(obj -> obj.setQuestion(finalMtQuestionSet)).collect(Collectors.toSet());
            optionsRepo.saveAll(mtOptionsSet);
        }
        return "create Assessment successfully";
    }

    private MtAssessment dtoToAssessmentEntity(AssessmentDto request) throws ParseException {
        MtAssessment assessment = new MtAssessment();
        assessment.setName(request.getName());
        assessment.setDescription(request.getDescription());
        assessment.setInstructions(request.getInstructions());
        assessment.setFrequencyType(FrequencyType.valueOf(request.getFrequencyType()));
        if(Validator.isValid(request.getHigh())) {
            assessment.setHigh(request.getHigh());
        }else if(Validator.isValid(request.getModerate())) {
            assessment.setModerate(request.getModerate());
        }else {
            assessment.setLow(request.getLow());
        }
        assessment.setDateOfPublishing(DateUtils.toFormatStringToDate(request.getDateOfPublishing(), Constants.DATE_FORMAT));
        if (request.getIsActive() != null && request.getIsActive().equals(Boolean.TRUE)) {
            assessment.setIsActive(request.getIsActive());
        }
        assessment.setSearchKey(getAppUserSearchKey(assessment));
        return assessment;
    }

    @Override
    public String updateAssessmentById(AssessmentDto request) throws ParseException {
        MtAssessment assessment = null;
        if (Validator.isValid(request.getId())) {
            Optional<MtAssessment> optionalAssessment = assessmentRepo.findById(request.getId());
            if (optionalAssessment.isPresent()) {
                assessment = optionalAssessment.get();
                if (Validator.isValid(request.getName())) {
                    assessment.setName(request.getName());
                }
                if (Validator.isValid(request.getDescription())) {
                    assessment.setDescription(request.getDescription());
                }
                if (Validator.isValid(request.getInstructions())) {
                    assessment.setInstructions(request.getInstructions());
                }
                if (Validator.isValid(String.valueOf(request.getFrequencyType()))) {
                    assessment.setFrequencyType(FrequencyType.valueOf(request.getFrequencyType()));
                }
                if (Validator.isValid(request.getHigh())) {
                    assessment.setHigh(request.getHigh());
                }
                if (Validator.isValid(request.getModerate())) {
                    assessment.setModerate(request.getModerate());
                }
                if (Validator.isValid(request.getLow())) {
                    assessment.setLow(request.getLow());
                }
                if(Validator.isValid(request.getDateOfPublishing())){
                    assessment.setDateOfPublishing(DateUtils.toFormatStringToDate(request.getDateOfPublishing(), Constants.DATE_FORMAT));
                }
                if (request.getIsActive()!=null && request.getIsActive().equals(Boolean.TRUE) || request.getIsActive().equals(Boolean.FALSE)) {
                    assessment.setIsActive(request.getIsActive());
                }
                assessment.setSearchKey(getAppUserSearchKey(assessment));
                assessmentRepo.save(assessment);
                return "Assessment update successfully";
            }
        }
        return "Assessment id not found";
    }

    @Override
    public String deleteAssessmentId(Long id) {
        MtAssessment assessment = null;
        if (Validator.isValid(id)) {
            Optional<MtAssessment> optionalAssessment = assessmentRepo.findById(id);
            if (optionalAssessment.isPresent()) {
                assessment = optionalAssessment.get();
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
        MtAssessment assessment = null;
        if (Validator.isValid(id)) {
            Optional<MtAssessment> optionalAssessment = assessmentRepo.findById(id);
            if (optionalAssessment.isPresent() && optionalAssessment.get().getIsActive().equals(Boolean.TRUE)) {
                return optionalAssessment.get();
            }
        }
        return new MtAssessment();
    }

    public String getAppUserSearchKey(MtAssessment mtAssessment) {
        String searchKey = "";
        if (mtAssessment.getName() != null) {
            searchKey = searchKey + " " + mtAssessment.getName();
        }
        if (mtAssessment.getDescription() != null) {
            searchKey = searchKey + " " + mtAssessment.getDescription();
        }
        if (mtAssessment.getCourses() != null) {
            searchKey = searchKey + " " + mtAssessment.getCourses();
        }
        if (mtAssessment.getInstructions() != null) {
            searchKey = searchKey + " " + mtAssessment.getInstructions();
        }
        if (mtAssessment.getFrequencyType() != null) {
            searchKey = searchKey + " " + mtAssessment.getFrequencyType();
        }
        if (mtAssessment.getHigh() != null) {
            searchKey = searchKey + " " + mtAssessment.getHigh();
        }
        if (mtAssessment.getModerate() != null) {
            searchKey = searchKey + " " + mtAssessment.getModerate();
        }
        if (mtAssessment.getLow() != null) {
            searchKey = searchKey + " " + mtAssessment.getLow();
        }
        if (mtAssessment.getDateOfPublishing() != null) {
            searchKey = searchKey + " " + mtAssessment.getDateOfPublishing();
        }
        if (mtAssessment.getQuestionList() != null) {
            searchKey = searchKey + " " + mtAssessment.getQuestionList();
        }
        return searchKey;
    }
}
