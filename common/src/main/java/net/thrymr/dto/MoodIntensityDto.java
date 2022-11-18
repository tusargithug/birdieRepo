package net.thrymr.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class MoodIntensityDto {
    private Long id;

    private String name;
    private Float score;
    private int sequence;

    private String description;

    private String emoji;

    private MoodInfoDto moodInfoDto;


}
