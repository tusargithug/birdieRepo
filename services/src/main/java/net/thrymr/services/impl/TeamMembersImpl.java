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
        if (request != null && Validator.isValid(request.getAppUserIdList()) && request.getTeamId() != null && request.getAlerts() != null) {
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
                        if (appUser.getRoles().equals(Roles.TEAM_LEADER)) {
                            appUser.setAlerts(Alerts.valueOf(request.getAlerts()));
                            teamMembers.setAppUser(appUser);
                            if (team != null) {
                                teamMembers.setTeam(team);
                            }
                        }
                        if (appUser.getRoles().equals(Roles.TEAM_MANAGER)) {
                            appUser.setAlerts(Alerts.valueOf(request.getAlerts()));
                            teamMembers.setAppUser(appUser);
                            if (team != null) {
                                teamMembers.setTeam(team);
                            }
                        }

                        if (appUser.getRoles().equals(Roles.DIRECTOR)) {
                            appUser.setAlerts(Alerts.valueOf(request.getAlerts()));
                            teamMembers.setAppUser(appUser);
                            if (team != null) {
                                teamMembers.setTeam(team);
                            }
                        }
                        if (appUser.getRoles().equals(Roles.ACCOUNT_MANAGER)) {
                            appUser.setAlerts(Alerts.valueOf(request.getAlerts()));
                            teamMembers.setAppUser(appUser);
                            if (team != null) {
                                teamMembers.setTeam(team);
                            }
                        }
                        if (appUser.getRoles().equals(Roles.GENERAL_MANAGER)) {
                            appUser.setAlerts(Alerts.valueOf(request.getAlerts()));
                            teamMembers.setAppUser(appUser);
                            if (team != null) {
                                teamMembers.setTeam(team);
                            }
                        }
                        if (appUser.getRoles().equals(Roles.SENIOR_MANAGER)) {
                            appUser.setAlerts(Alerts.valueOf(request.getAlerts()));
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
        if (Validator.isValid(request.getId())) {
            Optional<TeamMembers> optionalTeamMembers = teamMembersRepo.findById(request.getId());
            if (optionalTeamMembers.isPresent()) {
                if (request != null && Validator.isValid(request.getAppUserIdList()) && request.getTeamId() != null && request.getAlerts() != null) {
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
                                if (appUser.getRoles().equals(Roles.TEAM_LEADER)) {
                                    appUser.setAlerts(Alerts.valueOf(request.getAlerts()));
                                    teamMembers.setAppUser(appUser);
                                    if (team != null) {
                                        teamMembers.setTeam(team);
                                    }
                                }
                                if (appUser.getRoles().equals(Roles.TEAM_MANAGER)) {
                                    appUser.setAlerts(Alerts.valueOf(request.getAlerts()));
                                    teamMembers.setAppUser(appUser);
                                    if (team != null) {
                                        teamMembers.setTeam(team);
                                    }
                                }

                                if (appUser.getRoles().equals(Roles.DIRECTOR)) {
                                    appUser.setAlerts(Alerts.valueOf(request.getAlerts()));
                                    teamMembers.setAppUser(appUser);
                                    if (team != null) {
                                        teamMembers.setTeam(team);
                                    }
                                }
                                if (appUser.getRoles().equals(Roles.ACCOUNT_MANAGER)) {
                                    appUser.setAlerts(Alerts.valueOf(request.getAlerts()));
                                    teamMembers.setAppUser(appUser);
                                    if (team != null) {
                                        teamMembers.setTeam(team);
                                    }
                                }
                                if (appUser.getRoles().equals(Roles.GENERAL_MANAGER)) {
                                    appUser.setAlerts(Alerts.valueOf(request.getAlerts()));
                                    teamMembers.setAppUser(appUser);
                                    if (team != null) {
                                        teamMembers.setTeam(team);
                                    }
                                }
                                if (appUser.getRoles().equals(Roles.SENIOR_MANAGER)) {
                                    appUser.setAlerts(Alerts.valueOf(request.getAlerts()));
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
                        return "Team members update successfully";
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
        return "null";
    }
}
