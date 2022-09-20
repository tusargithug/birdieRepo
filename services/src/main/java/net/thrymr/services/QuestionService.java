package net.thrymr.services;

import net.thrymr.model.master.Question;
import net.thrymr.utils.ApiResponse;

public interface QuestionService {
    ApiResponse save(Question request);

    ApiResponse getQuestionById(Long id);

    ApiResponse deleteQuestionById(Long id);

    ApiResponse getAnswersByQuestionId(Long id);

    ApiResponse getAllQuestions();
}
