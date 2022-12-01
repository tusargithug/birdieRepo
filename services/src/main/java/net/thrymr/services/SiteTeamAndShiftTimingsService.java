package net.thrymr.services;

import net.thrymr.dto.*;
import net.thrymr.dto.response.PaginationResponse;
import net.thrymr.model.*;

import java.util.List;

public interface SiteTeamAndShiftTimingsService {
    String createTeam(TeamDto teamDto);

    String updateTeam(TeamDto teamDto);

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

    List<AppUser> getAllAppUserByAlerts(AppUserDto request);

    List<AppUser> getAllAppUserByRoles(AppUserDto request);

    RoleWiseCountResponse previewAlertNotification(AppUserDto request);

    Team getTeamById(Long id);
}
