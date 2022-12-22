package net.thrymr.services;

import net.thrymr.dto.OptionsDto;
import net.thrymr.dto.QuestionDto;
import net.thrymr.model.master.MtOptions;
import net.thrymr.model.master.MtQuestion;

import java.util.List;

public interface QuestionAndOptionsService {
    String createQuestion(List<QuestionDto> request);

    MtQuestion getQuestionById(Long id);

    String deleteQuestionById(Long id);

    List<MtOptions> getAnswersByQuestionId(Long id);

    List<MtQuestion> getAllQuestions();

    String updateQuestionById(QuestionDto request);

    String createOptions(OptionsDto request);

    String updateOptionById(OptionsDto request);

    MtOptions getOptionById(Long id);

    List<MtOptions> getAllOption();

    String deleteOptionById(Long id);
}
