package net.thrymr.dto;

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

    private List<QuestionDto>questionDtoList=new ArrayList<>();
}
