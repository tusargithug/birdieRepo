package net.thrymr.services;

import net.thrymr.dto.*;
import net.thrymr.dto.response.PaginationResponse;
import net.thrymr.enums.Alerts;
import net.thrymr.model.*;

import java.util.List;

public interface SiteTeamAndShiftTimingsService {
    Team createTeam(TeamDto teamDto);

    Team updateTeam(TeamDto teamDto);

    String updateTeams(List<TeamDto> teamDtoList);

    String deleteTeamById(Long id);

    String saveSite(SiteDto siteDto);

    String updateSite(SiteDto siteDto);

    String deleteSiteById(Long id);

    String saveShiftTimings(ShiftTimingsDto shiftTimingsDto);

    String updateSiftTimings(ShiftTimingsDto shiftTimingsDto);

    String deleteSiftTimingsById(Long id);

    PaginationResponse getAllTeamPagination(TeamDto teamdto);

    PaginationResponse getAllSitePagination(SiteDto siteDto);

    List<ShiftTimings> getAllShiftTimings();

    List<AppUser> getAllAppUserByRoles(AppUserDto request);

    RoleWiseCountResponse previewAlertNotification(TeamMembersDto request);

    Team getTeamById(Long id);

    Site getSiteById(Long id);

    List<Alerts> getAllEnumAlerts();

    Long getTotalTeamCount();
    Long getTotalSiteCount();
}
