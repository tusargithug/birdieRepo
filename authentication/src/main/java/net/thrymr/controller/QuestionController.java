package net.thrymr.controller;

import net.thrymr.model.master.MtQuestion;
import net.thrymr.services.QuestionService;
import net.thrymr.utils.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/question")
public class QuestionController {
    private final Logger logger = LoggerFactory.getLogger(QuestionController.class);

    private final QuestionService questionService;

    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }
    // save  question
    @PostMapping("/save")
    public ApiResponse save(@RequestBody MtQuestion request) {
        logger.info("save question service started");
        ApiResponse apiResponse=   questionService.save(request);
        logger.info("save question service completed");
        return new ApiResponse(HttpStatus.OK, "",apiResponse);
    }
    // get question by id
    @GetMapping("/get/{id}")
    public ApiResponse getQuestionById(@PathVariable Long id) {
        logger.info("Get question service started");
        ApiResponse apiResponse=   questionService.getQuestionById(id);
        logger.info("Get question service completed");
        return new ApiResponse(HttpStatus.OK,"", apiResponse);
    }
    // deleted by id
    @DeleteMapping("/delete/{id}")
    public ApiResponse deleteQuestionById(@PathVariable Long id) {
        logger.info("delete question service started");

        return  questionService.deleteQuestionById(id);
    }



    // get answers by question  id
    @GetMapping("/get/answers/{id}")
    public ApiResponse getAnswersByQuestionId(@PathVariable Long id) {
        logger.info("Get answers by question id service started");
        ApiResponse apiResponse=   questionService.getAnswersByQuestionId(id);
        logger.info("Get answers by question id service completed");
        return new ApiResponse(HttpStatus.OK,"", apiResponse);
    }

    // get all questions
    @GetMapping("/get/All/question")
    public ApiResponse getAllQuestions() {
        logger.info("Get all question service started");
        ApiResponse apiResponse=   questionService.getAllQuestions();
        logger.info("Get all question service completed");
        return new ApiResponse(HttpStatus.OK,"", apiResponse);
    }

}
