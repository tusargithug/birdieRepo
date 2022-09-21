package net.thrymr.dto;

import jdk.jfr.Frequency;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class AssignmentDto {

    private Long id;

    private String description;

    private String name;

    private String duration;

    private boolean isActive=false;

    private String instructions;
    //  TODO  need to create enum
   // private FrequencyType frequencyType;

    private List<QuestionDto>questionDtoList=new ArrayList<>();
}
