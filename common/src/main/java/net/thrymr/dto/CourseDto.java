package net.thrymr.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class CourseDto {
    private Long id;

    private Long CategoryId;

    private String code;

    private String name;

    private int sequence;

    private String description;

    private Boolean isActive;
}
