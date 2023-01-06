package net.thrymr.dto.response;

import lombok.Getter;
import lombok.Setter;
import net.thrymr.enums.QuestionCalType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
public class QuestionResponse {
    private Long id;
    private String question;
    private List<OptionResponse> mtOptions = new ArrayList<>();
    private QuestionCalType questionCalType;
    private Integer sequence;
}
