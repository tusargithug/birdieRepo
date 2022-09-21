package net.thrymr.services;

import net.thrymr.model.master.MtQuestion;
import net.thrymr.utils.ApiResponse;

public interface QuestionService {
    ApiResponse save(MtQuestion request);

    ApiResponse getQuestionById(Long id);

    ApiResponse deleteQuestionById(Long id);

    ApiResponse getAnswersByQuestionId(Long id);

    ApiResponse getAllQuestions();
}
