package net.thrymr.services;

import net.thrymr.dto.AppUserDto;
import net.thrymr.dto.ShiftTimingsDto;
import net.thrymr.dto.SiteDto;
import net.thrymr.dto.TeamDto;
import net.thrymr.model.master.MtShiftTimings;
import net.thrymr.model.master.MtSite;
import net.thrymr.model.master.MtTeam;
import net.thrymr.model.AppUser;

import java.util.List;

public interface SiteTeamAndShiftTimingsService {
    String createTeam(TeamDto teamDto);

    String updateTeam(TeamDto teamDto);

    List<MtTeam> getAllTeam();

    String deleteTeamById(Long id);

    String saveSite(SiteDto siteDto);

    String updateSite(SiteDto siteDto);

    List<MtSite> getAllSite();

    String deleteSiteById(Long id);

    String saveSiftTimings(ShiftTimingsDto shiftTimingsDto);

    String updateSiftTimings(ShiftTimingsDto shiftTimingsDto);

    String deleteSiftTimingsById(Long id);

    List<MtTeam> getAllTeamPagination(TeamDto teamdto);

    List<MtSite> getAllSitePagination(SiteDto siteDto);

    List<MtShiftTimings> getAllShiftTimings();

    List<AppUser> getAllAppUserByAlerts(AppUserDto request);
}
