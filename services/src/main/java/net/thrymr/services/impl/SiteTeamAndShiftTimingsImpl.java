package net.thrymr.services.impl;

import net.thrymr.dto.*;
import net.thrymr.enums.Roles;
import net.thrymr.enums.SlotShift;
import net.thrymr.model.*;
import net.thrymr.model.master.MtCity;
import net.thrymr.model.master.MtCountry;
import net.thrymr.model.master.MtRegion;
import net.thrymr.repository.*;
import net.thrymr.services.SiteTeamAndShiftTimingsService;
import net.thrymr.utils.Validator;
import org.apache.poi.sl.draw.geom.GuideIf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
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
    VendorRepo vendorRepo;

    @Override
    public String createTeam(TeamDto teamDto) {
        Team team = new Team();
        team.setTeamId(teamDto.getTeamId());
        team.setTeamName(teamDto.getTeamName());
        //Site
        if (teamDto.getSiteId() != null && siteRepo.existsById(teamDto.getSiteId())) {
            Optional<Site> optionalSite = siteRepo.findById(teamDto.getSiteId());
            optionalSite.ifPresent(team::setSite);
        }
        if (teamDto.getShiftTimingsId() != null && shiftTimingsRepo.existsById(teamDto.getShiftTimingsId())) {
            Optional<ShiftTimings> optionalShiftTimings = shiftTimingsRepo.findById(teamDto.getShiftTimingsId());
            optionalShiftTimings.ifPresent(team::setShiftTimings);
        }
        if (teamDto.getStatus() != null && teamDto.getStatus().equals(Boolean.TRUE)) {
            team.setIsActive(teamDto.getStatus());
        }
        team.setSearchKey(getTeamSearchKey(team));
        teamRepo.save(team);
        return "Team save successfully";
    }


    @Override
    public String updateTeam(TeamDto teamDto) {
        Optional<Team> teamId = teamRepo.findById(teamDto.getId());
        Team team;
        if (teamId.isPresent()) {
            team = teamId.get();
            if (Validator.isValid(teamDto.getTeamName())) {
                team.setTeamName(teamDto.getTeamName());
            }
            //Site
            if (teamDto.getSiteId() != null && siteRepo.existsById(teamDto.getSiteId())) {
                Optional<Site> optionalSite = siteRepo.findById(teamDto.getSiteId());
                optionalSite.ifPresent(team::setSite);
            }
            if (teamDto.getShiftTimingsId() != null && shiftTimingsRepo.existsById(teamDto.getShiftTimingsId())) {
                Optional<ShiftTimings> optionalShiftTimings = shiftTimingsRepo.findById(teamDto.getShiftTimingsId());
                optionalShiftTimings.ifPresent(team::setShiftTimings);
            }
            if (teamDto.getStatus() != null && teamDto.getStatus().equals(Boolean.FALSE) || teamDto.getStatus().equals(Boolean.TRUE)) {
                team.setIsActive(teamDto.getStatus());
            }
            //team.setSearchKey(getTeamSearchKey(team));
            teamRepo.save(team);
            return "Team update successfully";
        }
        return "this id not in database";
    }

    @Override
    public List<Team> getAllTeam() {
        List<Team> teamList = teamRepo.findAll();
        if (!teamList.isEmpty()) {
            return teamList.stream().filter(obj -> obj.getIsActive().equals(Boolean.TRUE)).collect(Collectors.toList());
        }
        return new ArrayList<>();
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
        //vendor
        if(siteDto.getVendorId() != null && vendorRepo.existsById(siteDto.getVendorId())) {
            Optional<Vendor> optionalVendor=vendorRepo.findById(siteDto.getVendorId());
            optionalVendor.ifPresent(site::setVendor);
        }
        //siteManager
        siteDto.setSearchKey(saveSiteSearchKey(site));
        if (siteDto.getStatus() != null && siteDto.getStatus().equals(Boolean.TRUE) && siteDto.getStatus().equals(Boolean.FALSE)) {
            site.setIsActive(siteDto.getStatus());
        }
        siteRepo.save(site);
        return "site save successfully";
    }

    @Override
    public String updateSite(SiteDto siteDto) {
        if (!Validator.isValid(siteDto.getId())) {
            return "id required";
        }
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

            siteRepo.save(site);
            return "site update successfully";
        }

        return "this id not in database";
    }

    @Override
    public List<Site> getAllSite() {
        List<Site> siteList = siteRepo.findAll();
        if (!siteList.isEmpty()) {
            return siteList.stream().filter(obj -> obj.getIsActive().equals(Boolean.TRUE)).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    @Override
    public List<Site> getAllSitePagination(SiteDto siteDto) {
        Pageable pageable = null;
        if (siteDto.getPageSize() != null) {
            pageable = PageRequest.of(siteDto.getPageNumber(), siteDto.getPageSize());
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
            if (siteDto.getCityId() != null) {
                Predicate city = criteriaBuilder.and(root.get("city").in(siteDto.getCityId()));
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
            return criteriaBuilder.and(addSitePredicate.toArray(new Predicate[0]));
        });
        Page<Site> siteObjective = siteRepo.findAll(siteSpecification, pageable);
        System.out.println(siteObjective.getSize());
        if (siteObjective.getContent() != null) {
            return siteObjective.stream().collect(Collectors.toList());
        }
        return new ArrayList<>();
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
        shiftTimings.setShiftStatAt(shiftTimingsDto.getShiftStatAt());
        shiftTimings.setShiftEndAt(shiftTimingsDto.getShiftEndAt());
        if (shiftTimingsDto.getSiteId() != null && siteRepo.existsById(shiftTimingsDto.getSiteId())) {
            Optional<Site> optionalSite = siteRepo.findById(shiftTimingsDto.getSiteId());
            optionalSite.ifPresent(shiftTimings::setSite);
        }
        if (Validator.isValid(shiftTimingsDto.getCounsellorId())) {
            Optional<Counsellor> optionalCounsellorId = counsellorRepo.findById(shiftTimingsDto.getCounsellorId());
            if (optionalCounsellorId.isPresent()) {
                shiftTimings.setCounsellors(optionalCounsellorId.get());
            }
        }
        if (shiftTimingsDto.getStatus() != null && shiftTimingsDto.getStatus().equals(Boolean.TRUE)) {
            shiftTimings.setIsActive(shiftTimingsDto.getStatus());
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
                    shiftTimings.setShiftStatAt(shiftTimingsDto.getShiftStatAt());
                }
                if (shiftTimingsDto.getShiftEndAt() != null) {
                    shiftTimings.setShiftEndAt(shiftTimingsDto.getShiftEndAt());
                }
                if (Validator.isValid(String.valueOf(shiftTimingsDto.getStatus()))) {
                    shiftTimings.setIsActive(shiftTimingsDto.getStatus());
                }
                if (shiftTimingsDto.getStatus() != null && shiftTimingsDto.getStatus().equals(Boolean.TRUE) || shiftTimingsDto.getStatus().equals(Boolean.FALSE)) {
                    shiftTimings.setIsActive(shiftTimingsDto.getStatus());
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
    public List<Team> getAllTeamPagination(TeamDto teamDto) {

        Pageable pageable = null;
        if (teamDto.getPageSize() != null) {
            pageable = PageRequest.of(teamDto.getPageNumber(), teamDto.getPageSize());
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
            if (teamDto.getStatus() != null && !teamDto.getStatus().equals(Boolean.TRUE)) {
                Predicate isActive = criteriaBuilder.and(root.get("isActive").in(teamDto.getStatus()));
                addTeamSpecification.add(isActive);
            } else if (teamDto.getStatus() != null && !teamDto.getStatus().equals(Boolean.FALSE)) {
                Predicate isActive = criteriaBuilder.and(root.get("isActive").in(teamDto.getStatus()));
                addTeamSpecification.add(isActive);
            }
            if (Validator.isValid(teamDto.getSiteId())) {
                Predicate shiftTimings = criteriaBuilder.and(root.get("site").in(teamDto.getSiteId()));
                addTeamSpecification.add(shiftTimings);
            }
            if (Validator.isValid(teamDto.getShiftTimingsId())) {
                Predicate shiftTimings = criteriaBuilder.and(root.get("shiftTimings").in(teamDto.getShiftTimingsId()));
                addTeamSpecification.add(shiftTimings);
            }
            return criteriaBuilder.and(addTeamSpecification.toArray(new Predicate[0]));
        });

        Page<Team> teamObjective = teamRepo.findAll(teamSpecification, pageable);
        if (teamObjective.getContent() != null) {
            return teamObjective.stream().collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    public String getTeamSearchKey(Team team) {
        String searchKey = "";
        if (team.getSite() != null) {
            searchKey = searchKey + " " + team.getSite();
        }
        if (team.getTeamId() != null) {
            searchKey = searchKey + " " + team.getTeamId();
        }
        if (team.getTeamName() != null) {
            searchKey = searchKey + " " + team.getTeamName();
        }
        if (team.getShiftTimings() != null) {
            searchKey = searchKey + " " + team.getShiftTimings();
        }
        return searchKey;
    }

    public String saveSiteSearchKey(Site site) {
        String searchKey = "";
        if (site.getRegion() != null) {
            searchKey = searchKey + " " + site.getRegion();
        }
        if (site.getSiteId() != null) {
            searchKey = searchKey + " " + site.getSiteId();
        }
        if (site.getSiteName() != null) {
            searchKey = searchKey + " " + site.getSiteName();
        }
        if (site.getCity() != null) {
            searchKey = searchKey + " " + site.getCity();
        }

        return searchKey;
    }

    @Override
    public List<AppUser> getAllAppUserByAlerts(AppUserDto request) {
        List<Roles> appUserList = Arrays.asList(Roles.TEAM_LEADER, Roles.TEAM_MANAGER);
        if (Validator.isValid(request.getAlerts().name())) {
            return appUserRepo.findAllByAlertsAndRolesIn(request.getAlerts(), appUserList);
        }

        return new ArrayList<>();
    }
}

