package net.thrymr.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class MoodSourceIntensityRequestDto {

    private Long intensityId;

    private List<Long> sourceIds=new ArrayList<>();

    private String intensityDescription;

    private String moreInfo;
}
