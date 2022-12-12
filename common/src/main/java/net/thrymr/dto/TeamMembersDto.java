package net.thrymr.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.thrymr.enums.Alerts;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class TeamMembersDto {
    private Long id;
    private List<Long> appUserIdList=new ArrayList<>();
    private Long teamId;
    private List<Alerts> alerts=new ArrayList<>();
    private Long previousTeamId;
    private Long newTeamId;
}
