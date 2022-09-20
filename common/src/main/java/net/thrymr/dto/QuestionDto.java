package net.thrymr.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
public class QuestionDto {

    private Long id;

    private String question;

    private Set<AnswerDto> answersDto = new HashSet<>();

    private String videoReference;
}
