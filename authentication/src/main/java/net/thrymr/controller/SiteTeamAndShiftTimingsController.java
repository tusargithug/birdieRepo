package net.thrymr.controller;

import net.thrymr.dto.*;
import net.thrymr.dto.response.PaginationResponse;
import net.thrymr.enums.Alerts;
import net.thrymr.model.AppUser;
import net.thrymr.model.ShiftTimings;
import net.thrymr.model.Site;
import net.thrymr.model.Team;
import net.thrymr.services.SiteTeamAndShiftTimingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SiteTeamAndShiftTimingsController {
    @Autowired
    SiteTeamAndShiftTimingsService siteTeamAndShiftTimingsService;


    @MutationMapping(name="createTeam")
    public Team createTeam(@Argument(name = "input") TeamDto teamDto){
        return siteTeamAndShiftTimingsService.createTeam(teamDto);
    }
    @MutationMapping(name="updateTeam")
    public Team updateTeam(@Argument(name = "input")TeamDto teamDto){
        return siteTeamAndShiftTimingsService.updateTeam(teamDto);
    }

    @MutationMapping(name="deleteTeamById")
    public String deleteTeamById(@Argument Long id){
        return siteTeamAndShiftTimingsService.deleteTeamById(id);
    }

    @MutationMapping(name="saveSite")
    public String saveSite(@Argument(name = "input")SiteDto siteDto){
        return siteTeamAndShiftTimingsService.saveSite(siteDto);
    }
    @MutationMapping(name="updateSite")
    public String updateSite(@Argument(name = "input")SiteDto siteDto){
        return siteTeamAndShiftTimingsService.updateSite(siteDto);
    }

    @QueryMapping(name = "getSiteById")
    public Site getSiteById(@Argument Long id){
        return siteTeamAndShiftTimingsService.getSiteById(id);
    }
    @MutationMapping(name="deleteSiteById")
    public String deleteSiteById(@Argument Long id){
        return siteTeamAndShiftTimingsService.deleteSiteById(id);
    }

    @MutationMapping(name="saveSiftTimings")
    public String saveSiftTimings(@Argument(name = "input")ShiftTimingsDto shiftTimingsDto){
        return siteTeamAndShiftTimingsService.saveShiftTimings(shiftTimingsDto);
    }

    @MutationMapping(name="updateSiftTimings")
    public String updateSiftTimings(@Argument(name = "input")ShiftTimingsDto shiftTimingsDto){
        return siteTeamAndShiftTimingsService.updateSiftTimings(shiftTimingsDto);
    }
    @MutationMapping(name="deleteSiftTimingsById")
    public String deleteSiftTimingsById(@Argument Long id){
        return siteTeamAndShiftTimingsService.deleteSiftTimingsById(id);
    }

    @QueryMapping("getAllTeamPagination")
    public PaginationResponse getAllTeamPagination(@Argument(name = "input")TeamDto teamdto){
        return siteTeamAndShiftTimingsService.getAllTeamPagination(teamdto);
    }
    @QueryMapping("getAllSitePagination")
    public PaginationResponse getAllSitePagination(@Argument(name = "input") SiteDto siteDto){
        return siteTeamAndShiftTimingsService.getAllSitePagination(siteDto);
    }


    @QueryMapping(name = "getAllShiftTimings")
    public List<ShiftTimings> getAllShiftTimings(){
        return siteTeamAndShiftTimingsService.getAllShiftTimings();
    }

    @QueryMapping(name="getAllAppUserByRoles")
    public List<AppUser> getAllAppUserByRoles(@Argument(name = "input") AppUserDto request)  {
        return siteTeamAndShiftTimingsService.getAllAppUserByRoles(request);
    }

    @QueryMapping(name="previewAlertNotification")
    public RoleWiseCountResponse previewAlertNotification(@Argument(name = "input") TeamMembersDto request)  {
        return siteTeamAndShiftTimingsService.previewAlertNotification(request);
    }

    @QueryMapping("getTeamById")
    public Team getTeamById(@Argument Long id) {
        return siteTeamAndShiftTimingsService.getTeamById(id);
    }
    @QueryMapping("getAllEnumAlerts")
    public List<Alerts> getAllEnumAlerts(){
        return siteTeamAndShiftTimingsService.getAllEnumAlerts();
    }
}
