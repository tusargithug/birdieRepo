package net.thrymr.dto.slotRequest;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Getter
@Setter
@NoArgsConstructor
public class SlotDetailsDto {


    private String startTime;

    private String endTime;

    private Integer duration;

    private String slotShift;

    private String dayOfWeek;

    private String date;

    //Date range
    private String toDate;

    private String fromDate;

}
