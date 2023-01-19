package net.thrymr.dto.response;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class UserMoodAverageInfo {

    private String moodName;
    private int moodCount;
}
