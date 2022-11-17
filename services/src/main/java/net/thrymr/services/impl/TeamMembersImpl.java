package net.thrymr.services.impl;

import lombok.Setter;
import net.thrymr.dto.TeamMembersDto;
import net.thrymr.model.AppUser;
import net.thrymr.model.TeamMembers;
import net.thrymr.model.master.MtTeam;
import net.thrymr.repository.AppUserRepo;
import net.thrymr.repository.TeamMembersRepo;
import net.thrymr.repository.TeamRepo;
import net.thrymr.services.TeamMembersService;
import net.thrymr.utils.Validator;
import org.apache.poi.sl.draw.geom.GuideIf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TeamMembersImpl implements TeamMembersService {
    @Autowired
    TeamMembersRepo teamMembersRepo;
    @Autowired
    AppUserRepo appUserRepo;
    @Autowired
    TeamRepo teamRepo;

    @Override
    public String saveTeamMembers(TeamMembersDto request) {
        TeamMembers teamMembers = new TeamMembers();
        if (Validator.isValid(request.getAppUserId())) {
            Optional<AppUser> optionalAppUser = appUserRepo.findById(request.getAppUserId());
            if (optionalAppUser.isPresent()) {
                teamMembers.setAppUser(optionalAppUser.get());
            }
        }
        if (Validator.isValid(request.getTeamId())) {
            Optional<MtTeam> optionalTeam = teamRepo.findById(request.getTeamId());
            if (optionalTeam.isPresent()) {
                teamMembers.setTeam(optionalTeam.get());
            }
        }
        teamMembersRepo.save(teamMembers);
        return "Team members saved successfully";
    }

    @Override
    public String updateTeamMemberById(TeamMembersDto request) {
        TeamMembers teamMembers = null;
        if (Validator.isValid(request.getId())) {
            Optional<TeamMembers> optionalTeamMembers = teamMembersRepo.findById(request.getId());
            if (optionalTeamMembers.isPresent()) {
                teamMembers = optionalTeamMembers.get();
                if (Validator.isValid(request.getAppUserId())) {
                    Optional<AppUser> optionalAppUser = appUserRepo.findById(request.getAppUserId());
                    if (optionalAppUser.isPresent()) {
                        teamMembers.setAppUser(optionalAppUser.get());
                    }
                }
                if (Validator.isValid(request.getTeamId())) {
                    Optional<MtTeam> optionalTeam = teamRepo.findById(request.getTeamId());
                    if (optionalTeam.isPresent()) {
                        teamMembers.setTeam(optionalTeam.get());
                    }
                }
                teamMembersRepo.save(teamMembers);
                return "Update team members updated successfully";
            }
        }
        return "This team member id not present in database";
    }

    @Override
    public TeamMembers getTeamMemberById(Long id) {
        TeamMembers teamMembers = null;
        if (Validator.isValid(id)) {
            Optional<TeamMembers> optionalTeamMembers = teamMembersRepo.findById(id);
            if (optionalTeamMembers.isPresent() && optionalTeamMembers.get().getIsActive().equals(Boolean.TRUE)) {
                teamMembers = optionalTeamMembers.get();
                return teamMembers;
            }
        }
        return new TeamMembers();
    }

    @Override
    public List<TeamMembers> getAllTeamMember() {
        List<TeamMembers> teamMembersList = teamMembersRepo.findAll();
        if (!teamMembersList.isEmpty()) {
            return teamMembersList.stream().filter(obj -> obj.getIsActive().equals(Boolean.TRUE)).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    @Override
    public String deleteTeamMember(Long id) {
        TeamMembers teamMembers = null;
        if (Validator.isValid(id)) {
            Optional<TeamMembers> optionalTeamMembers = teamMembersRepo.findById(id);
            if (optionalTeamMembers.isPresent()) {
                teamMembers = optionalTeamMembers.get();
                teamMembers.setIsActive(Boolean.FALSE);
                teamMembers.setIsDeleted(Boolean.TRUE);
                teamMembersRepo.save(teamMembers);
                return "Team member record deleted successfully";
            }
        }
        return "null";
    }
}
