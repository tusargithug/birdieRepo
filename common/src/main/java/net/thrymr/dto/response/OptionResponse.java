package net.thrymr.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OptionResponse {
    private Long id;
    private String  textAnswer;
    private Boolean isCorrect;
}
