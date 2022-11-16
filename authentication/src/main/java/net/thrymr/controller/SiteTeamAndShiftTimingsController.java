package net.thrymr.controller;

import net.thrymr.dto.ShiftTimingsDto;
import net.thrymr.dto.SiteDto;
import net.thrymr.dto.TeamDto;
import net.thrymr.model.master.MtTeam;
import net.thrymr.model.master.MtSite;
import net.thrymr.model.master.MtShiftTimings;
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


    @MutationMapping(name = "createTeam")
    public String createTeam(@Argument(name = "input") TeamDto teamDto) {
        return siteTeamAndShiftTimingsService.createTeam(teamDto);
    }

    @MutationMapping(name = "updateTeam")
    public String updateTeam(@Argument(name = "input") TeamDto teamDto) {
        return siteTeamAndShiftTimingsService.updateTeam(teamDto);
    }

    @MutationMapping(name = "deleteTeamById")
    public String deleteTeamById(@Argument Long id) {
        return siteTeamAndShiftTimingsService.deleteTeamById(id);
    }

    @MutationMapping(name = "saveSite")
    public String saveSite(@Argument(name = "input") SiteDto siteDto) {
        return siteTeamAndShiftTimingsService.saveSite(siteDto);
    }

    @MutationMapping(name = "updateSite")
    public String updateSite(@Argument(name = "input") SiteDto siteDto) {
        return siteTeamAndShiftTimingsService.updateSite(siteDto);
    }

    @MutationMapping(name = "deleteSiteById")
    public String deleteSiteById(Long id) {
        return siteTeamAndShiftTimingsService.deleteSiteById(id);
    }

    @MutationMapping(name = "saveSiftTimings")
    public String saveSiftTimings(@Argument(name = "input") ShiftTimingsDto shiftTimingsDto) {
        return siteTeamAndShiftTimingsService.saveSiftTimings(shiftTimingsDto);
    }

    @MutationMapping(name = "updateSiftTimings")
    public String updateSiftTimings(@Argument(name = "input") ShiftTimingsDto shiftTimingsDto) {
        return siteTeamAndShiftTimingsService.updateSiftTimings(shiftTimingsDto);
    }

    @MutationMapping(name = "deleteSiftTimingsById")
    public String deleteSiftTimingsById(@Argument Long id) {
        return siteTeamAndShiftTimingsService.deleteSiftTimingsById(id);
    }

    @QueryMapping("getAllTeamPagination")
    public List<MtTeam> getAllTeamPagination(@Argument(name = "input") TeamDto teamdto) {
        return siteTeamAndShiftTimingsService.getAllTeamPagination(teamdto);
    }

    @QueryMapping("getAllSitePagination")
    public List<MtSite> getAllSitePagination(@Argument(name = "input") SiteDto siteDto) {
        return siteTeamAndShiftTimingsService.getAllSitePagination(siteDto);
    }

    @QueryMapping(name = "getAllTeam")
    public List<MtTeam> getAllTeam() {
        List<MtTeam> mtTeamList = siteTeamAndShiftTimingsService.getAllTeam();
        return mtTeamList;
    }

    @QueryMapping(name = "getAllSite")
    public List<MtSite> getAllSite() {
        return siteTeamAndShiftTimingsService.getAllSite();
    }

    @QueryMapping(name = "getAllShiftTimings")
    public List<MtShiftTimings> getAllShiftTimings() {
        return siteTeamAndShiftTimingsService.getAllShiftTimings();
    }

}
