package net.thrymr.services;

import net.thrymr.dto.ShiftTimingsDto;
import net.thrymr.dto.SiteDto;
import net.thrymr.dto.TeamDto;
import net.thrymr.model.Site;
import net.thrymr.model.Team;
import net.thrymr.utils.ApiResponse;

import java.util.List;

public interface SiteTeamAndShiftTimingsService {
    String createTeam(TeamDto teamDto);

    String updateTeam(Long id,TeamDto teamDto);

    List<Team> getAllTeam();

    String deleteTeamById(Long id);

    String saveSite(SiteDto siteDto);

    String updateSite(Long id, SiteDto siteDto);

    List<Site> getAllSite();

    String deleteSiteById(Long id);

    String saveSiftTimings(ShiftTimingsDto shiftTimingsDto);

    String updateSiftTimings(Long id,ShiftTimingsDto shiftTimingsDto);

    String deleteSiftTimingsById(Long id);

    List<Team> getAllTeamPagination(TeamDto teamdto);

    List<Site> getAllSitePagination(SiteDto siteDto);

    List<Site> getAllShiftTimings();
}
