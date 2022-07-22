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

    private List<MoodIntensity> intensities = new ArrayList<MoodIntensity>();

    private int sequence;

    private MoodType moodType;

    private String intensityName;
}
