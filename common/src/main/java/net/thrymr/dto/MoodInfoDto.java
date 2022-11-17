package net.thrymr.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
public class MoodInfoDto {

    private Long id;

    private int sequence;

    private String moodType;

    private String intensityName;

    private String emoji;
}
