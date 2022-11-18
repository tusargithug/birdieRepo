package net.thrymr.controller;

import net.thrymr.dto.OptionsDto;
import net.thrymr.dto.QuestionDto;
import net.thrymr.model.master.MtOptions;
import net.thrymr.model.master.MtQuestion;
import net.thrymr.services.QuestionAndOptionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/question")
public class QuestionAndOptionsController {

    @Autowired
    QuestionAndOptionsService questionAndOptionsService;

    @MutationMapping("createQuestion")
    public String createQuestion(@Argument(name = "input") QuestionDto request) {
        return questionAndOptionsService.createQuestion(request);
    }

    @MutationMapping("updateQuestionById")
    public String updateQuestionById(@Argument(name = "input") QuestionDto request) {
        return questionAndOptionsService.updateQuestionById(request);
    }

    @QueryMapping("getQuestionById")
    public MtQuestion getQuestionById(@Argument Long id) {
        return questionAndOptionsService.getQuestionById(id);
    }

    @MutationMapping("deleteQuestionById")
    public String deleteQuestionById(@Argument Long id) {
        return questionAndOptionsService.deleteQuestionById(id);
    }

    @QueryMapping("getAnswersByQuestionId")
    public List<MtOptions> getAnswersByQuestionId(@Argument Long id) {
        return questionAndOptionsService.getAnswersByQuestionId(id);
    }

    @QueryMapping("getAllQuestions")
    public List<MtQuestion> getAllQuestions() {
        return questionAndOptionsService.getAllQuestions();
    }

    @MutationMapping("createOptions")
    public String createOptions(@Argument(name = "input") OptionsDto request) {
        return questionAndOptionsService.createOptions(request);
    }

    @MutationMapping("updateOptionById")
    public String updateOptionById(@Argument(name = "input") OptionsDto request) {
        return questionAndOptionsService.updateOptionById(request);
    }

    @QueryMapping("getOptionById")
    public MtOptions getOptionById(@Argument Long id) {
        return questionAndOptionsService.getOptionById(id);
    }

    @QueryMapping("getAllOption")
    public List<MtOptions> getAllOption() {
        return questionAndOptionsService.getAllOption();
    }

    @MutationMapping("deleteOptionById")
    public String deleteOptionById(@Argument Long id) {
        return questionAndOptionsService.deleteOptionById(id);
    }

}
