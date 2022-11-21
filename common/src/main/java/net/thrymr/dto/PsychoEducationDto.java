package net.thrymr.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class PsychoEducationDto {

    private Long id;

    private String name;

    private String description;

    private Boolean isActive;

}
