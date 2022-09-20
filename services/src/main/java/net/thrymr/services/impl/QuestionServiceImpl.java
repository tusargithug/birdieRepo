package net.thrymr.services.impl;

import net.thrymr.dto.AnswerDto;
import net.thrymr.dto.QuestionDto;
import net.thrymr.model.Answer;
import net.thrymr.model.master.Question;
import net.thrymr.repository.AnswerRepo;
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

    private final AnswerRepo answerRepo;

    private final Environment environment;

    private  final QuestionRepo questionRepo;

    public QuestionServiceImpl(AnswerRepo answerRepo, Environment environment, QuestionRepo questionRepo) {
        this.answerRepo = answerRepo;
        this.environment = environment;
        this.questionRepo = questionRepo;
    }

    @Override
    public ApiResponse save(Question request) {
        return null;
    }

    @Override
    public ApiResponse getQuestionById(Long id) {
        Optional<Question> optionalQuestion= questionRepo.findById(id);
        if(optionalQuestion.isPresent()){
            questionRepo.delete(optionalQuestion.get());
            return new ApiResponse(HttpStatus.OK,environment.getProperty("QUESTION_DELETED"));
        }

        return new ApiResponse(HttpStatus.OK,environment.getProperty("QUESTION_NOT_FOUND"));
    }

    @Override
    public ApiResponse deleteQuestionById(Long id) {
        Optional<Question> optionalQuestion= questionRepo.findById(id);
        return optionalQuestion.map(question -> new ApiResponse(HttpStatus.OK, environment.getProperty("SUCCESS"),this.entityToDto(question))).orElseGet(() -> new ApiResponse(HttpStatus.OK, environment.getProperty("QUESTION_NOT_FOUND")));

    }

    @Override
    public ApiResponse getAnswersByQuestionId(Long id) {
        List<Answer>answerList=answerRepo.findAllByQuestionId(id);
        if(!answerList.isEmpty()){
            List<AnswerDto>  answerDtoList =  answerList.stream().map(this::entityToDto).toList();
            return new ApiResponse(HttpStatus.OK,environment.getProperty("SUCCESS"),answerDtoList);
        }
        return new ApiResponse(HttpStatus.OK,environment.getProperty("NO_ANSWERS_FOUND"));


    }

    @Override
    public ApiResponse getAllQuestions() {
        List<Question>questionList=questionRepo.findAll();
        if(!questionList.isEmpty()){
            List<QuestionDto>  questionDtoList =  questionList.stream().map(this::entityToDto).toList();
            return new ApiResponse(HttpStatus.OK,environment.getProperty("SUCCESS"),questionDtoList);
        }

        return new ApiResponse(HttpStatus.OK,environment.getProperty("QUESTION_NOT_FOUND"));
    }

    private AnswerDto entityToDto(Answer request){
        AnswerDto dto=new AnswerDto();
        dto.setId(request.getId());
        dto.setTextAnswer(request.getTextAnswer());

        return dto;
    }

    private QuestionDto entityToDto(Question request){
        QuestionDto dto=new QuestionDto();
        dto.setId(request.getId());
        dto.setQuestion(request.getQuestion());

        return dto;
    }
}
