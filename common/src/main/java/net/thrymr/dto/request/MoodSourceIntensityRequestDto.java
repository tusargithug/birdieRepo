package net.thrymr.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class MoodSourceIntensityRequestDto {

    private Long id;
    private Long appUserId;
    private Long intensityId;
    private Long moodInfoId;
    private Long userSourceCheckedInId;
    private List<Long> moodSourceIdList=new ArrayList<>();
    private List<Long> sourceIds=new ArrayList<>();
    private String intensityDescription;
    private String loginUserMail;

}
