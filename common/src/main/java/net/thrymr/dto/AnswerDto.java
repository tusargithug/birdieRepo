package net.thrymr.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class AnswerDto {

    private Long id;

    private String  textAnswer;

    private QuestionDto questionDto;
}
