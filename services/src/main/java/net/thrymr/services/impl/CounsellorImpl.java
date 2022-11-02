package net.thrymr.services.impl;

import net.thrymr.dto.CounsellorDto;
import net.thrymr.enums.Roles;
import net.thrymr.model.*;
import net.thrymr.repository.*;
import net.thrymr.services.CounsellorService;
import net.thrymr.utils.Validator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CounsellorImpl implements CounsellorService {

    public final CounsellorRepo counsellorRepo;
    public final AppUserRepo appUserRepo;
    private final TeamRepo teamRepo;
    private final SiteRepo siteRepo;
    private final ShiftTimingsRepo shiftTimingsRepo;

    public CounsellorImpl(CounsellorRepo counsellorRepo, AppUserRepo appUserRepo, TeamRepo teamRepo, SiteRepo siteRepo, ShiftTimingsRepo shiftTimingsRepo) {
        this.counsellorRepo = counsellorRepo;
        this.appUserRepo = appUserRepo;
        this.teamRepo = teamRepo;
        this.siteRepo = siteRepo;
        this.shiftTimingsRepo = shiftTimingsRepo;

    }

    @Override
    public String createCounsellor(CounsellorDto request) {
        AppUser user=new AppUser();
        Counsellor counsellor=new Counsellor();
        if(Validator.isValid(request.getAppUserName())){
            user.setUserName(request.getAppUserName());
        }
        if(Validator.isValid(request.getEmployeeId())){
            user.setEmpId(request.getEmployeeId());
        }
        if(Validator.isValid(request.getDesignation())){
            user.setRoles(Roles.COUNSELLOR);
        }
        if (Validator.isValid(request.getContactNumber())){
            user.setMobile(request.getContactNumber());
        }
        if (Validator.isValid(request.getEmailId())){
            user.setEmail(request.getEmailId());
        }
        if(Validator.isValid(request.getTeamId())){
            Optional<Team> optionalTeam=teamRepo.findById(request.getTeamId());
            if(optionalTeam.isPresent()){
                user.setTeam(optionalTeam.get());
            }
        }
        if(Validator.isValid(request.getSiteId())){
            Optional<Site> optionalSite=siteRepo.findById(request.getSiteId());
            if(optionalSite.isPresent()){
                user.setSite(optionalSite.get());
            }
        }
        if(Validator.isValid(request.getShiftTimingsId())){
            Optional<ShiftTimings> optionalShiftTimings=shiftTimingsRepo.findById(request.getShiftTimingsId());
            if(optionalShiftTimings.isPresent()){
                user.setShiftTimings(optionalShiftTimings.get());
            }
        }
        counsellor.setAppUser(user);

        //Team_Manager
        if(request.getTeamManagerId()!=null && appUserRepo.existsById(request.getTeamManagerId())){
            Optional<AppUser> optionalAppUser=appUserRepo.findById(request.getTeamManagerId());
            if(optionalAppUser.isPresent() && optionalAppUser.get().getRoles().equals(Roles.TEAM_MANAGER)) {
                optionalAppUser.ifPresent(counsellor::setTeamManager);
            }
        }
        appUserRepo.save(user);
        counsellor.setLanguages(request.getLanguages());
        counsellor.setEducationalDetails(request.getEducationalDetails());
        counsellor.setBio(request.getBio());
        counsellorRepo.save(counsellor);

        return "counsellor create successfully";
    }

    @Override
    public String updateCounsellorById(CounsellorDto request) {
        Optional<Counsellor> optionalCounsellor=counsellorRepo.findById(request.getId());
        Counsellor counsellor = null;
        AppUser user=null;
        if(optionalCounsellor.isPresent()){
            counsellor=optionalCounsellor.get();

            //Team_Manager
            if(request.getTeamManagerId()!=null && appUserRepo.existsById(request.getTeamManagerId())){
                Optional<AppUser> optionalAppUser=appUserRepo.findById(request.getTeamManagerId());
                if(optionalAppUser.isPresent() && optionalAppUser.get().getRoles().equals(Roles.TEAM_MANAGER)) {
                    optionalAppUser.ifPresent(counsellor::setTeamManager);
                }
            }
            if(Validator.isValid(request.getLanguages())) {
                counsellor.setLanguages(request.getLanguages());
            }
            if(Validator.isValid(request.getEducationalDetails())) {
                counsellor.setEducationalDetails(request.getEducationalDetails());
            }
            if(Validator.isValid(request.getBio())) {
                counsellor.setBio(request.getBio());
            }
        }
        Optional<AppUser> optionalAppUser=appUserRepo.findById(request.getAppUserId());
        if(optionalAppUser.isPresent()) {
            user=optionalAppUser.get();
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
                Optional<Team> optionalTeam = teamRepo.findById(request.getTeamId());
                if (optionalTeam.isPresent()) {
                    user.setTeam(optionalTeam.get());
                }
            }
            if (Validator.isValid(request.getSiteId())) {
                Optional<Site> optionalSite = siteRepo.findById(request.getSiteId());
                if (optionalSite.isPresent()) {
                    user.setSite(optionalSite.get());
                }
            }
            if (Validator.isValid(request.getShiftTimingsId())) {
                Optional<ShiftTimings> optionalShiftTimings = shiftTimingsRepo.findById(request.getShiftTimingsId());
                if (optionalShiftTimings.isPresent()) {
                    user.setShiftTimings(optionalShiftTimings.get());
                }
            }
        }
        if(Validator.isObjectValid(user)) {
            counsellor.setAppUser(user);
        }
        counsellorRepo.save(counsellor);
        return "counsellor update successfully";
    }

    @Override
    public String deleteCounsellorById(Long id) {
        Optional<Counsellor> optionalCounsellor=counsellorRepo.findById(id);
        Counsellor counsellor = null;
        if(optionalCounsellor.isPresent()){
            counsellor=optionalCounsellor.get();
            counsellor.setIsDeleted(Boolean.TRUE);
            counsellor.setIsActive(Boolean.FALSE);
        }
        return "counsellor delete successfully";
    }

    @Override
    public List<Counsellor> getAllCounsellor(CounsellorDto response) {
        Pageable pageable=null;
        if (response.getPageSize() != null) {
            pageable = PageRequest.of(response.getPageNumber(), response.getPageSize());
        }
        if (response.getAppUserName()!= null) {
            pageable = PageRequest.of(response.getPageNumber(),response.getPageSize(), Sort.Direction.DESC,"userName");
        }
        //filters
        Specification<Counsellor> addUserSpecification = ((root, criteriaQuery, criteriaBuilder)-> {
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
            if (response.getTeam()!= null) {
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
        Page<Counsellor> counsellorObjectives = counsellorRepo.findAll(addUserSpecification, pageable);

        List<Counsellor> counsellorList =new ArrayList<>();
        if(counsellorObjectives.getContent()!=null){
            counsellorList = counsellorObjectives.getContent().stream().filter(obj->obj.getAppUser().getRoles().equals(Roles.COUNSELLOR)).collect(Collectors.toList());
        }
        return  counsellorList;
    }
    
}
