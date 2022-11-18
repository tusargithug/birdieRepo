package net.thrymr.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private String questionCalType;
    private Integer Sequence;
    private Boolean isActive;
    //TODO questionType
    // one month
    //       6 month
    //appoinment
}
