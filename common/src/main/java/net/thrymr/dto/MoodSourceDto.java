package net.thrymr.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.thrymr.model.UserMoodCheckIn;
import net.thrymr.model.master.FileEntity;
import java.util.LinkedList;
import java.util.List;

/*
 *@author Chanda Veeresh
 *@version 1.0
 *@since  16-08-2022
 */
@Getter
@Setter
@NoArgsConstructor
public class MoodSourceDto {

    private String name;

    private String description;

    private FileEntity icon;

    private int sequence;

    private String category;

    private List<UserMoodCheckIn> userMoodCheckIns = new LinkedList<UserMoodCheckIn>();
}
