package net.thrymr.services.impl;


import net.thrymr.dto.OptionsDto;
import net.thrymr.dto.QuestionDto;
import net.thrymr.enums.QuestionCalType;
import net.thrymr.model.Chapter;
import net.thrymr.model.UserCourse;
import net.thrymr.model.master.MtAssessment;
import net.thrymr.model.master.MtOptions;
import net.thrymr.model.master.MtQuestion;
import net.thrymr.model.master.PsychometricTest;
import net.thrymr.repository.*;
import net.thrymr.services.QuestionAndOptionsService;
import net.thrymr.utils.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
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
    @Autowired
    ChapterRepo chapterRepo;

    @Override
    public List<MtQuestion> createQuestion(List<QuestionDto> request) {
        List<MtQuestion> mtQuestionList = new ArrayList<>();
        if (Validator.isObjectValid(request)) {
            for (QuestionDto questionDto : request) {
                Set<MtOptions> mtOptionsList = new HashSet<>();
                MtQuestion question = new MtQuestion();
                question.setQuestion(questionDto.getQuestion());
                if (questionDto.getQuestionCalType() != null) {
                    question.setQuestionCalType(QuestionCalType.valueOf(questionDto.getQuestionCalType()));
                }
                if (questionDto.getSequence() != null) {
                    question.setSequence(questionDto.getSequence());
                }

                if (questionDto.getPsychometricTestId() != null) {
                    Optional<PsychometricTest> optionalPsychometricTest = psychometricTestRepo.findById(questionDto.getPsychometricTestId());
                    if (optionalPsychometricTest.isPresent()) {
                        question.setPsychometricTest(optionalPsychometricTest.get());
                    }
                }
                if (questionDto.getAssessmentId() != null) {
                    Optional<MtAssessment> optionalAssessment = assessmentRepo.findById(questionDto.getAssessmentId());
                    if (optionalAssessment.isPresent()) {
                        question.setAssessment(optionalAssessment.get());
                    }
                }

                if (questionDto.getChapterId() != null) {
                    Optional<Chapter> chapterOptional = chapterRepo.findById(questionDto.getChapterId());
                    if (chapterOptional.isPresent()) {
                        question.setChapter(chapterOptional.get());
                    }
                }
                question.setSearchKey(getAppUserSearchKey(question));
                question = questionRepo.save(question);
                for (OptionsDto optionsDto : questionDto.getOptionsDtoList()) {
                    MtOptions option = new MtOptions();
                    option.setQuestion(question);
                    option.setTextAnswer(optionsDto.getTextAnswer());
                    if (optionsDto.getIsCorrect().equals(Boolean.TRUE)) {
                        option.setIsCorrect(optionsDto.getIsCorrect());
                    }
                    if (optionsDto.getUserCourseId() != null) {
                        Optional<UserCourse> optionalUserCourse = userCourseRepo.findById(optionsDto.getUserCourseId());
                        if (optionalUserCourse.isPresent()) {
                            option.setUserCourse(optionalUserCourse.get());
                        }
                    }
                    option.setSearchKey(getOptionsSearchKey(option));
                    optionsRepo.save(option);
                    mtOptionsList.add(option);
                }

                mtQuestionList.add(question);
                question.setMtOptions(new HashSet<>(mtOptionsList));
            }
            return mtQuestionList;
        }
        return new ArrayList<>();
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
    public String deleteQuestionById(QuestionDto request) {
        MtQuestion question = null;
        if (Validator.isValid(request.getQuestionId())) {
            Optional<MtQuestion> optionalQuestion = questionRepo.findById(request.getQuestionId());
            if (optionalQuestion.isPresent()) {
                question = optionalQuestion.get();
                question.setIsActive(Boolean.FALSE);
                question.setIsDeleted(Boolean.TRUE);
                questionRepo.save(question);
            }
            List<MtOptions> optionsList = optionsRepo.findAllByQuestionId(request.getQuestionId());
            for (MtOptions mtOptions : optionsList) {
                for (OptionsDto optionsDto : request.getOptionsDtoList()) {
                    if (mtOptions.getId().equals(optionsDto.getId())) {
                        mtOptions.setIsActive(Boolean.FALSE);
                        mtOptions.setIsDeleted(Boolean.TRUE);
                        optionsRepo.save(mtOptions);
                    }
                }
            }
            return "delete successfully";
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
        List<MtQuestion> mtQuestionList = questionRepo.findAll(Sort.by(Sort.Direction.DESC, "createdOn"));
        if (!mtQuestionList.isEmpty()) {
            return mtQuestionList.stream().filter(obj -> obj.getIsActive().equals(Boolean.TRUE)).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    @Override
    public String updateQuestionById(List<QuestionDto> request) {
        MtQuestion question = null;
        if (Validator.isObjectValid(request)) {
            for (QuestionDto questionDto : request) {
                if (Validator.isValid(questionDto.getQuestionId())) {
                    Optional<MtQuestion> optionalQuestion = questionRepo.findById(questionDto.getQuestionId());
                    if (optionalQuestion.isPresent()) {
                        question = optionalQuestion.get();
                        if (Validator.isValid(questionDto.getQuestion())) {
                            question.setQuestion(questionDto.getQuestion());
                        }
                        if (Validator.isValid(questionDto.getQuestionCalType())) {
                            question.setQuestionCalType(QuestionCalType.valueOf(questionDto.getQuestionCalType()));
                        }
                        if (Validator.isValid(questionDto.getSequence())) {
                            question.setSequence(questionDto.getSequence());
                        }
                        if (Validator.isValid(questionDto.getPsychometricTestId())) {
                            Optional<PsychometricTest> optionalPsychometricTest = psychometricTestRepo.findById(questionDto.getPsychometricTestId());
                            if (optionalPsychometricTest.isPresent()) {
                                question.setPsychometricTest(optionalPsychometricTest.get());
                            }
                        }
                        if (Validator.isValid(questionDto.getAssessmentId())) {
                            Optional<MtAssessment> optionalAssessment = assessmentRepo.findById(questionDto.getAssessmentId());
                            if (optionalAssessment.isPresent()) {
                                question.setAssessment(optionalAssessment.get());
                            }
                        }
                        if (Validator.isValid(questionDto.getChapterId())) {
                            Optional<Chapter> chapterOptional = chapterRepo.findById(questionDto.getChapterId());
                            if (chapterOptional.isPresent()) {
                                question.setChapter(chapterOptional.get());
                            }
                        }
                        question.setSearchKey(getAppUserSearchKey(question));
                        questionRepo.save(question);
                        List<MtOptions> optionsList = optionsRepo.findAllByQuestionId(questionDto.getQuestionId());
                        if (!optionsList.isEmpty()) {
                            for (MtOptions mtOptions : optionsList) {
                                for (OptionsDto optionsDto : questionDto.getOptionsDtoList()) {
                                    if (mtOptions.getId().equals(optionsDto.getId())) {
                                        mtOptions.setQuestion(question);
                                        mtOptions.setTextAnswer(optionsDto.getTextAnswer());
                                        if (optionsDto.getIsCorrect() != null && (optionsDto.getIsCorrect().equals(Boolean.TRUE) || optionsDto.getIsCorrect().equals(Boolean.FALSE))) {
                                            mtOptions.setIsCorrect(optionsDto.getIsCorrect());
                                        }
                                        mtOptions.setSearchKey(getOptionsSearchKey(mtOptions));
                                        optionsRepo.save(mtOptions);
                                    }
                                }
                            }
                        }

                    }
                } else {

                    Set<MtOptions> mtOptionsList = new HashSet<>();
                    question = new MtQuestion();
                    question.setQuestion(questionDto.getQuestion());
                    if (questionDto.getQuestionCalType() != null) {
                        question.setQuestionCalType(QuestionCalType.valueOf(questionDto.getQuestionCalType()));
                    }
                    if (questionDto.getSequence() != null) {
                        question.setSequence(questionDto.getSequence());
                    }

                    if (questionDto.getPsychometricTestId() != null) {
                        Optional<PsychometricTest> optionalPsychometricTest = psychometricTestRepo.findById(questionDto.getPsychometricTestId());
                        if (optionalPsychometricTest.isPresent()) {
                            question.setPsychometricTest(optionalPsychometricTest.get());
                        }
                    }
                    if (questionDto.getAssessmentId() != null) {
                        Optional<MtAssessment> optionalAssessment = assessmentRepo.findById(questionDto.getAssessmentId());
                        if (optionalAssessment.isPresent()) {
                            question.setAssessment(optionalAssessment.get());
                        }
                    }

                    if (questionDto.getChapterId() != null) {
                        Optional<Chapter> chapterOptional = chapterRepo.findById(questionDto.getChapterId());
                        if (chapterOptional.isPresent()) {
                            question.setChapter(chapterOptional.get());
                        }
                    }
                    question.setSearchKey(getAppUserSearchKey(question));
                    question = questionRepo.save(question);
                    for (OptionsDto optionsDto : questionDto.getOptionsDtoList()) {
                        MtOptions option = new MtOptions();
                        option.setQuestion(question);
                        option.setTextAnswer(optionsDto.getTextAnswer());
                        if (optionsDto.getIsCorrect().equals(Boolean.TRUE)) {
                            option.setIsCorrect(optionsDto.getIsCorrect());
                        }
                        if (optionsDto.getUserCourseId() != null) {
                            Optional<UserCourse> optionalUserCourse = userCourseRepo.findById(optionsDto.getUserCourseId());
                            if (optionalUserCourse.isPresent()) {
                                option.setUserCourse(optionalUserCourse.get());
                            }
                        }
                        option.setSearchKey(getOptionsSearchKey(option));
                        optionsRepo.save(option);
                        mtOptionsList.add(option);
                    }

                    question.setMtOptions(new HashSet<>(mtOptionsList));
                }
            }
            return "update question successfully";
        }
        return "give valid input";
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
        options.setSearchKey(getOptionsSearchKey(options));
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
                options.setSearchKey(getOptionsSearchKey(options));
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

    public String getAppUserSearchKey(MtQuestion question) {
        String searchKey = "";
        if (question.getQuestion() != null) {
            searchKey = searchKey + " " + question.getQuestion();
        }
        if (question.getMtOptions() != null) {
            searchKey = searchKey + " " + question.getMtOptions();
        }
        if (question.getPsychometricTest() != null) {
            searchKey = searchKey + " " + question.getPsychometricTest();
        }
        if (question.getQuestionCalType() != null) {
            searchKey = searchKey + " " + question.getQuestionCalType();
        }
        if (question.getSequence() != null) {
            searchKey = searchKey + " " + question.getSequence();
        }
        if (question.getAssessment() != null) {
            searchKey = searchKey + " " + question.getAssessment();
        }
        return searchKey;
    }

    public String getOptionsSearchKey(MtOptions options) {
        String searchKey = "";
        if (options.getTextAnswer() != null) {
            searchKey = searchKey + " " + options.getTextAnswer();
        }
        if (options.getQuestion() != null) {
            searchKey = searchKey + " " + options.getQuestion();
        }
        if (options.getUserCourse() != null) {
            searchKey = searchKey + " " + options.getUserCourse();
        }
        if (options.getQuestion().getMtOptions() != null) {
            searchKey = searchKey + " " + options.getQuestion().getMtOptions().stream().map(MtOptions::getTextAnswer);
        }
        if (options.getQuestion().getMtOptions() != null) {
            searchKey = searchKey + " " + options.getQuestion().getMtOptions().stream().map(MtOptions::getIsCorrect);
        }
        return searchKey;
    }
}
