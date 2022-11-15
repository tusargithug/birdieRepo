package net.thrymr.services.impl;


import net.thrymr.dto.OptionsDto;
import net.thrymr.dto.QuestionDto;
import net.thrymr.enums.QuestionCalType;
import net.thrymr.model.UserCourse;
import net.thrymr.model.master.MtAssessment;
import net.thrymr.model.master.MtOptions;
import net.thrymr.model.master.MtQuestion;
import net.thrymr.model.master.PsychometricTest;
import net.thrymr.repository.*;
import net.thrymr.services.QuestionAndOptionsService;
import net.thrymr.utils.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class QuestionAndOptionsServiceImpl implements QuestionAndOptionsService {

    @Autowired
    OptionsRepo optionsRepo;
    @Autowired
    QuestionRepo questionRepo;
    @Autowired
    PsychometricTestRepo psychometricTestRepo;
    @Autowired
    AssessmentRepo assessmentRepo;
    @Autowired
    UserCourseRepo userCourseRepo;

    @Override
    public String createQuestion(QuestionDto request) {
        MtQuestion question = new MtQuestion();
        question.setQuestion(request.getQuestion());
        question.setQuestionCalType(QuestionCalType.valueOf(request.getQuestionCalType()));
        question.setSequence(request.getSequence());
        if (Validator.isValid(request.getPsychometricTestId())) {
            Optional<PsychometricTest> optionalPsychometricTest = psychometricTestRepo.findById(request.getPsychometricTestId());
            if (optionalPsychometricTest.isPresent()) {
                question.setPsychometricTest(optionalPsychometricTest.get());
            }
        }
        if (Validator.isValid(request.getAssessmentId())) {
            Optional<MtAssessment> optionalAssessment = assessmentRepo.findById(request.getAssessmentId());
            if (optionalAssessment.isPresent()) {
                question.setAssessment(optionalAssessment.get());
            }
        }
        if (request.getIsActive()!=null && request.getIsActive().equals(Boolean.TRUE)) {
            question.setIsActive(request.getIsActive());
        }
        questionRepo.save(question);
        return "create question successfully";
    }

    @Override
    public MtQuestion getQuestionById(Long id) {
        MtQuestion question = null;
        if (Validator.isValid(id)) {
            Optional<MtQuestion> optionalQuestion = questionRepo.findById(id);
            if (optionalQuestion.isPresent() && optionalQuestion.get().getIsActive().equals(Boolean.TRUE)) {
                question = optionalQuestion.get();
                return question;
            }
        }
        return new MtQuestion();
    }

    @Override
    public String deleteQuestionById(Long id) {
        MtQuestion question = null;
        if (Validator.isValid(id)) {
            Optional<MtQuestion> optionalQuestion = questionRepo.findById(id);
            if (optionalQuestion.isPresent()) {
                question = optionalQuestion.get();
                question.setIsActive(Boolean.FALSE);
                question.setIsDeleted(Boolean.TRUE);
                questionRepo.save(question);
                return "delete successfully";
            }
        }
        return "this question id not found in database";
    }

    @Override
    public List<MtOptions> getAnswersByQuestionId(Long id) {
        List<MtOptions> mtOptionsList = optionsRepo.findAllByQuestion(id);
        if (!mtOptionsList.isEmpty()) {
            return new ArrayList<>(mtOptionsList);
        }
        return new ArrayList<>();
    }

    @Override
    public List<MtQuestion> getAllQuestions() {
        List<MtQuestion> mtQuestionList = questionRepo.findAll();
        if (!mtQuestionList.isEmpty()) {
            return mtQuestionList.stream().filter(obj -> obj.getIsActive().equals(Boolean.TRUE)).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    @Override
    public String updateQuestionById(QuestionDto request) {
        MtQuestion question = null;
        if (Validator.isValid(request.getId())) {
            Optional<MtQuestion> optionalQuestion = questionRepo.findById(request.getId());
            if (optionalQuestion.isPresent()) {
                question = optionalQuestion.get();
                if (Validator.isValid(request.getQuestion())) {
                    question.setQuestion(request.getQuestion());
                }
                if (Validator.isValid(request.getQuestionCalType())) {
                    question.setQuestionCalType(QuestionCalType.valueOf(request.getQuestionCalType()));
                }
                if (Validator.isValid(request.getSequence())) {
                    question.setSequence(request.getSequence());
                }
                if (Validator.isValid(request.getPsychometricTestId())) {
                    Optional<PsychometricTest> optionalPsychometricTest = psychometricTestRepo.findById(request.getPsychometricTestId());
                    if (optionalPsychometricTest.isPresent()) {
                        question.setPsychometricTest(optionalPsychometricTest.get());
                    }
                }
                if (Validator.isValid(request.getAssessmentId())) {
                    Optional<MtAssessment> optionalAssessment = assessmentRepo.findById(request.getAssessmentId());
                    if (optionalAssessment.isPresent()) {
                        question.setAssessment(optionalAssessment.get());
                    }
                }
                if (request.getIsActive()!=null && request.getIsActive().equals(Boolean.TRUE) || request.getIsActive().equals(Boolean.FALSE)) {
                    question.setIsActive(request.getIsActive());
                }
                questionRepo.save(question);
                return "update question successfully";
            }
        }
        return "this question id not in database";
    }

    @Override
    public String createOptions(OptionsDto request) {
        MtOptions options = new MtOptions();
        if (Validator.isValid(request.getQuestionId())) {
            Optional<MtQuestion> optionalOptions = questionRepo.findById(request.getQuestionId());
            if (optionalOptions.isPresent()) {
                options.setQuestion(optionalOptions.get());
            }
        }
        options.setTextAnswer(request.getTextAnswer());
        if (Validator.isValid(request.getUserCourseId())) {
            Optional<UserCourse> optionalUserCourse = userCourseRepo.findById(request.getUserCourseId());
            if (optionalUserCourse.isPresent()) {
                options.setUserCourse(optionalUserCourse.get());
            }
        }
        if (request.getIsActive().equals(Boolean.TRUE)) {
            options.setIsActive(request.getIsActive());
        }
        optionsRepo.save(options);
        return "created option successfully";
    }

    @Override
    public String updateOptionById(OptionsDto request) {
        MtOptions options = null;
        if (Validator.isValid(request.getId())) {
            Optional<MtOptions> optionsOptional = optionsRepo.findById(request.getId());
            if (optionsOptional.isPresent()) {
                options = optionsOptional.get();
                if (Validator.isValid(request.getQuestionId())) {
                    Optional<MtQuestion> optionalOptions = questionRepo.findById(request.getQuestionId());
                    if (optionalOptions.isPresent()) {
                        options.setQuestion(optionalOptions.get());
                    }
                }
                if (Validator.isValid(request.getTextAnswer())) {
                    System.out.println(request.getTextAnswer());
                    options.setTextAnswer(request.getTextAnswer());
                }
                if (Validator.isValid(request.getUserCourseId())) {
                    Optional<UserCourse> optionalUserCourse = userCourseRepo.findById(request.getUserCourseId());
                    if (optionalUserCourse.isPresent()) {
                        options.setUserCourse(optionalUserCourse.get());
                    }
                }
                if (request.getIsActive().equals(Boolean.TRUE) || request.getIsActive().equals(Boolean.FALSE)) {
                    options.setIsActive(request.getIsActive());
                }
                optionsRepo.save(options);
            }
            return "Option updated successfully";
        } else {
            return "this option id not in database ";
        }
    }

    @Override
    public MtOptions getOptionById(Long id) {
        MtOptions option = null;
        if (Validator.isValid(id)) {
            Optional<MtOptions> optionsOptional = optionsRepo.findById(id);
            if (optionsOptional.isPresent() && optionsOptional.get().getIsActive().equals(Boolean.TRUE)) {
                option = optionsOptional.get();
                return option;
            }
        }
        return new MtOptions();
    }

    @Override
    public List<MtOptions> getAllOption() {
        List<MtOptions> optionalList = optionsRepo.findAll();
        if (!optionalList.isEmpty()) {
            return optionalList.stream().filter(obj -> obj.getIsActive().equals(Boolean.TRUE)).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    @Override
    public String deleteOptionById(Long id) {
        MtOptions options = null;
        if (Validator.isValid(id)) {
            Optional<MtOptions> optionsOptional = optionsRepo.findById(id);
            if (optionsOptional.isPresent()) {
                options = optionsOptional.get();
                options.setIsActive(Boolean.FALSE);
                options.setIsDeleted(Boolean.TRUE);
                optionsRepo.save(options);
            }
        }
        return "option deleted successfully";
    }
}
