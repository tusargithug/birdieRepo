package net.thrymr.controller;

import net.thrymr.dto.TeamMembersDto;
import net.thrymr.model.TeamMembers;
import net.thrymr.services.TeamMembersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TeamMembersController {
    @Autowired
    TeamMembersService teamMembersService;

    @MutationMapping(name = "saveTeamMembers")
    public String saveTeamMembers(@Argument(name = "input") TeamMembersDto request) {
        return teamMembersService.saveTeamMembers(request);
    }

    @MutationMapping(name = "updateTeamMemberById")
    public String updateTeamMemberById(@Argument(name = "input") TeamMembersDto request) {
        return teamMembersService.updateTeamMemberById(request);
    }

    @QueryMapping(name="getTeamMemberById")
    public TeamMembers getTeamMemberById(@Argument Long id){
        return teamMembersService.getTeamMemberById(id);
    }

    @QueryMapping(name="getAllTeamMember")
    public List<TeamMembers> getAllTeamMember(){
        return teamMembersService.getAllTeamMember();
    }

    @QueryMapping(name = "deleteTeamMember")
    public String deleteTeamMember(@Argument Long id){
        return teamMembersService.deleteTeamMember(id);
    }

}
