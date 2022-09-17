package net.thrymr.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.thrymr.enums.MoodType;
import net.thrymr.model.master.MoodIntensity;

import java.util.ArrayList;
import java.util.List;

/*
 *@author Chanda Veeresh
 *@version 1.0
 *@since  15-07-2022
 */
@Getter
@Setter
@NoArgsConstructor
public class MoodInfoDto {

    private String moodName;

    //TODO dought
    private List<MoodIntensity> intensities = new ArrayList<>();

    private int sequence;

    private String moodType;

    private String intensityName;
}
