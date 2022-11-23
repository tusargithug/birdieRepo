package net.thrymr.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class MeditationDto {

    private Long id;

    private String name;

    private String meditationVideoLink;

    private Boolean isActive;
}
