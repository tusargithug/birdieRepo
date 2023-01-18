package net.thrymr.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class UserMoodCheckInResponse {

    private int totalEmployees;

    private List<MoodCheckInMonthResponse> moodCheckInMonthResponseList;

    private List<UserMoodAverages> userMoodAveragesList;

}
