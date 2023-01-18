package net.thrymr.dto.response;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
public class MoodCheckInMonthResponse {

    private String monthName;
    private long positiveMoodRes;
    private long negativeMoodRes;
    private long positiveHighIntensityRes;
    private long negativeHighIntensityRes;
    private long positiveLowIntensityRes;
    private long negativeLowIntensityRes;

}
