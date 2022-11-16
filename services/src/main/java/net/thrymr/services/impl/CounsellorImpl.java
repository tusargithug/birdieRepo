package net.thrymr.services.impl;

import net.thrymr.dto.CounsellorDto;
import net.thrymr.enums.Roles;
import net.thrymr.model.*;
import net.thrymr.model.master.MtCounsellor;
import net.thrymr.model.master.MtShiftTimings;
import net.thrymr.model.master.MtSite;
import net.thrymr.model.master.MtTeam;
import net.thrymr.repository.*;
import net.thrymr.services.CounsellorService;
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
public class CounsellorImpl implements CounsellorService {

    @Autowired
    CounsellorRepo counsellorRepo;
    @Autowired
    AppUserRepo appUserRepo;
    @Autowired
    TeamRepo teamRepo;
    @Autowired
    SiteRepo siteRepo;
    @Autowired
    ShiftTimingsRepo shiftTimingsRepo;


    @Override
    public String createCounsellor(CounsellorDto request) {
        AppUser user = new AppUser();
        MtCounsellor mtCounsellor = new MtCounsellor();
        if (Validator.isValid(request.getAppUserName())) {
            user.setUserName(request.getAppUserName());
        }
        if (Validator.isValid(request.getEmployeeId())) {
            user.setEmpId(request.getEmployeeId());
        }
        if (Validator.isValid(request.getDesignation())) {
            user.setRoles(Roles.COUNSELLOR);
        }
        if (Validator.isValid(request.getContactNumber())) {
            user.setMobile(request.getContactNumber());
        }
        if (Validator.isValid(request.getEmailId())) {
            user.setEmail(request.getEmailId());
        }
        if (Validator.isValid(request.getTeamId())) {
            Optional<MtTeam> optionalTeam = teamRepo.findById(request.getTeamId());
            if (optionalTeam.isPresent()) {
                user.setMtTeam(optionalTeam.get());
            }
        }
        if (Validator.isValid(request.getSiteId())) {
            Optional<MtSite> optionalSite = siteRepo.findById(request.getSiteId());
            if (optionalSite.isPresent()) {
                user.setMtSite(optionalSite.get());
            }
        }
        if (Validator.isValid(request.getShiftTimingsId())) {
            Optional<MtShiftTimings> optionalShiftTimings = shiftTimingsRepo.findById(request.getShiftTimingsId());
            if (optionalShiftTimings.isPresent()) {
                user.setMtShiftTimings(optionalShiftTimings.get());
            }
        }
        mtCounsellor.setAppUser(user);

        //Team_Manager
        if (request.getTeamManagerId() != null && appUserRepo.existsById(request.getTeamManagerId())) {
            Optional<AppUser> optionalAppUser = appUserRepo.findById(request.getTeamManagerId());
            if (optionalAppUser.isPresent() && optionalAppUser.get().getRoles().equals(Roles.TEAM_MANAGER)) {
                optionalAppUser.ifPresent(mtCounsellor::setTeamManager);
            }
        }
        appUserRepo.save(user);
        mtCounsellor.setLanguages(request.getLanguages());
        mtCounsellor.setEducationalDetails(request.getEducationalDetails());
        mtCounsellor.setBio(request.getBio());
        counsellorRepo.save(mtCounsellor);

        return "counsellor create successfully";
    }

    @Override
    public String updateCounsellorById(CounsellorDto request) {
        if (Validator.isValid(request.getId())) {
            Optional<MtCounsellor> optionalCounsellor = counsellorRepo.findById(request.getId());
            MtCounsellor mtCounsellor = null;
            AppUser user = null;
            if (optionalCounsellor.isPresent()) {
                mtCounsellor = optionalCounsellor.get();

                //Team_Manager
                if (request.getTeamManagerId() != null && appUserRepo.existsById(request.getTeamManagerId())) {
                    Optional<AppUser> optionalAppUser = appUserRepo.findById(request.getTeamManagerId());
                    if (optionalAppUser.isPresent() && optionalAppUser.get().getRoles().equals(Roles.TEAM_MANAGER)) {
                        optionalAppUser.ifPresent(mtCounsellor::setTeamManager);
                    }
                }
                if (Validator.isValid(request.getLanguages())) {
                    mtCounsellor.setLanguages(request.getLanguages());
                }
                if (Validator.isValid(request.getEducationalDetails())) {
                    mtCounsellor.setEducationalDetails(request.getEducationalDetails());
                }
                if (Validator.isValid(request.getBio())) {
                    mtCounsellor.setBio(request.getBio());
                }
            }
            if (Validator.isValid(request.getAppUserId())) {
                Optional<AppUser> optionalAppUser = appUserRepo.findById(request.getAppUserId());
                if (optionalAppUser.isPresent()) {
                    user = optionalAppUser.get();
                    if (Validator.isValid(request.getAppUserName())) {
                        user.setUserName(request.getAppUserName());
                    }
                    if (Validator.isValid(request.getEmployeeId())) {
                        user.setEmpId(request.getEmployeeId());
                    }
                    if (Validator.isValid(request.getDesignation())) {
                        user.setRoles(Roles.COUNSELLOR);
                    }
                    if (Validator.isValid(request.getContactNumber())) {
                        user.setMobile(request.getContactNumber());
                    }
                    if (Validator.isValid(request.getEmailId())) {
                        user.setEmail(request.getEmailId());
                    }
                    if (Validator.isValid(request.getTeamId())) {
                        Optional<MtTeam> optionalTeam = teamRepo.findById(request.getTeamId());
                        if (optionalTeam.isPresent()) {
                            user.setMtTeam(optionalTeam.get());
                        }
                    }
                    if (Validator.isValid(request.getSiteId())) {
                        Optional<MtSite> optionalSite = siteRepo.findById(request.getSiteId());
                        if (optionalSite.isPresent()) {
                            user.setMtSite(optionalSite.get());
                        }
                    }
                    if (Validator.isValid(request.getShiftTimingsId())) {
                        Optional<MtShiftTimings> optionalShiftTimings = shiftTimingsRepo.findById(request.getShiftTimingsId());
                        if (optionalShiftTimings.isPresent()) {
                            user.setMtShiftTimings(optionalShiftTimings.get());
                        }
                    }
                }
            }
            if (Validator.isObjectValid(user)) {
                mtCounsellor.setAppUser(user);
            }
            counsellorRepo.save(mtCounsellor);
            return "counsellor update successfully";
        }
        return "this id not in database";
    }

    @Override
    public String deleteCounsellorById(Long id) {
        Optional<MtCounsellor> optionalCounsellor = counsellorRepo.findById(id);
        MtCounsellor mtCounsellor = null;
        if (optionalCounsellor.isPresent()) {
            mtCounsellor = optionalCounsellor.get();
            mtCounsellor.setIsDeleted(Boolean.TRUE);
            mtCounsellor.setIsActive(Boolean.FALSE);
        }
        return "counsellor delete successfully";
    }

    @Override
    public List<MtCounsellor> getAllCounsellor(CounsellorDto response) {
        Pageable pageable = null;
        if (response.getPageSize() != null) {
            pageable = PageRequest.of(response.getPageNumber(), response.getPageSize());
        }
        if (response.getAppUserName() != null) {
            pageable = PageRequest.of(response.getPageNumber(), response.getPageSize(), Sort.Direction.DESC, "userName");
        }
        //filters
        Specification<MtCounsellor> addUserSpecification = ((root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> addCounsellorPredicate = new ArrayList<>();
            if (response.getAppUserName() != null) {
                Predicate appUserName = criteriaBuilder.and(root.get("userName").in(response.getAppUserName()));
                addCounsellorPredicate.add(appUserName);
            }
            if (response.getEmployeeId() != null && !response.getEmployeeId().isEmpty()) {
                Predicate employeeId = criteriaBuilder.and(root.get("empId").in(response.getEmployeeId()));
                addCounsellorPredicate.add(employeeId);
            }
            if (response.getAddedOn() != null) {
                Predicate addedOn = criteriaBuilder.and(root.get("createdOn").in(response.getAddedOn()));
                addCounsellorPredicate.add(addedOn);
            }
            if (response.getDesignation() != null) {
                Predicate designation = criteriaBuilder.and(root.get("roles").in(response.getDesignation()));
                addCounsellorPredicate.add(designation);
            }
            if (response.getShiftTimings() != null) {
                Predicate shiftTimings = criteriaBuilder.and(root.get("shiftTimings").in(response.getShiftTimings()));
                addCounsellorPredicate.add(shiftTimings);
            }
            if (response.getSite() != null) {
                Predicate site = criteriaBuilder.and(root.get("site").in(response.getSite()));
                addCounsellorPredicate.add(site);
            }
            if (response.getTeam() != null) {
                Predicate team = criteriaBuilder.and(root.get("team").in(response.getTeam()));
                addCounsellorPredicate.add(team);
            }
            if (Validator.isValid(response.getSearchKey())) {
                Predicate searchPredicate = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("searchKey")),
                        "%" + response.getSearchKey().toLowerCase() + "%");
                addCounsellorPredicate.add(searchPredicate);
            }
            return criteriaBuilder.and(addCounsellorPredicate.toArray(new Predicate[0]));
        });
        Page<MtCounsellor> counsellorObjectives = counsellorRepo.findAll(addUserSpecification, pageable);

        List<MtCounsellor> mtCounsellorList = new ArrayList<>();
        if (counsellorObjectives.getContent() != null) {
            mtCounsellorList = counsellorObjectives.getContent().stream().filter(obj -> obj.getAppUser().getRoles().equals(Roles.COUNSELLOR)).collect(Collectors.toList());
        }
        return mtCounsellorList;
    }


    @Override
    public MtCounsellor getCounsellorById(Long id) {
        if (Validator.isValid(id)) {
            Optional<MtCounsellor> optionalCounsellor = counsellorRepo.findById(id);
            if (optionalCounsellor.isPresent() && optionalCounsellor.get().getIsActive().equals(Boolean.TRUE)) {
                return optionalCounsellor.get();
            }
        }
        return new MtCounsellor();
    }

}
