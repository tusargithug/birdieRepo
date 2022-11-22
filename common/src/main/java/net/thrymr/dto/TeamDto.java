package net.thrymr.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TeamDto {
    private Long id;
    private String teamId;
    private String teamName;
    private AppUserDto teamLeader;
    private AppUserDto teamManager;
    private ShiftTimingsDto shiftTimings;
    private SiteDto site;
    private Boolean status;
    private Integer pageNumber;
    private Integer PageSize;
    private String searchKey;
    private Long teamLeaderId;
    private Long teamManagerId;
    private Long siteId;
    private Long shiftTimingsId;
    private Boolean sortTeamName;
}
