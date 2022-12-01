package net.thrymr.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class TeamDto {
    private Long id;
    private String teamId;
    private String teamName;
    private Boolean status;
    private Integer pageNumber;
    private Integer PageSize;
    private String searchKey;
    private Long siteId;
    private Boolean sortTeamName;
    private String shiftStartAt;
    private String shiftEndAt;
    private String shiftTimings;
    private List<Long> siteIds;

}
