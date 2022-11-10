package net.thrymr.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.thrymr.enums.FrequencyType;
import net.thrymr.model.master.MtOptions;
import net.thrymr.model.master.MtQuestion;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.ArrayList;
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

    private FrequencyType frequencyType;

    private String high;

    private String moderate;

    private String low;

    private Boolean isActive;
}
