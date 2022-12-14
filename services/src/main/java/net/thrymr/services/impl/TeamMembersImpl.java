package net.thrymr.services.impl;

import lombok.Setter;
import net.thrymr.dto.TeamMembersDto;
import net.thrymr.enums.Alerts;
import net.thrymr.enums.Roles;
import net.thrymr.model.AppUser;
import net.thrymr.model.Team;
import net.thrymr.model.TeamMembers;
import net.thrymr.repository.AppUserRepo;
import net.thrymr.repository.TeamMembersRepo;
import net.thrymr.repository.TeamRepo;
import net.thrymr.services.TeamMembersService;
import net.thrymr.utils.Validator;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.sl.draw.geom.GuideIf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
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
    public String addEmployeeToTeam(TeamMembersDto request) {
        if (Validator.isValid(request.getAppUserIdList()) && request.getTeamId() != null) {
            List<AppUser> appUserList = appUserRepo.findAllByIdIn(request.getAppUserIdList());
            if (!appUserList.isEmpty()) {
                Optional<Team> optionalTeam = teamRepo.findById(request.getTeamId());
                Team team = null;
                if (optionalTeam.isPresent()) {
                    team = optionalTeam.get();
                }
                for (AppUser appUser : appUserList) {
                    TeamMembers teamMembers = new TeamMembers();
                    if (appUser != null && appUser.getRoles() != null) {
                        teamMembers.setAppUser(appUser);
                        if (team != null) {
                            teamMembers.setTeam(team);
                        }
                        teamMembersRepo.save(teamMembers);
                    }
                }
            }
        }
        return "Team members saved successfully";
    }

    public String getAppUserSearchKey(AppUser appUser) {
        String searchKey = "";
        if (appUser.getUserName() != null) {
            searchKey = searchKey + " " + appUser.getAlerts();
        }
        return searchKey;
    }

    @Override
    public String updateTeamMemberById(TeamMembersDto request) {
        if (!request.getAppUserIdList().isEmpty() && Validator.isValid(request.getTeamId())) {
            List<TeamMembers> teamMembersList = teamMembersRepo.findAllByTeamId(request.getTeamId());
            List<Long> existedUsers = teamMembersList.stream().filter(user -> user.getAppUser() != null).map(obj -> obj.getAppUser().getId()).collect(Collectors.toList());
            List<Long> allUsersFromRequest = request.getAppUserIdList();
            List<Long> list = new ArrayList<>(CollectionUtils.disjunction(allUsersFromRequest, existedUsers));
            System.out.println(list);
            if (allUsersFromRequest != null) {
                List<AppUser> appUserList = appUserRepo.findAllByIdIn(list);
                if (!appUserList.isEmpty()) {
                    Optional<Team> optionalTeam = teamRepo.findById(request.getTeamId());
                    Team team = null;
                    if (optionalTeam.isPresent()) {
                        team = optionalTeam.get();
                    }
                  //  for (TeamMembers teamMembers : teamMembersList) {
                        for (AppUser appUser : appUserList) {
                            if (!teamMembersRepo.existsByAppUserId(appUser.getId())) {
                                TeamMembers insertNewRecord = new TeamMembers();
                                Optional<TeamMembers> optionalTeamMembers = teamMembersRepo.findByAppUserId(appUser.getId());
                                TeamMembers members = null;
                                if (optionalTeamMembers.isPresent()) {
                                    members = optionalTeamMembers.get();
                                }
                                insertNewRecord.setAppUser(appUser);
                                if (team != null) {
                                    insertNewRecord.setTeam(team);
                                }
                                teamMembersRepo.save(insertNewRecord);
                            } else {
                                Optional<TeamMembers> optionalTeamMembers = teamMembersRepo.findByAppUserId(appUser.getId());
                                TeamMembers members = null;
                                if (optionalTeamMembers.isPresent()) {
                                    members = optionalTeamMembers.get();
                                }
                                members.setIsActive(Boolean.FALSE);
                                members.setIsDeleted(Boolean.TRUE);
                                if (team != null) {
                                    members.setTeam(team);
                                }
                                teamMembersRepo.save(members);
                            }
                        }
                   // }
                    return "Team members update successfully";
                }
            }
        }
        return "give the valid appUser id's";
    }


    @Override
    public Set<TeamMembers> getTeamMemberById(Long id) {
        if (Validator.isValid(id)) {
            List<TeamMembers> teamMembers= teamMembersRepo.findAllByTeamId(id);
            if(!teamMembers.isEmpty()){
                return teamMembers.stream().filter(obj -> obj.getIsDeleted().equals(Boolean.FALSE)).collect(Collectors.toSet());
            }
        }
        return new HashSet<>();
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
    public String deleteTeamMember(TeamMembersDto request) {
        if (Validator.isValid(request.getTeamId())) {
            List<TeamMembers> teamMembersList = teamMembersRepo.findAllByTeamId(request.getTeamId());
            if (!teamMembersList.isEmpty()) {
                for (TeamMembers teamMembers : teamMembersList) {
                    List<TeamMembers> appUserList = teamMembersRepo.findAllByAppUserIdIn(request.getAppUserIdList());
                    for (TeamMembers appUser : appUserList) {
                        appUser.setIsActive(Boolean.FALSE);
                        appUser.setIsDeleted(Boolean.TRUE);
                        teamMembersRepo.save(teamMembers);
                        return "Team member record deleted successfully";
                    }
                }
            }
        }
        return "This id not present database";
    }
}
