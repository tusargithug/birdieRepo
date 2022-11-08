package net.thrymr.controller;

import net.thrymr.dto.ShiftTimingsDto;
import net.thrymr.dto.SiteDto;
import net.thrymr.dto.TeamDto;
import net.thrymr.model.ShiftTimings;
import net.thrymr.model.Site;
import net.thrymr.model.Team;
import net.thrymr.services.SiteTeamAndShiftTimingsService;
import net.thrymr.utils.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SiteTeamAndShiftTimingsController {
    @Autowired
    SiteTeamAndShiftTimingsService siteTeamAndShiftTimingsService;


    @MutationMapping(name="createTeam")
    public String createTeam(@Argument(name = "input") TeamDto teamDto){
        return siteTeamAndShiftTimingsService.createTeam(teamDto);
    }
    @MutationMapping(name="updateTeam")
    public String updateTeam(@Argument(name = "input")TeamDto teamDto){
        return siteTeamAndShiftTimingsService.updateTeam(teamDto);
    }

    @MutationMapping(name="deleteTeamById")
    public String deleteTeamById(Long id){
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
    @MutationMapping(name="deleteSiteById")
    public String deleteSiteById(@Argument Long id){
        return siteTeamAndShiftTimingsService.deleteSiteById(id);
    }

    @MutationMapping(name="saveSiftTimings")
    public String saveSiftTimings(@Argument(name = "input")ShiftTimingsDto shiftTimingsDto){
        return siteTeamAndShiftTimingsService.saveSiftTimings(shiftTimingsDto);
    }

    @MutationMapping(name="updateSiftTimings")
    public String updateSiftTimings(@Argument(name = "input")ShiftTimingsDto shiftTimingsDto){
        return siteTeamAndShiftTimingsService.updateSiftTimings(shiftTimingsDto);
    }
    @MutationMapping(name="deleteSiftTimingsById")
    public String deleteSiftTimingsById(Long id){
        return siteTeamAndShiftTimingsService.deleteSiftTimingsById(id);
    }

    @QueryMapping("getAllTeamPagination")
    public List<Team> getAllTeamPagination(@Argument(name = "input")TeamDto teamdto){
        return siteTeamAndShiftTimingsService.getAllTeamPagination(teamdto);
    }
    @QueryMapping("getAllSitePagination")
    public List<Site> getAllSitePagination(@Argument(name = "input") SiteDto siteDto){
        return siteTeamAndShiftTimingsService.getAllSitePagination(siteDto);
    }

    @QueryMapping(name = "getAllTeam")
    public List<Team> getAllTeam(){
        List<Team> teamList= siteTeamAndShiftTimingsService.getAllTeam();
        return teamList;
    }
    @QueryMapping(name = "getAllSite")
    public List<Site> getAllSite(){
        return siteTeamAndShiftTimingsService.getAllSite();
    }

    @QueryMapping(name = "getAllShiftTimings")
    public List<ShiftTimings> getAllShiftTimings(){
        return siteTeamAndShiftTimingsService.getAllShiftTimings();
    }

}
