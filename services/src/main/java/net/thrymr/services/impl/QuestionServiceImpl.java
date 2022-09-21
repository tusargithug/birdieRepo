package net.thrymr.services.impl;

import net.thrymr.dto.OptionsDto;
import net.thrymr.dto.QuestionDto;
import net.thrymr.model.master.MtOptions;
import net.thrymr.model.master.MtQuestion;
import net.thrymr.repository.MtOptionsRepo;
import net.thrymr.repository.QuestionRepo;
import net.thrymr.services.QuestionService;
import net.thrymr.utils.ApiResponse;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class QuestionServiceImpl implements QuestionService {

    private final MtOptionsRepo mtOptionsRepo;

    private final Environment environment;

    private  final QuestionRepo questionRepo;

    public QuestionServiceImpl(MtOptionsRepo mtOptionsRepo, Environment environment, QuestionRepo questionRepo) {
        this.mtOptionsRepo = mtOptionsRepo;
        this.environment = environment;
        this.questionRepo = questionRepo;
    }

    @Override
    public ApiResponse save(MtQuestion request) {
        return null;
    }

    @Override
    public ApiResponse getQuestionById(Long id) {
        Optional<MtQuestion> optionalQuestion= questionRepo.findById(id);
        if(optionalQuestion.isPresent()){
            questionRepo.delete(optionalQuestion.get());
            return new ApiResponse(HttpStatus.OK,environment.getProperty("QUESTION_DELETED"));
        }

        return new ApiResponse(HttpStatus.OK,environment.getProperty("QUESTION_NOT_FOUND"));
    }

    @Override
    public ApiResponse deleteQuestionById(Long id) {
        Optional<MtQuestion> optionalQuestion= questionRepo.findById(id);
        return optionalQuestion.map(question -> new ApiResponse(HttpStatus.OK, environment.getProperty("SUCCESS"),this.entityToDto(question))).orElseGet(() -> new ApiResponse(HttpStatus.OK, environment.getProperty("QUESTION_NOT_FOUND")));

    }

    @Override
    public ApiResponse getAnswersByQuestionId(Long id) {
        List<MtOptions> mtOptionsList = mtOptionsRepo.findAllByMtQuestionId(id);
        if(!mtOptionsList.isEmpty()){
            List<OptionsDto> optionsDtoList =  mtOptionsList.stream().map(this::entityToDto).toList();
            return new ApiResponse(HttpStatus.OK,environment.getProperty("SUCCESS"), optionsDtoList);
        }
        return new ApiResponse(HttpStatus.OK,environment.getProperty("NO_ANSWERS_FOUND"));


    }

    @Override
    public ApiResponse getAllQuestions() {
        List<MtQuestion> mtQuestionList =questionRepo.findAll();
        if(!mtQuestionList.isEmpty()){
            List<QuestionDto>  questionDtoList =  mtQuestionList.stream().map(this::entityToDto).toList();
            return new ApiResponse(HttpStatus.OK,environment.getProperty("SUCCESS"),questionDtoList);
        }

        return new ApiResponse(HttpStatus.OK,environment.getProperty("QUESTION_NOT_FOUND"));
    }

    private OptionsDto entityToDto(MtOptions request){
        OptionsDto dto=new OptionsDto();
        dto.setId(request.getId());
        dto.setTextAnswer(request.getTextAnswer());

        return dto;
    }

    private QuestionDto entityToDto(MtQuestion request){
        QuestionDto dto=new QuestionDto();
        dto.setId(request.getId());
        dto.setQuestion(request.getQuestion());

        return dto;
    }
}
