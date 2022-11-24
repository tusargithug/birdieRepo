package net.thrymr.services;

import net.thrymr.dto.AppUserDto;
import net.thrymr.dto.ShiftTimingsDto;
import net.thrymr.dto.SiteDto;
import net.thrymr.dto.TeamDto;
import net.thrymr.model.AppUser;
import net.thrymr.model.ShiftTimings;
import net.thrymr.model.Site;
import net.thrymr.model.Team;
import org.springframework.data.domain.Page;

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

    String saveShiftTimings(ShiftTimingsDto shiftTimingsDto);

    String updateSiftTimings(ShiftTimingsDto shiftTimingsDto);

    String deleteSiftTimingsById(Long id);

    Page<Team> getAllTeamPagination(TeamDto teamdto);

    Page<Site> getAllSitePagination(SiteDto siteDto);

    List<ShiftTimings> getAllShiftTimings();

    List<AppUser> getAllAppUserByAlerts(AppUserDto request);
}
