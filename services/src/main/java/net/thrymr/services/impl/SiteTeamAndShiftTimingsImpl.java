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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

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

    @Override
    public String createTeam(TeamDto teamDto) {
        Team team = new Team();
        team.setTeamId(teamDto.getTeamId());
        team.setTeamName(teamDto.getTeamName());
        //Team_leader
        if (teamDto.getTeamLeaderId() != null && appUserRepo.existsById(teamDto.getTeamLeaderId())) {
            Optional<AppUser> optionalAppUser = appUserRepo.findById(teamDto.getTeamLeaderId());
            if (optionalAppUser.get().getRoles().equals(Roles.TEAM_LEADER)) {
                optionalAppUser.ifPresent(team::setTeamLeader);
            }
        }
        //Team_manager
        if (teamDto.getTeamManagerId() != null && appUserRepo.existsById(teamDto.getTeamManagerId())) {
            Optional<AppUser> optionalAppUser = appUserRepo.findById(teamDto.getTeamManagerId());
            if (optionalAppUser.get().getRoles().equals(Roles.TEAM_MANAGER)) {
                optionalAppUser.ifPresent(team::setTeamManager);
            }
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
        //team.setShiftTimings(dtoToShiftTimings(teamDto.getShiftTimings()));
        if (teamDto.getStatus().equals(Boolean.TRUE)) {
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
            if (teamDto.getTeamLeaderId() != null && appUserRepo.existsById(teamDto.getTeamLeaderId())) {
                Optional<AppUser> optionalAppUser = appUserRepo.findById(teamDto.getTeamLeaderId());
                if (optionalAppUser.get().getRoles().equals(Roles.TEAM_LEADER)) {
                    optionalAppUser.ifPresent(team::setTeamLeader);
                }
            }
            //Team_manager
            if (teamDto.getTeamManagerId() != null && appUserRepo.existsById(teamDto.getTeamManagerId())) {
                Optional<AppUser> optionalAppUser = appUserRepo.findById(teamDto.getTeamManagerId());
                if (optionalAppUser.get().getRoles().equals(Roles.TEAM_MANAGER)) {
                    optionalAppUser.ifPresent(team::setTeamManager);
                }
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
            if (teamDto.getStatus().equals(Boolean.FALSE) || teamDto.getStatus().equals(Boolean.TRUE)) {
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
        //siteManager
        if (siteDto.getSiteManagerId() != null && appUserRepo.existsById(siteDto.getSiteManagerId())) {
            Optional<AppUser> optionalAppUser = appUserRepo.findById(siteDto.getSiteManagerId());
            if (optionalAppUser.isPresent() && optionalAppUser.get().getRoles().equals(Roles.SITE_MANAGER)) {
                optionalAppUser.ifPresent(site::setSiteManager);
            }
        }
        siteDto.setSearchKey(saveSiteSearchKey(site));
        if (siteDto.getStatus().equals(Boolean.TRUE)) {
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
            //siteManager
            if (siteDto.getSiteManagerId() != null && appUserRepo.existsById(siteDto.getSiteManagerId())) {
                Optional<AppUser> optionalAppUser = appUserRepo.findById(siteDto.getSiteManagerId());
                optionalAppUser.ifPresent(site::setSiteManager);
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
        List<Site> siteList = siteRepo.findAll();
        Pageable pageable = null;
        if (siteDto.getPageSize() != null) {
            pageable = PageRequest.of(siteDto.getPageNumber(), siteDto.getPageSize());
        }
        if (siteDto.getSiteId() != null) {
            pageable = PageRequest.of(siteDto.getPageNumber(), siteDto.getPageSize(), Sort.Direction.ASC, "siteId");
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
            if (siteDto.getStatus()) {
                Predicate isActive = criteriaBuilder.and(root.get("status").in(siteDto.getStatus()));
                addSitePredicate.add(isActive);
            }
            if (siteDto.getSiteManager() != null) {
                Predicate siteManager = criteriaBuilder.and(root.get("siteManager").in(siteDto.getSiteManager()));
                addSitePredicate.add(siteManager);
            }
            if (siteDto.getCity() != null) {
                Predicate city = criteriaBuilder.and(root.get("city").in(siteDto.getCity()));
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
        if (siteObjective.getContent() != null) {
            return siteObjective.stream().filter(obj -> obj.getIsActive().equals(Boolean.TRUE)).collect(Collectors.toList());
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
    public String saveSiftTimings(ShiftTimingsDto shiftTimingsDto) {
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
        if (Validator.isValid(shiftTimingsDto.getTeamId())) {
            Optional<Team> optionalTeamId = teamRepo.findById(shiftTimingsDto.getTeamId());
            if (optionalTeamId.isPresent()) {
                shiftTimings.setTeam(optionalTeamId.get());
            }
        }
        if (Validator.isValid(shiftTimingsDto.getCounsellorId())) {
            Optional<Counsellor> optionalCounsellorId = counsellorRepo.findById(shiftTimingsDto.getCounsellorId());
            if (optionalCounsellorId.isPresent()) {
                shiftTimings.setCounsellors(optionalCounsellorId.get());
            }
        }
        if (shiftTimingsDto.getStatus().equals(Boolean.TRUE)) {
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
                if (shiftTimingsDto.getShiftStatAt()!=null) {
                    shiftTimings.setShiftStatAt(shiftTimingsDto.getShiftStatAt());
                }
                if (shiftTimingsDto.getShiftEndAt()!=null) {
                    shiftTimings.setShiftEndAt(shiftTimingsDto.getShiftEndAt());
                }
                if (Validator.isValid(String.valueOf(shiftTimingsDto.getStatus()))) {
                    shiftTimings.setIsActive(shiftTimingsDto.getStatus());
                }
                if (shiftTimingsDto.getStatus().equals(Boolean.TRUE) || shiftTimingsDto.getStatus().equals(Boolean.FALSE)) {
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
        if (teamDto.getTeamId() != null) {
            pageable = PageRequest.of(teamDto.getPageNumber(), teamDto.getPageSize(), Sort.Direction.ASC, "teamId");
        }
        Team team = new Team();
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
            if (teamDto.getStatus()) {
                Predicate isActive = criteriaBuilder.and(root.get("status").in(teamDto.getStatus()));
                addTeamSpecification.add(isActive);
            }
            if (teamDto.getTeamLeader() != null) {
                Predicate teamLeader = criteriaBuilder.and(root.get("TeamLeader").in(teamDto.getTeamLeader()));
                addTeamSpecification.add(teamLeader);
            }
            if (teamDto.getTeamManager() != null) {
                Predicate teamManager = criteriaBuilder.and(root.get("teamManager").in(teamDto.getTeamManager()));
                addTeamSpecification.add(teamManager);
            }
            return criteriaBuilder.and(addTeamSpecification.toArray(new Predicate[0]));
        });

        Page<Team> teamObjective = teamRepo.findAll(teamSpecification, pageable);
        if (teamObjective.getContent() != null) {

            return teamObjective.stream().filter(obj -> obj.getIsActive().equals(Boolean.TRUE)).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }


    private SiteDto entityToSiteDtoForGetAll(Site site) {
        SiteDto siteDto = new SiteDto();
        siteDto.setSiteName(site.getSiteName());
        siteDto.setSiteId(site.getSiteId());
        siteDto.setCity(entityToCityDto(site.getCity()));
        siteDto.setCountry(entityToCountryDtoForGetAll(site.getCountry()));
        siteDto.setRegion(entityToRegionDtoForGetAll(site.getRegion()));
        siteDto.setSiteManager(entityToAppUserDto(site.getSiteManager()));
        siteDto.setStatus(site.getIsActive());
        return siteDto;
    }

    private RegionDto entityToRegionDtoForGetAll(MtRegion region) {
        RegionDto regionDto = new RegionDto();
        regionDto.setRegionName(regionDto.getRegionName());
        return regionDto;
    }

    private CountryDto entityToCountryDtoForGetAll(MtCountry country) {
        CountryDto countryDto = new CountryDto();
        countryDto.setCountryName(country.getCountryName());
        return countryDto;
    }

    private CityDto entityToCityDto(MtCity mtCity) {
        CityDto cityDto = new CityDto();
        cityDto.setCityName(mtCity.getCityName());
        return cityDto;
    }

    public TeamDto entityToDtoForGetAll(Team team) {
        TeamDto teamDto = new TeamDto();
        teamDto.setTeamName(team.getTeamName());
        teamDto.setTeamId(team.getTeamId());
        if (team.getTeamLeader() != null) {
            teamDto.setTeamLeader(entityToAppUserDto(team.getTeamLeader()));
        }
        if (team.getTeamManager() != null) {
            teamDto.setTeamLeader(entityToAppUserDto(team.getTeamManager()));
        }
        teamDto.setSite(entityToSiteDto(team.getSite()));
        teamDto.setShiftTimings(teamDto.getShiftTimings());
        teamDto.setStatus(team.getIsActive());
        return teamDto;
    }

    private SiteDto entityToSiteDto(Site site) {
        SiteDto siteDto = new SiteDto();
        siteDto.setSiteName(site.getSiteName());
        return siteDto;
    }


    public AppUser dtoToAppUserEntity(AppUserDto appUserDto) {
        AppUser appUser = new AppUser();
        appUser.setUserName(appUserDto.getUserName());
        appUser.setEmail(appUserDto.getEmail());
        appUser.setEmpId(appUserDto.getEmpId());
        appUser.setFirstName(appUserDto.getFirstName());
        appUser.setLastName(appUserDto.getLastName());
        appUser.setMobile(appUserDto.getMobile());
        appUser.setRoles(Roles.valueOf(appUserDto.getRoles()));
        appUser.setPassword(appUser.getPassword());
        return appUser;
    }


    public AppUserDto entityToAppUserDto(AppUser appUser) {
        AppUserDto appUserDto = new AppUserDto();
        appUserDto.setUserName(appUser.getUserName());
        appUserDto.setEmail(appUser.getEmail());
        appUserDto.setEmpId(appUser.getEmpId());
        appUserDto.setFirstName(appUser.getFirstName());
        appUserDto.setLastName(appUser.getLastName());
        appUserDto.setRoles(String.valueOf(appUser.getRoles()));
        return appUserDto;
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
        if (team.getTeamLeader() != null) {
            searchKey = searchKey + " " + team.getTeamLeader();
        }
        if (team.getTeamManager() != null) {
            searchKey = searchKey + " " + team.getTeamManager();
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
        if (site.getSiteManager() != null) {
            searchKey = searchKey + " " + site.getSiteManager();
        }

        return searchKey;
    }

    @Override
    public List<AppUser> getAllAppUserByAlerts(AppUserDto request) {
        List<Roles> appUserList = Arrays.asList(Roles.TEAM_LEADER, Roles.TEAM_MANAGER);
        if (Validator.isValid(request.getAlerts().name())){
            return appUserRepo.findAllByAlertsAndRolesIn(request.getAlerts(),appUserList);
        }

        return new ArrayList<>();
    }
}

