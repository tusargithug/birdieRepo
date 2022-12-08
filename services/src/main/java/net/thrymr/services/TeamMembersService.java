package net.thrymr.services;

import net.thrymr.dto.TeamMembersDto;
import net.thrymr.model.TeamMembers;

import java.util.List;
import java.util.Set;

public interface TeamMembersService {
    String addEmployeeToTeam(TeamMembersDto request);

    String updateTeamMemberById(TeamMembersDto request);

    Set<TeamMembers> getTeamMemberById(Long id);

   List<TeamMembers> getAllTeamMember();

    String deleteTeamMember(Long id);


}
