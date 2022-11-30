package net.thrymr.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class AssessmentDto {

    private Long id;

    private String description;

    private String name;

    private String duration;

    private String instructions;

    private String frequencyType;

    private String high;

    private String moderate;

    private String low;

    private Boolean isActive;

    private String dateOfPublishing;

    private List<QuestionDto> questionDtoList;


}
