package net.thrymr.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class OptionsDto {
    private Long id;
    private String  textAnswer;
    private Long questionId;
    private Long userCourseId;
    private Boolean isActive;
}
