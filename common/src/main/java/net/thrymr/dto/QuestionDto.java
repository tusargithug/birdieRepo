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

    private Set<OptionsDto> answersDto = new HashSet<>();
//tutorial refernce id
    private String videoReference;


    //TODO questionType
   // one month
     //       6 month
    //appoinment

}
