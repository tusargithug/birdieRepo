package net.thrymr.services.impl;

import net.thrymr.dto.*;
import net.thrymr.enums.Roles;
import net.thrymr.enums.SlotShift;
import net.thrymr.model.*;
import net.thrymr.model.master.*;
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
        MtTeam mtTeam = new MtTeam();
        mtTeam.setTeamId(teamDto.getTeamId());
        mtTeam.setTeamName(teamDto.getTeamName());
        //Team_leader
        if (teamDto.getTeamLeaderId() != null && appUserRepo.existsById(teamDto.getTeamLeaderId())) {
            Optional<AppUser> optionalAppUser = appUserRepo.findById(teamDto.getTeamLeaderId());
            if (optionalAppUser.get().getRoles().equals(Roles.TEAM_LEADER)) {
                optionalAppUser.ifPresent(mtTeam::setTeamLeader);
            }
        }
        //Team_manager
        if (teamDto.getTeamManagerId() != null && appUserRepo.existsById(teamDto.getTeamManagerId())) {
            Optional<AppUser> optionalAppUser = appUserRepo.findById(teamDto.getTeamManagerId());
            if (optionalAppUser.get().getRoles().equals(Roles.TEAM_MANAGER)) {
                optionalAppUser.ifPresent(mtTeam::setTeamManager);
            }
        }
        //Site
        if (teamDto.getSiteId() != null && siteRepo.existsById(teamDto.getSiteId())) {
            Optional<MtSite> optionalSite = siteRepo.findById(teamDto.getSiteId());
            optionalSite.ifPresent(mtTeam::setMtSite);
        }
        if (teamDto.getShiftTimingsId() != null && shiftTimingsRepo.existsById(teamDto.getShiftTimingsId())) {
            Optional<MtShiftTimings> optionalShiftTimings = shiftTimingsRepo.findById(teamDto.getShiftTimingsId());
            optionalShiftTimings.ifPresent(mtTeam::setMtShiftTimings);
        }
        //team.setShiftTimings(dtoToShiftTimings(teamDto.getShiftTimings()));
        if (teamDto.getStatus().equals(Boolean.TRUE)) {
            mtTeam.setIsActive(teamDto.getStatus());
        }
        mtTeam.setSearchKey(getTeamSearchKey(mtTeam));
        teamRepo.save(mtTeam);
        return "Team save successfully";
    }


    @Override
    public String updateTeam(TeamDto teamDto) {
        Optional<MtTeam> teamId = teamRepo.findById(teamDto.getId());
        MtTeam mtTeam;
        if (teamId.isPresent()) {
            mtTeam = teamId.get();
            if (Validator.isValid(teamDto.getTeamName())) {
                mtTeam.setTeamName(teamDto.getTeamName());
            }
            if (teamDto.getTeamLeaderId() != null && appUserRepo.existsById(teamDto.getTeamLeaderId())) {
                Optional<AppUser> optionalAppUser = appUserRepo.findById(teamDto.getTeamLeaderId());
                if (optionalAppUser.get().getRoles().equals(Roles.TEAM_LEADER)) {
                    optionalAppUser.ifPresent(mtTeam::setTeamLeader);
                }
            }
            //Team_manager
            if (teamDto.getTeamManagerId() != null && appUserRepo.existsById(teamDto.getTeamManagerId())) {
                Optional<AppUser> optionalAppUser = appUserRepo.findById(teamDto.getTeamManagerId());
                if (optionalAppUser.get().getRoles().equals(Roles.TEAM_MANAGER)) {
                    optionalAppUser.ifPresent(mtTeam::setTeamManager);
                }
            }
            //Site
            if (teamDto.getSiteId() != null && siteRepo.existsById(teamDto.getSiteId())) {
                Optional<MtSite> optionalSite = siteRepo.findById(teamDto.getSiteId());
                optionalSite.ifPresent(mtTeam::setMtSite);
            }
            if (teamDto.getShiftTimingsId() != null && shiftTimingsRepo.existsById(teamDto.getShiftTimingsId())) {
                Optional<MtShiftTimings> optionalShiftTimings = shiftTimingsRepo.findById(teamDto.getShiftTimingsId());
                optionalShiftTimings.ifPresent(mtTeam::setMtShiftTimings);
            }
            if (teamDto.getStatus().equals(Boolean.FALSE) || teamDto.getStatus().equals(Boolean.TRUE)) {
                mtTeam.setIsActive(teamDto.getStatus());
            }
            //team.setSearchKey(getTeamSearchKey(team));
            teamRepo.save(mtTeam);
            return "Team update successfully";
        }
        return "this id not in database";
    }

    @Override
    public List<MtTeam> getAllTeam() {
        List<MtTeam> mtTeamList = teamRepo.findAll();
        if (!mtTeamList.isEmpty()) {
            return mtTeamList.stream().filter(obj -> obj.getIsActive().equals(Boolean.TRUE)).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }


    @Override
    public String deleteTeamById(Long id) {
        if (Validator.isValid(id)) {
            Optional<MtTeam> teamId = teamRepo.findById(id);
            MtTeam mtTeam;
            if (teamId.isPresent()) {
                mtTeam = teamId.get();
                mtTeam.setIsActive(Boolean.FALSE);
                mtTeam.setIsDeleted(Boolean.TRUE);
                teamRepo.save(mtTeam);
            }
            return "delete records successfully";
        }
        return "this id not present in database";
    }

    @Override
    public String saveSite(SiteDto siteDto) {
        MtSite mtSite = new MtSite();
        mtSite.setSiteId(siteDto.getSiteId());
        mtSite.setSiteName(siteDto.getSiteName());
        //Region
        if (siteDto.getRegionId() != null && regionRepo.existsById(siteDto.getRegionId())) {
            Optional<MtRegion> optionalRegion = regionRepo.findById(siteDto.getRegionId());
            optionalRegion.ifPresent(mtSite::setRegion);
        }
        //Country
        if (siteDto.getCountryId() != null && countryRepo.existsById(siteDto.getCountryId())) {
            Optional<MtCountry> optionalCountry = countryRepo.findById(siteDto.getCountryId());
            optionalCountry.ifPresent(mtSite::setCountry);
        }
        //City
        if (siteDto.getCityId() != null && cityRepo.existsById(siteDto.getCityId())) {
            Optional<MtCity> optionalCity = cityRepo.findById(siteDto.getCityId());
            optionalCity.ifPresent(mtSite::setCity);
        }
        //siteManager
        if (siteDto.getSiteManagerId() != null && appUserRepo.existsById(siteDto.getSiteManagerId())) {
            Optional<AppUser> optionalAppUser = appUserRepo.findById(siteDto.getSiteManagerId());
            if (optionalAppUser.isPresent() && optionalAppUser.get().getRoles().equals(Roles.SITE_MANAGER)) {
                optionalAppUser.ifPresent(mtSite::setSiteManager);
            }
        }
        siteDto.setSearchKey(saveSiteSearchKey(mtSite));
        if (siteDto.getStatus().equals(Boolean.TRUE)) {
            mtSite.setIsActive(siteDto.getStatus());
        }
        siteRepo.save(mtSite);
        return "site save successfully";
    }

    @Override
    public String updateSite(SiteDto siteDto) {
        if (!Validator.isValid(siteDto.getId())) {
            return "id required";
        }

        Optional<MtSite> optionalSite = siteRepo.findById(siteDto.getId());
        MtSite mtSite;
        if (optionalSite.isPresent()) {
            mtSite = optionalSite.get();
            if (Validator.isValid(siteDto.getSiteId())) {
                mtSite.setSiteId(siteDto.getSiteId());
            }
            if (Validator.isValid(siteDto.getSiteName())) {
                mtSite.setSiteName(siteDto.getSiteName());
            }
            //Region
            if (siteDto.getRegionId() != null && regionRepo.existsById(siteDto.getRegionId())) {
                Optional<MtRegion> optionalRegion = regionRepo.findById(siteDto.getRegionId());
                optionalRegion.ifPresent(mtSite::setRegion);
            }
            //Country
            if (siteDto.getCountryId() != null && countryRepo.existsById(siteDto.getCountryId())) {
                Optional<MtCountry> optionalCountry = countryRepo.findById(siteDto.getCountryId());
                optionalCountry.ifPresent(mtSite::setCountry);
            }
            //City
            if (siteDto.getCityId() != null && cityRepo.existsById(siteDto.getCityId())) {
                Optional<MtCity> optionalCity = cityRepo.findById(siteDto.getCityId());
                optionalCity.ifPresent(mtSite::setCity);
            }
            //siteManager
            if (siteDto.getSiteManagerId() != null && appUserRepo.existsById(siteDto.getSiteManagerId())) {
                Optional<AppUser> optionalAppUser = appUserRepo.findById(siteDto.getSiteManagerId());
                optionalAppUser.ifPresent(mtSite::setSiteManager);
            }

            siteRepo.save(mtSite);
            return "site update successfully";
        }
        return "this id not in database";
    }

    @Override
    public List<MtSite> getAllSite() {
        List<MtSite> mtSiteList = siteRepo.findAll();
        if (!mtSiteList.isEmpty()) {
            return mtSiteList.stream().filter(obj -> obj.getIsActive().equals(Boolean.TRUE)).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    @Override
    public List<MtSite> getAllSitePagination(SiteDto siteDto) {
        List<MtSite> mtSiteList = siteRepo.findAll();
        Pageable pageable = null;
        if (siteDto.getPageSize() != null) {
            pageable = PageRequest.of(siteDto.getPageNumber(), siteDto.getPageSize());
        }
        if (siteDto.getSiteId() != null) {
            pageable = PageRequest.of(siteDto.getPageNumber(), siteDto.getPageSize(), Sort.Direction.ASC, "siteId");
        }
        Specification<MtSite> siteSpecification = ((root, criteriaQuery, criteriaBuilder) -> {
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
        Page<MtSite> siteObjective = siteRepo.findAll(siteSpecification, pageable);
        if (siteObjective.getContent() != null) {
            return siteObjective.stream().filter(obj -> obj.getIsActive().equals(Boolean.TRUE)).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    @Override
    public List<MtShiftTimings> getAllShiftTimings() {
        List<MtShiftTimings> mtShiftTimingsList = shiftTimingsRepo.findAll();
        if (!mtShiftTimingsList.isEmpty()) {
            return mtShiftTimingsList.stream().filter(obj -> obj.getIsActive().equals(Boolean.TRUE)).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    @Override
    public String deleteSiteById(Long id) {
        Optional<MtSite> optionalSite = siteRepo.findById(id);
        MtSite mtSite;
        if (optionalSite.isPresent()) {
            mtSite = optionalSite.get();
            mtSite.setIsActive(Boolean.FALSE);
            mtSite.setIsDeleted(Boolean.TRUE);
            siteRepo.save(mtSite);
        }
        return "delete record successfully";
    }

    @Override
    public String saveSiftTimings(ShiftTimingsDto shiftTimingsDto) {
        MtShiftTimings mtShiftTimings = new MtShiftTimings();
        if (shiftTimingsDto.getShiftName().equals(SlotShift.MORNING)) {
            mtShiftTimings.setShiftName(SlotShift.MORNING);
        }
        if (shiftTimingsDto.getShiftName().equals(SlotShift.AFTERNOON)) {
            mtShiftTimings.setShiftName(SlotShift.AFTERNOON);
        }
        if (shiftTimingsDto.getShiftName().equals(SlotShift.EVENING)) {
            mtShiftTimings.setShiftName(SlotShift.EVENING);
        }
        mtShiftTimings.setShiftStatAt(shiftTimingsDto.getShiftStatAt());
        mtShiftTimings.setShiftEndAt(shiftTimingsDto.getShiftEndAt());
        if (shiftTimingsDto.getSiteId() != null && siteRepo.existsById(shiftTimingsDto.getSiteId())) {
            Optional<MtSite> optionalSite = siteRepo.findById(shiftTimingsDto.getSiteId());
            optionalSite.ifPresent(mtShiftTimings::setMtSite);
        }
        if (Validator.isValid(shiftTimingsDto.getTeamId())) {
            Optional<MtTeam> optionalTeamId = teamRepo.findById(shiftTimingsDto.getTeamId());
            if (optionalTeamId.isPresent()) {
                mtShiftTimings.setMtTeam(optionalTeamId.get());
            }
        }
        if (Validator.isValid(shiftTimingsDto.getCounsellorId())) {
            Optional<MtCounsellor> optionalCounsellorId = counsellorRepo.findById(shiftTimingsDto.getCounsellorId());
            if (optionalCounsellorId.isPresent()) {
                mtShiftTimings.setCounsellors(optionalCounsellorId.get());
            }
        }
        if (shiftTimingsDto.getStatus().equals(Boolean.TRUE)) {
            mtShiftTimings.setIsActive(shiftTimingsDto.getStatus());
        }
        shiftTimingsRepo.save(mtShiftTimings);
        return "shift timings save successfully";
    }

    @Override
    public String updateSiftTimings(ShiftTimingsDto shiftTimingsDto) {
        if (Validator.isValid(shiftTimingsDto.getId())) {
            Optional<MtShiftTimings> shiftTimingsId = shiftTimingsRepo.findById(shiftTimingsDto.getId());
            MtShiftTimings mtShiftTimings;
            if (shiftTimingsId.isPresent()) {
                mtShiftTimings = shiftTimingsId.get();
                if (Validator.isValid(String.valueOf(shiftTimingsDto.getShiftName()))) {
                    System.out.println(shiftTimingsDto.getShiftName());
                    mtShiftTimings.setShiftName(shiftTimingsDto.getShiftName());
                }
                mtShiftTimings.setShiftStatAt(shiftTimingsDto.getShiftStatAt());
                mtShiftTimings.setShiftEndAt(shiftTimingsDto.getShiftEndAt());
                if (Validator.isValid(String.valueOf(shiftTimingsDto.getStatus()))) {
                    mtShiftTimings.setIsActive(shiftTimingsDto.getStatus());
                }
                if (shiftTimingsDto.getStatus().equals(Boolean.TRUE) || shiftTimingsDto.getStatus().equals(Boolean.FALSE)) {
                    mtShiftTimings.setIsActive(shiftTimingsDto.getStatus());
                }
                shiftTimingsRepo.save(mtShiftTimings);
                return "shift timings update successfully";
            }
        }
        return "this id not in database";
    }

    @Override
    public String deleteSiftTimingsById(Long id) {
        Optional<MtShiftTimings> shiftTimingsId = shiftTimingsRepo.findById(id);
        MtShiftTimings mtShiftTimings;
        if (shiftTimingsId.isPresent()) {
            mtShiftTimings = shiftTimingsId.get();
            mtShiftTimings.setIsActive(Boolean.FALSE);
            mtShiftTimings.setIsDeleted(Boolean.TRUE);
            shiftTimingsRepo.save(mtShiftTimings);
            return "delete record successfully";
        }
        return "this id not in database";
    }

    @Override
    public List<MtTeam> getAllTeamPagination(TeamDto teamDto) {

        Pageable pageable = null;
        if (teamDto.getPageSize() != null) {
            pageable = PageRequest.of(teamDto.getPageNumber(), teamDto.getPageSize());
        }
        if (teamDto.getTeamId() != null) {
            pageable = PageRequest.of(teamDto.getPageNumber(), teamDto.getPageSize(), Sort.Direction.ASC, "teamId");
        }
        MtTeam mtTeam = new MtTeam();
        //filters
        Specification<MtTeam> teamSpecification = ((root, criteriaQuery, criteriaBuilder) -> {
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

        Page<MtTeam> teamObjective = teamRepo.findAll(teamSpecification, pageable);
        if (teamObjective.getContent() != null) {

            return teamObjective.stream().filter(obj -> obj.getIsActive().equals(Boolean.TRUE)).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }


    private SiteDto entityToSiteDtoForGetAll(MtSite mtSite) {
        SiteDto siteDto = new SiteDto();
        siteDto.setSiteName(mtSite.getSiteName());
        siteDto.setSiteId(mtSite.getSiteId());
        siteDto.setCity(entityToCityDto(mtSite.getCity()));
        siteDto.setCountry(entityToCountryDtoForGetAll(mtSite.getCountry()));
        siteDto.setRegion(entityToRegionDtoForGetAll(mtSite.getRegion()));
        siteDto.setSiteManager(entityToAppUserDto(mtSite.getSiteManager()));
        siteDto.setStatus(mtSite.getIsActive());
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

    public TeamDto entityToDtoForGetAll(MtTeam mtTeam) {
        TeamDto teamDto = new TeamDto();
        teamDto.setTeamName(mtTeam.getTeamName());
        teamDto.setTeamId(mtTeam.getTeamId());
        if (mtTeam.getTeamLeader() != null) {
            teamDto.setTeamLeader(entityToAppUserDto(mtTeam.getTeamLeader()));
        }
        if (mtTeam.getTeamManager() != null) {
            teamDto.setTeamLeader(entityToAppUserDto(mtTeam.getTeamManager()));
        }
        teamDto.setSite(entityToSiteDto(mtTeam.getMtSite()));
        teamDto.setShiftTimings(teamDto.getShiftTimings());
        teamDto.setStatus(mtTeam.getIsActive());
        return teamDto;
    }

    private SiteDto entityToSiteDto(MtSite mtSite) {
        SiteDto siteDto = new SiteDto();
        siteDto.setSiteName(mtSite.getSiteName());
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

    public String getTeamSearchKey(MtTeam mtTeam) {
        String searchKey = "";
        if (mtTeam.getMtSite() != null) {
            searchKey = searchKey + " " + mtTeam.getMtSite();
        }
        if (mtTeam.getTeamId() != null) {
            searchKey = searchKey + " " + mtTeam.getTeamId();
        }
        if (mtTeam.getTeamName() != null) {
            searchKey = searchKey + " " + mtTeam.getTeamName();
        }
        if (mtTeam.getTeamLeader() != null) {
            searchKey = searchKey + " " + mtTeam.getTeamLeader();
        }
        if (mtTeam.getTeamManager() != null) {
            searchKey = searchKey + " " + mtTeam.getTeamManager();
        }
        if (mtTeam.getMtShiftTimings() != null) {
            searchKey = searchKey + " " + mtTeam.getMtShiftTimings();
        }
        return searchKey;
    }

    public String saveSiteSearchKey(MtSite mtSite) {
        String searchKey = "";
        if (mtSite.getRegion() != null) {
            searchKey = searchKey + " " + mtSite.getRegion();
        }
        if (mtSite.getSiteId() != null) {
            searchKey = searchKey + " " + mtSite.getSiteId();
        }
        if (mtSite.getSiteName() != null) {
            searchKey = searchKey + " " + mtSite.getSiteName();
        }
        if (mtSite.getCity() != null) {
            searchKey = searchKey + " " + mtSite.getCity();
        }
        if (mtSite.getSiteManager() != null) {
            searchKey = searchKey + " " + mtSite.getSiteManager();
        }

        return searchKey;
    }
}

