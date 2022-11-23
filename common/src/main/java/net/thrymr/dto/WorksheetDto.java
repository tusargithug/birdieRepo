package net.thrymr.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class WorksheetDto {

    private Long id;

    private String name;

    private String description;

    private Boolean isActive;
}
