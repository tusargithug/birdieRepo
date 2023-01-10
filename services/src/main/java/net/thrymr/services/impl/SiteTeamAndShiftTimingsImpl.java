package net.thrymr.services.impl;

import net.thrymr.constant.Constants;
import net.thrymr.dto.*;
import net.thrymr.dto.response.PaginationResponse;
import net.thrymr.enums.Alerts;
import net.thrymr.enums.Roles;
import net.thrymr.enums.SlotShift;
import net.thrymr.model.*;
import net.thrymr.model.master.MtCity;
import net.thrymr.model.master.MtCountry;
import net.thrymr.model.master.MtRegion;
import net.thrymr.repository.*;
import net.thrymr.services.SiteTeamAndShiftTimingsService;
import net.thrymr.utils.DateUtils;
import net.thrymr.utils.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SiteTeamAndShiftTimingsImpl implements SiteTeamAndShiftTimingsService {
    @Autowired
    TeamRepo teamRepo;
    @Autowired
    CountryRepo countryRepo;
    @Autowired
    CityRepo cityRepo;
    @Autowired
    RegionRepo regionRepo;
    @Autowired
    AppUserRepo appUserRepo;
    @Autowired
    SiteRepo siteRepo;
    @Autowired
    ShiftTimingsRepo shiftTimingsRepo;
    @Autowired
    CounsellorRepo counsellorRepo;
    @Autowired
    TeamMembersRepo teamMembersRepo;

    @Override
    public Team createTeam(TeamDto teamDto) {
        Team team = new Team();
        team.setTeamId(teamDto.getTeamId());
        team.setTeamName(teamDto.getTeamName());
        if (teamDto.getShiftStartAt() != null) {
            team.setShiftStartAt(DateUtils.toStringToLocalTime(teamDto.getShiftStartAt(), Constants.TIME_FORMAT_12_HOURS));
        }
        if (teamDto.getShiftEndAt() != null) {
            team.setShiftEndAt(DateUtils.toStringToLocalTime(teamDto.getShiftEndAt(), Constants.TIME_FORMAT_12_HOURS));
        }
        if (teamDto.getShiftStartAt() != null && teamDto.getShiftEndAt() != null) {
            team.setShiftTimings(teamDto.getShiftStartAt() + " - " + teamDto.getShiftEndAt());
        }
        //Site
        if (teamDto.getSiteId() != null && siteRepo.existsById(teamDto.getSiteId())) {
            Optional<Site> optionalSite = siteRepo.findById(teamDto.getSiteId());
            optionalSite.ifPresent(team::setSite);
        }
        if (teamDto.getStatus() != null && teamDto.getStatus().equals(Boolean.TRUE)) {
            team.setIsActive(teamDto.getStatus());
        } else {
            team.setIsActive(Boolean.FALSE);
        }
        team.setSearchKey(getTeamSearchKey(team));
        teamRepo.save(team);
        return team;
    }

    @Override
    public String updateTeams(List<TeamDto> teamDtoList) {
        if (teamDtoList != null && teamDtoList.size() > 0) {
            for (TeamDto teamDto : teamDtoList) {
                Team team = null;
                if (Validator.isValid(teamDto.getId())) {
                    Optional<Team> teamId = teamRepo.findById(teamDto.getId());
                    if (teamId.isPresent()) {
                        team = teamId.get();

                        if (teamDto.getShiftStartAt() != null) {
                            team.setShiftStartAt(DateUtils.toStringToLocalTime(teamDto.getShiftStartAt(), Constants.TIME_FORMAT_12_HOURS));
                        }
                        if (teamDto.getShiftEndAt() != null) {
                            team.setShiftEndAt(DateUtils.toStringToLocalTime(teamDto.getShiftEndAt(), Constants.TIME_FORMAT_12_HOURS));
                        }
                        if (teamDto.getShiftStartAt() != null && teamDto.getShiftEndAt() != null) {
                            team.setShiftTimings(teamDto.getShiftStartAt() + " - " + teamDto.getShiftEndAt());
                        }
                        team.setSearchKey(getTeamSearchKey(team));
                        teamRepo.save(team);
                    }
                }
            }
            return "Teams updated successfully";
        }
        return "Data not found";
    }


    @Override
    public Team updateTeam(TeamDto teamDto) {
        Team team = null;
        if (Validator.isValid(teamDto.getId())) {
            Optional<Team> teamId = teamRepo.findById(teamDto.getId());
            if (teamId.isPresent()) {
                team = teamId.get();
                if (Validator.isValid(teamDto.getTeamId())) {
                    team.setTeamId(teamDto.getTeamId());
                }
                if (Validator.isValid(teamDto.getTeamName())) {
                    team.setTeamName(teamDto.getTeamName());
                }
                //Site
                if (teamDto.getSiteId() != null && siteRepo.existsById(teamDto.getSiteId())) {
                    Optional<Site> optionalSite = siteRepo.findById(teamDto.getSiteId());
                    optionalSite.ifPresent(team::setSite);
                }
                if (teamDto.getStatus() != null && teamDto.getStatus().equals(Boolean.FALSE) || teamDto.getStatus().equals(Boolean.TRUE)) {
                    team.setIsActive(teamDto.getStatus());
                }
                if (teamDto.getShiftStartAt() != null) {
                    team.setShiftStartAt(DateUtils.toStringToLocalTime(teamDto.getShiftStartAt(), Constants.TIME_FORMAT_12_HOURS));
                }
                if (teamDto.getShiftEndAt() != null) {
                    team.setShiftEndAt(DateUtils.toStringToLocalTime(teamDto.getShiftEndAt(), Constants.TIME_FORMAT_12_HOURS));
                }
                if (teamDto.getShiftStartAt() != null && teamDto.getShiftEndAt() != null) {
                    team.setShiftTimings(teamDto.getShiftStartAt() + " - " + teamDto.getShiftEndAt());
                }
                team.setSearchKey(getTeamSearchKey(team));
                teamRepo.save(team);
                return team;
            }
        }
        return new Team();
    }


    @Override
    public String deleteTeamById(Long id) {
        if (Validator.isValid(id)) {
            Optional<Team> teamId = teamRepo.findById(id);
            Team team;
            if (teamId.isPresent()) {
                team = teamId.get();
                team.setIsActive(Boolean.FALSE);
                team.setIsDeleted(Boolean.TRUE);
                teamRepo.save(team);
            }
            return "delete records successfully";
        }
        return "this id not present in database";
    }

    @Override
    public String saveSite(SiteDto siteDto) {
        Site site = new Site();
        site.setSiteId(siteDto.getSiteId());
        site.setSiteName(siteDto.getSiteName());
        //Region
        if (siteDto.getRegionId() != null && regionRepo.existsById(siteDto.getRegionId())) {
            Optional<MtRegion> optionalRegion = regionRepo.findById(siteDto.getRegionId());
            optionalRegion.ifPresent(site::setRegion);
        }
        //Country
        if (siteDto.getCountryId() != null && countryRepo.existsById(siteDto.getCountryId())) {
            Optional<MtCountry> optionalCountry = countryRepo.findById(siteDto.getCountryId());
            optionalCountry.ifPresent(site::setCountry);
        }
        //City
        if (siteDto.getCityId() != null && cityRepo.existsById(siteDto.getCityId())) {
            Optional<MtCity> optionalCity = cityRepo.findById(siteDto.getCityId());
            optionalCity.ifPresent(site::setCity);
        }
        //siteManager

        if (siteDto.getStatus() != null) {
            site.setIsActive(siteDto.getStatus());
        } else {
            site.setIsActive(Boolean.TRUE);
        }
        site.setSearchKey(getSiteSearchKey(site));
        siteRepo.save(site);
        return "site save successfully";
    }

    @Override
    public String updateSite(SiteDto siteDto) {
        if (Validator.isValid(siteDto.getId())) {
            Optional<Site> optionalSite = siteRepo.findById(siteDto.getId());
            Site site;
            if (optionalSite.isPresent()) {
                site = optionalSite.get();
                if (Validator.isValid(siteDto.getSiteId())) {
                    site.setSiteId(siteDto.getSiteId());
                }
                if (Validator.isValid(siteDto.getSiteName())) {
                    site.setSiteName(siteDto.getSiteName());
                }
                //Region
                if (siteDto.getRegionId() != null && regionRepo.existsById(siteDto.getRegionId())) {
                    Optional<MtRegion> optionalRegion = regionRepo.findById(siteDto.getRegionId());
                    optionalRegion.ifPresent(site::setRegion);
                }
                //Country
                if (siteDto.getCountryId() != null && countryRepo.existsById(siteDto.getCountryId())) {
                    Optional<MtCountry> optionalCountry = countryRepo.findById(siteDto.getCountryId());
                    optionalCountry.ifPresent(site::setCountry);
                }
                //City
                if (siteDto.getCityId() != null && cityRepo.existsById(siteDto.getCityId())) {
                    Optional<MtCity> optionalCity = cityRepo.findById(siteDto.getCityId());
                    optionalCity.ifPresent(site::setCity);
                }
                if (siteDto.getStatus().equals(Boolean.TRUE) || siteDto.getStatus().equals(Boolean.FALSE)) {
                    site.setIsActive(siteDto.getStatus());
                }
                site.setSearchKey(getSiteSearchKey(site));
                siteRepo.save(site);
                return "site update successfully";
            }
        }
        return "this id not in database";
    }

    @Override
    public PaginationResponse getAllSitePagination(SiteDto siteDto) {
        Pageable pageable = null;
        if (siteDto.getPageSize() != null && siteDto.getPageNumber() != null) {
            pageable = PageRequest.of(siteDto.getPageNumber(), siteDto.getPageSize());
        }
        if (siteDto.getPageSize() != null && siteDto.getPageNumber() != null) {
            pageable = PageRequest.of(siteDto.getPageNumber(), siteDto.getPageSize(), Sort.Direction.DESC, "createdOn");
        }
        if (siteDto.getSortSiteName() != null && siteDto.getSortSiteName().equals(Boolean.TRUE)) {
            pageable = PageRequest.of(siteDto.getPageNumber(), siteDto.getPageSize(), Sort.Direction.ASC, "siteName");
        } else if (siteDto.getSortSiteName() != null && siteDto.getSortSiteName().equals(Boolean.FALSE)) {
            pageable = PageRequest.of(siteDto.getPageNumber(), siteDto.getPageSize(), Sort.Direction.DESC, "siteName");
        }
        Specification<Site> siteSpecification = ((root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> addSitePredicate = new ArrayList<>();
            if (siteDto.getSiteId() != null) {
                Predicate siteId = criteriaBuilder.and(root.get("siteId").in(siteDto.getSiteId()));
                addSitePredicate.add(siteId);
            }
            if (siteDto.getSiteName() != null && !siteDto.getSiteName().isEmpty()) {
                Predicate siteName = criteriaBuilder.and(root.get("siteName").in(siteDto.getSiteName()));
                addSitePredicate.add(siteName);
            }
            if (siteDto.getStatus() != null && siteDto.getStatus().equals(Boolean.TRUE)) {
                Predicate isActive = criteriaBuilder.and(root.get("isActive").in(siteDto.getStatus()));
                addSitePredicate.add(isActive);
            } else if (siteDto.getStatus() != null && siteDto.getStatus().equals(Boolean.FALSE)) {
                Predicate isActive = criteriaBuilder.and(root.get("isActive").in(siteDto.getStatus()));
                addSitePredicate.add(isActive);
            }
            if (siteDto.getCityIdList() != null) {
                Predicate city = criteriaBuilder.and(root.get("city").in(siteDto.getCityIdList()));
                addSitePredicate.add(city);
            }
            if (siteDto.getCountry() != null) {
                Predicate country = criteriaBuilder.and(root.get("country").in(siteDto.getCountry()));
                addSitePredicate.add(country);
            }
            if (siteDto.getRegion() != null) {
                Predicate region = criteriaBuilder.and(root.get("region").in(siteDto.getRegion()));
                addSitePredicate.add(region);
            }
            Predicate isDeletedPredicate = criteriaBuilder.equal(root.get("isDeleted"), Boolean.FALSE);
            addSitePredicate.add(isDeletedPredicate);

            if (Validator.isValid(siteDto.getSearchKey())) {
                Predicate searchPredicate = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("searchKey")),
                        "%" + siteDto.getSearchKey().toLowerCase() + "%");
                addSitePredicate.add(searchPredicate);
            }
            return criteriaBuilder.and(addSitePredicate.toArray(new Predicate[0]));
        });
        PaginationResponse paginationResponse = new PaginationResponse();
        if (siteDto.getPageSize() != null && siteDto.getPageNumber() != null) {
            Page<Site> siteObjective = siteRepo.findAll(siteSpecification, pageable);
            if (siteObjective.getContent() != null) {
                paginationResponse.setSiteList(siteObjective.getContent());
                paginationResponse.setTotalPages(siteObjective.getTotalPages());
                paginationResponse.setTotalElements(siteObjective.getTotalElements());
                return paginationResponse;
            }
        } else {
            List<Site> siteList = siteRepo.findAll(siteSpecification);
            paginationResponse.setSiteList(siteList.stream().filter(site -> site.getIsDeleted().equals(Boolean.FALSE) && site.getIsActive().equals(Boolean.TRUE)).collect(Collectors.toList()));
            return paginationResponse;
        }
        return new PaginationResponse();
    }

    @Override
    public List<ShiftTimings> getAllShiftTimings() {
        List<ShiftTimings> shiftTimingsList = shiftTimingsRepo.findAll();
        if (!shiftTimingsList.isEmpty()) {
            return shiftTimingsList.stream().filter(obj -> obj.getIsActive().equals(Boolean.TRUE)).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    @Override
    public String deleteSiteById(Long id) {
        Optional<Site> optionalSite = siteRepo.findById(id);
        Site site;
        if (optionalSite.isPresent()) {
            site = optionalSite.get();
            site.setIsActive(Boolean.FALSE);
            site.setIsDeleted(Boolean.TRUE);
            siteRepo.save(site);
        }
        return "delete record successfully";
    }

    @Override
    public String saveShiftTimings(ShiftTimingsDto shiftTimingsDto) {
        ShiftTimings shiftTimings = new ShiftTimings();
        if (shiftTimingsDto.getShiftName().equals(SlotShift.MORNING)) {
            shiftTimings.setShiftName(SlotShift.MORNING);
        }
        if (shiftTimingsDto.getShiftName().equals(SlotShift.AFTERNOON)) {
            shiftTimings.setShiftName(SlotShift.AFTERNOON);
        }
        if (shiftTimingsDto.getShiftName().equals(SlotShift.EVENING)) {
            shiftTimings.setShiftName(SlotShift.EVENING);
        }
        if (shiftTimingsDto.getShiftStatAt() != null) {
            shiftTimings.setShiftStatAt(DateUtils.toStringToLocalTime(shiftTimingsDto.getShiftStatAt(), Constants.TIME_FORMAT_12_HOURS));
        }
        if (shiftTimingsDto.getShiftEndAt() != null) {
            shiftTimings.setShiftEndAt(DateUtils.toStringToLocalTime(shiftTimingsDto.getShiftEndAt(), Constants.TIME_FORMAT_12_HOURS));
        }
        if (shiftTimingsDto.getShiftStatAt() != null && shiftTimingsDto.getShiftEndAt() != null) {
            shiftTimings.setShiftTimings(shiftTimingsDto.getShiftStatAt() + " - " + shiftTimingsDto.getShiftEndAt());
        }
        if (shiftTimingsDto.getStatus() != null) {
            shiftTimings.setIsActive(shiftTimingsDto.getStatus());
        } else {
            shiftTimings.setIsActive(Boolean.TRUE);
        }
        if (shiftTimingsDto.getSiteId() != null && siteRepo.existsById(shiftTimingsDto.getSiteId())) {
            Optional<Site> optionalSite = siteRepo.findById(shiftTimingsDto.getSiteId());
            optionalSite.ifPresent(shiftTimings::setSite);
        }

        shiftTimingsRepo.save(shiftTimings);
        return "shift timings save successfully";
    }

    @Override
    public String updateSiftTimings(ShiftTimingsDto shiftTimingsDto) {
        if (Validator.isValid(shiftTimingsDto.getId())) {
            Optional<ShiftTimings> shiftTimingsId = shiftTimingsRepo.findById(shiftTimingsDto.getId());
            ShiftTimings shiftTimings;
            if (shiftTimingsId.isPresent()) {
                shiftTimings = shiftTimingsId.get();
                if (Validator.isValid(String.valueOf(shiftTimingsDto.getShiftName()))) {
                    System.out.println(shiftTimingsDto.getShiftName());
                    shiftTimings.setShiftName(shiftTimingsDto.getShiftName());
                }
                if (shiftTimingsDto.getShiftStatAt() != null) {
                    shiftTimings.setShiftStatAt(DateUtils.toStringToLocalTime(shiftTimingsDto.getShiftStatAt(), Constants.TIME_FORMAT_12_HOURS));
                }
                if (shiftTimingsDto.getShiftEndAt() != null) {
                    shiftTimings.setShiftEndAt(DateUtils.toStringToLocalTime(shiftTimingsDto.getShiftEndAt(), Constants.TIME_FORMAT_12_HOURS));
                }
                if (shiftTimingsDto.getShiftStatAt() != null && shiftTimingsDto.getShiftEndAt() != null) {
                    shiftTimings.setShiftTimings(shiftTimingsDto.getShiftStatAt() + " - " + shiftTimingsDto.getShiftEndAt());
                }
                if (Validator.isValid(String.valueOf(shiftTimingsDto.getStatus()))) {
                    shiftTimings.setIsActive(shiftTimingsDto.getStatus());
                } else {
                    shiftTimings.setIsActive(Boolean.TRUE);
                }

                shiftTimingsRepo.save(shiftTimings);
                return "shift timings update successfully";
            }
        }
        return "this id not in database";
    }

    @Override
    public String deleteSiftTimingsById(Long id) {
        Optional<ShiftTimings> shiftTimingsId = shiftTimingsRepo.findById(id);
        ShiftTimings shiftTimings;
        if (shiftTimingsId.isPresent()) {
            shiftTimings = shiftTimingsId.get();
            shiftTimings.setIsActive(Boolean.FALSE);
            shiftTimings.setIsDeleted(Boolean.TRUE);
            shiftTimingsRepo.save(shiftTimings);
            return "delete record successfully";
        }
        return "this id not in database";
    }

    @Override
    public PaginationResponse getAllTeamPagination(TeamDto teamDto) {

        Pageable pageable = null;
        if (teamDto.getPageSize() != null && teamDto.getPageNumber() != null) {
            pageable = PageRequest.of(teamDto.getPageNumber(), teamDto.getPageSize());
        }
        if (teamDto.getPageSize() != null && teamDto.getPageNumber() != null) {
            pageable = PageRequest.of(teamDto.getPageNumber(), teamDto.getPageSize(), Sort.Direction.DESC, "createdOn");
        }
        if (teamDto.getSortTeamName() != null && teamDto.getSortTeamName().equals(Boolean.TRUE)) {
            pageable = PageRequest.of(teamDto.getPageNumber(), teamDto.getPageSize(), Sort.Direction.ASC, "teamName");
        } else if (teamDto.getSortTeamName() != null && teamDto.getSortTeamName().equals(Boolean.FALSE)) {
            pageable = PageRequest.of(teamDto.getPageNumber(), teamDto.getPageSize(), Sort.Direction.DESC, "teamName");
        }
        //filters
        Specification<Team> teamSpecification = ((root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> addTeamSpecification = new ArrayList<>();
            if (teamDto.getTeamId() != null) {
                Predicate teamId = criteriaBuilder.and(root.get("teamId").in(teamDto.getTeamId()));
                addTeamSpecification.add(teamId);
            }
            if (teamDto.getTeamName() != null && !teamDto.getTeamName().isEmpty()) {
                Predicate teamName = criteriaBuilder.and(root.get("teamName").in(teamDto.getTeamName()));
                addTeamSpecification.add(teamName);
            }
            if (teamDto.getShiftTimings() != null && !teamDto.getShiftTimings().isEmpty()) {
                Predicate shiftTimings = criteriaBuilder.and(root.get("shiftTimings").in(teamDto.getShiftTimings()));
                addTeamSpecification.add(shiftTimings);
            }
            if (teamDto.getStatus() != null && !teamDto.getStatus().equals(Boolean.TRUE)) {
                Predicate isActive = criteriaBuilder.and(root.get("isActive").in(teamDto.getStatus()));
                addTeamSpecification.add(isActive);
            } else if (teamDto.getStatus() != null && !teamDto.getStatus().equals(Boolean.FALSE)) {
                Predicate isActive = criteriaBuilder.and(root.get("isActive").in(teamDto.getStatus()));
                addTeamSpecification.add(isActive);
            }
            if (Validator.isValid(teamDto.getSiteIds())) {
                Predicate shiftTimings = criteriaBuilder.and(root.get("site").in(teamDto.getSiteIds()));
                addTeamSpecification.add(shiftTimings);
            }
            if (Validator.isValid(teamDto.getShiftTimingsList())) {
                Predicate shiftTimings = criteriaBuilder.and(root.get("shiftTimings").in(teamDto.getShiftTimingsList()));
                addTeamSpecification.add(shiftTimings);
            }
            Predicate isDeletedPredicate = criteriaBuilder.equal(root.get("isDeleted"), Boolean.FALSE);
            addTeamSpecification.add(isDeletedPredicate);
            if (Validator.isValid(teamDto.getSearchKey())) {
                Predicate searchPredicate = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("searchKey")),
                        "%" + teamDto.getSearchKey().toLowerCase() + "%");
                addTeamSpecification.add(searchPredicate);
            }
            return criteriaBuilder.and(addTeamSpecification.toArray(new Predicate[0]));
        });
        PaginationResponse paginationResponse = new PaginationResponse();
        if (teamDto.getPageSize() != null && teamDto.getPageNumber() != null) {
            Page<Team> teamObjective = teamRepo.findAll(teamSpecification, pageable);
            if (teamObjective.getContent() != null) {
                paginationResponse.setTeamList(teamObjective.getContent());
                paginationResponse.setTotalPages(teamObjective.getTotalPages());
                paginationResponse.setTotalElements(teamObjective.getTotalElements());
                return paginationResponse;
            }
        } else {
            List<Team> teamList = teamRepo.findAll(teamSpecification);
            paginationResponse.setTeamList(teamList.stream().filter(team -> team.getIsDeleted().equals(Boolean.FALSE)).collect(Collectors.toList()));
            return paginationResponse;
        }
        return new PaginationResponse();
    }


    public String getTeamSearchKey(Team team) {
        String searchKey = "";
        if (team.getTeamId() != null) {
            searchKey = searchKey + " " + team.getTeamId();
        }
        if (team.getTeamName() != null) {
            searchKey = searchKey + " " + team.getTeamName();
        }
        if (team.getSite() != null) {
            searchKey = searchKey + " " + team.getSite().getSiteName();
        }
        if (team.getShiftStartAt() != null) {
            searchKey = searchKey + " " + team.getShiftStartAt();
        }
        if (team.getShiftEndAt() != null) {
            searchKey = searchKey + " " + team.getShiftEndAt();
        }
        if (team.getShiftTimings() != null) {
            searchKey = searchKey + " " + team.getShiftTimings();
        }
        if (team.getIsActive() != null) {
            searchKey = searchKey + " " + team.getIsActive();
        }
        return searchKey;
    }

    public String getSiteSearchKey(Site site) {
        String searchKey = "";
        if (site.getSiteId() != null) {
            searchKey = searchKey + " " + site.getSiteId();
        }
        if (site.getSiteName() != null) {
            searchKey = searchKey + " " + site.getSiteName();
        }
        if (site.getCity() != null) {
            searchKey = searchKey + " " + site.getCity().getCityName();
        }
        if (site.getCountry() != null) {
            searchKey = searchKey + " " + site.getCountry().getCountryCode();
        }
        if (site.getRegion() != null) {
            searchKey = searchKey + " " + site.getRegion().getRegionName();
        }
        if (site.getCountry() != null) {
            searchKey = searchKey + " " + site.getCountry().getCountryName();
        }
        if (site.getIsActive() != null) {
            searchKey = searchKey + " " + site.getIsActive();
        }
        return searchKey;
    }


    @Override
    public List<AppUser> getAllAppUserByRoles(AppUserDto request) {
        Specification<AppUser> appUserSpecification = ((root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> addVendorPredicate = new ArrayList<>();
            if (request.getSearchKey() != null && !request.getSearchKey().isEmpty()) {
                Predicate searchPredicate = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("searchKey")),
                        "%" + request.getSearchKey().toLowerCase() + "%");
                addVendorPredicate.add(searchPredicate);
            }
            return criteriaBuilder.and(addVendorPredicate.toArray(new Predicate[0]));
        });
        List<AppUser> appUserList = appUserRepo.findAll(appUserSpecification);
        if (Validator.isValid(request.getRoles()) && Roles.roles(request.getRoles()).equals(Roles.TEAM_LEADER)) {
            return appUserList.stream().filter(appUser -> (appUser.getRoles() != null && appUser.getRoles().equals(Roles.TEAM_LEADER))).collect(Collectors.toList());
        }
        if (Validator.isValid(request.getRoles()) && Roles.roles(request.getRoles()).equals(Roles.TEAM_MANAGER)) {
            return appUserList.stream().filter(appUser -> (appUser.getRoles() != null && appUser.getRoles().equals(Roles.TEAM_MANAGER))).collect(Collectors.toList());
        }
        if (Validator.isValid(request.getRoles()) && Roles.roles(request.getRoles()).equals(Roles.DIRECTOR)) {
            return appUserList.stream().filter(appUser -> (appUser.getRoles() != null && appUser.getRoles().equals(Roles.DIRECTOR))).collect(Collectors.toList());
        }
        if (Validator.isValid(request.getRoles()) && Roles.roles(request.getRoles()).equals(Roles.ACCOUNT_MANAGER)) {
            return appUserList.stream().filter(appUser -> (appUser.getRoles() != null && appUser.getRoles().equals(Roles.ACCOUNT_MANAGER))).collect(Collectors.toList());
        }
        if (Validator.isValid(request.getRoles()) && Roles.roles(request.getRoles()).equals(Roles.GENERAL_MANAGER)) {
            return appUserList.stream().filter(appUser -> (appUser.getRoles() != null && appUser.getRoles().equals(Roles.GENERAL_MANAGER))).collect(Collectors.toList());
        }
        if (Validator.isValid(request.getRoles()) && Roles.roles(request.getRoles()).equals(Roles.SENIOR_MANAGER)) {
            return appUserList.stream().filter(appUser -> (appUser.getRoles() != null && appUser.getRoles().equals(Roles.SENIOR_MANAGER))).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    @Override
    public RoleWiseCountResponse previewAlertNotification(TeamMembersDto request) {
        RoleWiseCountResponse roleWiseCountResponse = new RoleWiseCountResponse();
        roleWiseCountResponse.setTeamLeaderCount(0);
        roleWiseCountResponse.setTeamManagerCount(0);
        roleWiseCountResponse.setDirectorCount(0);
        roleWiseCountResponse.setGeneralManagerCount(0);
        roleWiseCountResponse.setSeniorManagerCount(0);
        roleWiseCountResponse.setAccountManagerCount(0);
        if (Validator.isValid(request.getAppUserIdList())) {
            List<AppUser> appUserList = appUserRepo.findAllById(request.getAppUserIdList());
            for (AppUser appUser : appUserList) {
                if (appUser.getRoles().equals(Roles.TEAM_LEADER)) {
                    roleWiseCountResponse.setTeamLeaderCount(roleWiseCountResponse.getTeamLeaderCount() + 1);
                }
                if (appUser.getRoles().equals(Roles.TEAM_MANAGER)) {
                    roleWiseCountResponse.setTeamManagerCount(roleWiseCountResponse.getTeamManagerCount() + 1);
                }
                if (appUser.getRoles().equals(Roles.DIRECTOR)) {
                    roleWiseCountResponse.setDirectorCount(roleWiseCountResponse.getDirectorCount() + 1);
                }
                if (appUser.getRoles().equals(Roles.ACCOUNT_MANAGER)) {
                    roleWiseCountResponse.setAccountManagerCount(roleWiseCountResponse.getAccountManagerCount() + 1);
                }
                if (appUser.getRoles().equals(Roles.GENERAL_MANAGER)) {
                    roleWiseCountResponse.setGeneralManagerCount(roleWiseCountResponse.getGeneralManagerCount() + 1);
                }
                if (appUser.getRoles().equals(Roles.SENIOR_MANAGER)) {
                    roleWiseCountResponse.setSeniorManagerCount(roleWiseCountResponse.getSeniorManagerCount() + 1);
                }
            }
            return roleWiseCountResponse;
        }
        return new RoleWiseCountResponse();
    }

    @Override
    public Team getTeamById(Long id) {
        Team team;
        if (Validator.isValid(id)) {
            Optional<Team> optionalAppUser = teamRepo.findById(id);
            if (optionalAppUser.isPresent() && optionalAppUser.get().getIsDeleted().equals(Boolean.FALSE)) {
                team = optionalAppUser.get();
                return team;
            }
        }
        return new Team();
    }


    @Override
    public Site getSiteById(Long id) {
        Site site = null;
        if (Validator.isValid(id)) {
            Optional<Site> optionalSite = siteRepo.findById(id);
            if (optionalSite.isPresent() && optionalSite.get().getIsDeleted().equals(Boolean.FALSE)) {
                site = optionalSite.get();
                return site;
            }
        }
        return new Site();
    }

    @Override
    public List<Alerts> getAllEnumAlerts() {
        List<Alerts> alertsList = Arrays.asList(Alerts.NONE, Alerts.RED_ALERT, Alerts.ORANGE_ALERT, Alerts.GREEN_ALERT);
        return alertsList;
    }
}
