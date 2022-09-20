package net.thrymr.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.thrymr.model.UserMoodCheckIn;
import net.thrymr.model.master.MoodInfo;

import java.util.LinkedList;
import java.util.List;

/*
 *@author Chanda Veeresh
 *@version 1.0
 *@since  15-07-2022
 */
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
