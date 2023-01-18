package net.thrymr.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class UserMoodAverages {

    private String moodType;
    private List<UserMoodAverageInfo> userMoodAverageInfoList;
}
