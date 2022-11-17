package net.thrymr.services;

import net.thrymr.dto.TeamMembersDto;
import net.thrymr.model.TeamMembers;

import java.util.List;

public interface TeamMembersService {
    String saveTeamMembers(TeamMembersDto request);

    String updateTeamMemberById(TeamMembersDto request);

    TeamMembers getTeamMemberById(Long id);

   List<TeamMembers> getAllTeamMember();

    String deleteTeamMember(Long id);
}
