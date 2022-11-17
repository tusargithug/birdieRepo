package net.thrymr.services;

import net.thrymr.dto.AppUserDto;
import net.thrymr.dto.ShiftTimingsDto;
import net.thrymr.dto.SiteDto;
import net.thrymr.dto.TeamDto;
import net.thrymr.model.AppUser;
import net.thrymr.model.ShiftTimings;
import net.thrymr.model.Site;
import net.thrymr.model.Team;

import java.util.List;

public interface SiteTeamAndShiftTimingsService {
    String createTeam(TeamDto teamDto);

    String updateTeam(TeamDto teamDto);

    List<Team> getAllTeam();

    String deleteTeamById(Long id);

    String saveSite(SiteDto siteDto);

    String updateSite(SiteDto siteDto);

    List<Site> getAllSite();

    String deleteSiteById(Long id);

    String saveSiftTimings(ShiftTimingsDto shiftTimingsDto);

    String updateSiftTimings(ShiftTimingsDto shiftTimingsDto);

    String deleteSiftTimingsById(Long id);

    List<Team> getAllTeamPagination(TeamDto teamdto);

    List<Site> getAllSitePagination(SiteDto siteDto);

    List<ShiftTimings> getAllShiftTimings();

    List<AppUser> getAllAppUserByAlerts(AppUserDto request);
}
