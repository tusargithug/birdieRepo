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
        if (request != null && Validator.isValid(request.getAppUserIdList()) && request.getTeamId() != null) {
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
                        if (appUser.getRoles().equals(Roles.TEAM_LEADER) || appUser.getRoles().equals(Roles.TEAM_MANAGER) || appUser.getRoles().equals(Roles.DIRECTOR) || appUser.getRoles().equals(Roles.ACCOUNT_MANAGER) || appUser.getRoles().equals(Roles.GENERAL_MANAGER) || appUser.getRoles().equals(Roles.SENIOR_MANAGER)) {
                           // appUser.setAlerts(request.getAlerts());
                            teamMembers.setAppUser(appUser);
                            if (team != null) {
                                teamMembers.setTeam(team);
                            }
                        } else {
                            teamMembers.setAppUser(appUser);
                            if (team != null) {
                                teamMembers.setTeam(team);
                            }
                        }
                        appUser.setSearchKey(appUser.getSearchKey() + " " + getAppUserSearchKey(appUser));
                        appUserRepo.save(appUser);
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
        if (Validator.isValid(request.getPreviousTeamId())) {
            List<TeamMembers> teamMembersList = teamMembersRepo.findAllByTeamId(request.getPreviousTeamId());
            if (!teamMembersList.isEmpty()) {
                if (!request.getAppUserIdList().isEmpty() && request.getPreviousTeamId() != null && request.getNewTeamId() != null) {
                    List<AppUser> appUserList = appUserRepo.findAllByIdIn(request.getAppUserIdList());
                    if (!appUserList.isEmpty()) {
                        Optional<Team> optionalTeam = teamRepo.findById(request.getNewTeamId());
                        Team team = null;
                        if (optionalTeam.isPresent()) {
                            team = optionalTeam.get();
                        }
                        for (TeamMembers teamMembers : teamMembersList) {
                            for (AppUser appUser : appUserList) {
                                if (teamMembersRepo.existsByAppUserId(appUser.getId())) {
                                    if (appUser.getRoles() != null) {
                                        Optional<TeamMembers> optionalTeamMembers = teamMembersRepo.findByAppUserId(appUser.getId());
                                        TeamMembers members = null;
                                        if (optionalTeamMembers.isPresent()) {
                                            members = optionalTeamMembers.get();
                                        }
                                        if (appUser.getRoles().equals(Roles.TEAM_LEADER) || appUser.getRoles().equals(Roles.TEAM_MANAGER) || appUser.getRoles().equals(Roles.DIRECTOR) || appUser.getRoles().equals(Roles.ACCOUNT_MANAGER) || appUser.getRoles().equals(Roles.GENERAL_MANAGER) || appUser.getRoles().equals(Roles.SENIOR_MANAGER)) {
                                           // appUser.setAlerts(request.getAlerts());
                                            appUser.setSearchKey(appUser.getSearchKey() + " " + getAppUserSearchKey(appUser));
                                            members.setAppUser(appUser);
                                            if (team != null) {
                                                members.setTeam(team);
                                            }
                                        } else {
                                            members.setAppUser(appUser);
                                            if (team != null) {
                                                members.setTeam(team);
                                            }
                                        }
                                    }
                                    appUserRepo.save(appUser);
                                    teamMembersRepo.save(teamMembers);
                                } else {
                                    TeamMembers insertNewRecord = new TeamMembers();
                                    if (appUser.getRoles().equals(Roles.TEAM_LEADER) || appUser.getRoles().equals(Roles.TEAM_MANAGER) || appUser.getRoles().equals(Roles.DIRECTOR) || appUser.getRoles().equals(Roles.ACCOUNT_MANAGER) || appUser.getRoles().equals(Roles.GENERAL_MANAGER) || appUser.getRoles().equals(Roles.SENIOR_MANAGER)) {
                                       // appUser.setAlerts(request.getAlerts());
                                        insertNewRecord.setAppUser(appUser);
                                        appUser.setSearchKey(appUser.getSearchKey() + " " + getAppUserSearchKey(appUser));
                                        if (team != null) {
                                            insertNewRecord.setTeam(team);
                                        }
                                    } else {
                                        insertNewRecord.setAppUser(appUser);
                                        if (team != null) {
                                            insertNewRecord.setTeam(team);
                                        }
                                    }
                                    appUserRepo.save(appUser);
                                    teamMembersRepo.save(insertNewRecord);
                                }
                            }
                            return "Team members update successfully";
                        }
                    }
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
        return "This id not present database";
    }
}
