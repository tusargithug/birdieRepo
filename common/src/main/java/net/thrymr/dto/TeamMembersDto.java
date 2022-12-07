package net.thrymr.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class TeamMembersDto {
    private Long id;
    private List<Long> appUserIdList=new ArrayList<>();
    private Long teamId;
    private String alerts;
}
