package net.thrymr.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
//Question Bank  master
public class QuestionDto {
    private Long id;
    private String question;
    private String videoReference;
    private Long assessmentId;
    private Long psychometricTestId;
    private String setQuestionCalType;
    private Integer Sequence;
    //TODO questionType
   // one month
     //       6 month
    //appoinment
}
