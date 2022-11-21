package net.thrymr.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.thrymr.model.UserMoodCheckIn;

import java.util.LinkedList;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
public class MoodSourceDto {

    private String name;

    private String description;

    private FileEntity icon;

    private int sequence;

    private String category;

    private List<UserMoodCheckIn> userMoodCheckIns = new LinkedList<>();
}
