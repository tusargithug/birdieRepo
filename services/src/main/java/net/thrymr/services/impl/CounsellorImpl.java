package net.thrymr.services.impl;

import net.thrymr.constant.Constants;
import net.thrymr.dto.CounsellorDto;
import net.thrymr.dto.EducationDto;
import net.thrymr.dto.LanguageDto;
import net.thrymr.dto.response.PaginationResponse;
import net.thrymr.enums.Gender;
import net.thrymr.enums.Roles;
import net.thrymr.model.*;
import net.thrymr.model.master.EducationDetails;
import net.thrymr.model.master.LanguageDetails;
import net.thrymr.repository.*;
import net.thrymr.services.CounsellorService;
import net.thrymr.utils.DateUtils;
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
   EducationRepo educationRepo;
   @Autowired
   LanguageRepo languageRepo;


    @Override
    public String createCounsellor(CounsellorDto request) {
        Counsellor counsellor = new Counsellor();
        counsellor.setEmpId(request.getEmpId());
        counsellor.setCounsellorName(request.getCounsellorName());
        counsellor.setEducationalDetails(request.getEducationalDetails());
        counsellor.setEmailId(request.getEmailId());
        counsellor.setLanguages(request.getLanguages());
        counsellor.setBio(request.getBio());
        counsellor.setShiftStartAt(DateUtils.toStringToLocalTime(request.getShiftStartAt(), Constants.TIME_FORMAT_12_HOURS));
        counsellor.setShiftEndAt(DateUtils.toStringToLocalTime(request.getShiftEndAt(), Constants.TIME_FORMAT_12_HOURS));
        counsellor.setShiftTimings(request.getShiftStartAt() + " - " + request.getShiftEndAt());
        if (Validator.isValid(request.getDesignation()) && request.getDesignation().equals("COUNSELLOR")) {
            counsellor.setDesignation(Roles.valueOf(request.getDesignation()));
        }
        counsellor.setCountryCode(request.getCountryCode());
        counsellor.setMobileNumber(request.getMobileNumber());
        counsellor.setGender(Gender.valueOf(request.getGender()));
        if (Validator.isValid(request.getSiteId())) {
            Optional<Site> optionalSite = siteRepo.findById(request.getSiteId());
            if (optionalSite.isPresent()) {
                counsellor.setSite(optionalSite.get());
            }
        }
        counsellor.setSearchKey(getCounsellorSearchKey(counsellor));
        counsellorRepo.save(counsellor);
        return "counsellor create successfully";
    }

    @Override
    public String updateCounsellorById(CounsellorDto request) {
        if (Validator.isValid(request.getId())) {
            Optional<Counsellor> optionalCounsellor = counsellorRepo.findById(request.getId());
            Counsellor counsellor = null;
            if (optionalCounsellor.isPresent()) {
                counsellor = optionalCounsellor.get();
                if (Validator.isValid(request.getEmpId())) {
                    counsellor.setEmpId(request.getEmpId());
                }
                if (Validator.isValid(request.getCounsellorName())) {
                    counsellor.setCounsellorName(request.getCounsellorName());
                }
                if (Validator.isValid(request.getEducationalDetails())) {
                    counsellor.setEducationalDetails(request.getEducationalDetails());
                }
                if (Validator.isValid(request.getEmailId())) {
                    counsellor.setEmailId(request.getEmailId());
                }
                if (Validator.isValid(request.getLanguages())) {
                    counsellor.setLanguages(request.getLanguages());
                }
                if (Validator.isValid(request.getBio())) {
                    counsellor.setBio(request.getBio());
                }
                if (Validator.isValid(request.getDesignation()) && request.getDesignation().equals("COUNSELLOR")) {
                    counsellor.setDesignation(Roles.valueOf(request.getDesignation()));
                }
                if (Validator.isValid(request.getCountryCode())) {
                    counsellor.setCountryCode(request.getCountryCode());
                }
                if (Validator.isValid(request.getMobileNumber())) {
                    counsellor.setMobileNumber(request.getMobileNumber());
                }
                if (request.getShiftStartAt() != null) {
                    counsellor.setShiftStartAt(DateUtils.toStringToLocalTime(request.getShiftStartAt(), Constants.TIME_FORMAT_12_HOURS));
                }
                if (request.getShiftEndAt() != null) {
                    counsellor.setShiftEndAt(DateUtils.toStringToLocalTime(request.getShiftEndAt(), Constants.TIME_FORMAT_12_HOURS));
                }
                if (request.getShiftStartAt() != null && request.getShiftEndAt() != null) {
                    counsellor.setShiftTimings(request.getShiftStartAt() + " - " + request.getShiftEndAt());
                }
                if (Validator.isValid(request.getGender())) {
                    counsellor.setGender(Gender.valueOf(request.getGender()));
                }
                if (Validator.isValid(request.getSiteId())) {
                    Optional<Site> optionalSite = siteRepo.findById(request.getSiteId());
                    if (optionalSite.isPresent()) {
                        counsellor.setSite(optionalSite.get());
                    }
                }
                counsellor.setSearchKey(getCounsellorSearchKey(counsellor));
                counsellorRepo.save(counsellor);
                return "counsellor update successfully";
            }
        }
        return "this id not in database";
    }

    @Override
    public String deleteCounsellorById(Long id) {
        Optional<Counsellor> optionalCounsellor = counsellorRepo.findById(id);
        Counsellor counsellor;
        if (optionalCounsellor.isPresent()) {
            counsellor = optionalCounsellor.get();
            counsellor.setIsDeleted(Boolean.TRUE);
            counsellor.setIsActive(Boolean.FALSE);
            counsellor.setSearchKey(getCounsellorSearchKey(counsellor));
            counsellorRepo.save(counsellor);
            return "counsellor delete successfully";
        }
        return "this id not in database";
    }

    @Override
    public PaginationResponse getAllCounsellorPagination(CounsellorDto response) {
        Pageable pageable = null;
        if (response.getPageSize() != null && response.getPageNumber() != null) {
            pageable = PageRequest.of(response.getPageNumber(), response.getPageSize());
        }
        if (response.getPageSize() != null && response.getPageNumber() != null) {
            pageable = PageRequest.of(response.getPageNumber(), response.getPageSize(), Sort.Direction.DESC, "createdOn");
        }
        if (response.getSortCounsellorName() != null && response.getSortCounsellorName().equals(Boolean.TRUE)) {
            pageable = PageRequest.of(response.getPageNumber(), response.getPageSize(), Sort.Direction.ASC, "counsellorName");
        } else if (response.getSortCounsellorName() != null && response.getSortCounsellorName().equals(Boolean.FALSE)) {
            pageable = PageRequest.of(response.getPageNumber(), response.getPageSize(), Sort.Direction.DESC, "counsellorName");
        }
        //filters
        Specification<Counsellor> addCounsellorSpecification = ((root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> addCounsellorPredicate = new ArrayList<>();
            if (response.getCounsellorName() != null) {
                Predicate counsellorName = criteriaBuilder.and(root.get("counsellorName").in(response.getCounsellorName()));
                addCounsellorPredicate.add(counsellorName);
            }
            if (response.getEmpId() != null && !response.getEmpId().isEmpty()) {
                Predicate empId = criteriaBuilder.and(root.get("empId").in(response.getEmpId()));
                addCounsellorPredicate.add(empId);
            }
            if (response.getAddedOn() != null && response.getAddedOn().equals(Boolean.TRUE)) {
                Predicate addedOn = criteriaBuilder.and(root.get("createdOn").in(response.getAddedOn()));
                addCounsellorPredicate.add(addedOn);
            }
            if (response.getDesignation() != null) {
                Predicate designation = criteriaBuilder.and(root.get("roles").in(response.getDesignation()));
                addCounsellorPredicate.add(designation);
            }
            if (response.getSiteId() != null) {
                Predicate site = criteriaBuilder.and(root.get("site").in(response.getSiteId()));
                addCounsellorPredicate.add(site);
            }

            if (Validator.isValid(response.getShiftTimings())) {
                Predicate shiftTimings = criteriaBuilder.and(root.get("shiftTimings").in(response.getShiftTimings()));
                addCounsellorPredicate.add(shiftTimings);
            }

            if (Validator.isValid(response.getSearchKey())) {
                Predicate searchPredicate = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("searchKey")),
                        "%" + response.getSearchKey().toLowerCase() + "%");
                addCounsellorPredicate.add(searchPredicate);
            }
            return criteriaBuilder.and(addCounsellorPredicate.toArray(new Predicate[0]));
        });
        PaginationResponse paginationResponse = new PaginationResponse();
        if (response.getPageSize() != null && response.getPageNumber() != null) {
            Page<Counsellor> councellorObjective = counsellorRepo.findAll(addCounsellorSpecification, pageable);
            if (councellorObjective.getContent() != null) {
                paginationResponse.setCounsellorList(councellorObjective.getContent());
                paginationResponse.setTotalPages(councellorObjective.getTotalPages());
                paginationResponse.setTotalElements(councellorObjective.getTotalElements());
                return paginationResponse;
            }
        } else {
            List<Counsellor> counsellorList = counsellorRepo.findAll(addCounsellorSpecification);
            paginationResponse.setCounsellorList(counsellorList.stream().filter(counsellor -> counsellor.getIsDeleted().equals(Boolean.FALSE)).collect(Collectors.toList()));
            return paginationResponse;
        }

        return new PaginationResponse();
    }

    @Override
    public Counsellor getCounsellorById(Long id) {
        if (Validator.isValid(id)) {
            Optional<Counsellor> optionalCounsellor = counsellorRepo.findById(id);
            if (optionalCounsellor.isPresent() && optionalCounsellor.get().getIsActive().equals(Boolean.TRUE)) {
                return optionalCounsellor.get();
            }
        }
        return new Counsellor();
    }

    @Override
    public List<EducationDetails> getAllEducationalDetails() {
        return educationRepo.findAll();
    }

    @Override
    public List<LanguageDetails> getAllLanguagesDetails() {
        return languageRepo.findAll();
    }

    @Override
    public String addNewLanguage(LanguageDto request) {
        LanguageDetails languageDetails = new LanguageDetails();
        languageDetails.setLanguageName(request.getLanguageName());
        languageRepo.save(languageDetails);
        return "language added successfully";
    }

    @Override
    public String addNewEducation(EducationDto request) {
        EducationDetails educationDetails = new EducationDetails();
        educationDetails.setEducationName(request.getEducationName());
        educationRepo.save(educationDetails);
        return "education added successfully";
    }

    @Override
    public String updateEducationDetailsById(EducationDto request) {
        EducationDetails educationDetails = null;
        if(Validator.isValid(request.getId())){
            Optional<EducationDetails> optionalEducationDetails = educationRepo.findById(request.getId());
            if(optionalEducationDetails.isPresent()){
                educationDetails = optionalEducationDetails.get();
                if (Validator.isValid(request.getEducationName())) {
                    educationDetails.setEducationName(request.getEducationName());
                }
                educationRepo.save(educationDetails);
                return "educational details successfully updated";
            }
        }
        return "This id not present in database";
    }

    @Override
    public String updateLanguageDetailsById(LanguageDto request) {
        LanguageDetails languageDetails = null;
        if(Validator.isValid(request.getId())){
            Optional<LanguageDetails> languageDetailsOptional = languageRepo.findById(request.getId());
            if(languageDetailsOptional.isPresent()) {
                languageDetails = languageDetailsOptional.get();
                languageDetails.setLanguageName(request.getLanguageName());
                languageRepo.save(languageDetails);
                return "language details successfully updated";
            }
        }
        return "This id not present in database";
    }

    @Override
    public EducationDetails getEducationalDetailsById(Long id) {
        EducationDetails educationDetails = null;
        if(Validator.isValid(id)) {
            Optional<EducationDetails> optionalEducationDetails = educationRepo.findById(id);
            if(optionalEducationDetails.isPresent()){
                educationDetails = optionalEducationDetails.get();
                return educationDetails;
            }
        }
        return new EducationDetails();
    }

    @Override
    public LanguageDetails getLanguageDetailsById(Long id) {
        LanguageDetails languageDetails = null;
        if(Validator.isValid(id)){
            Optional<LanguageDetails> languageDetailsOptional = languageRepo.findById(id);
            if(languageDetailsOptional.isPresent()){
                languageDetails = languageDetailsOptional.get();
                return languageDetails;
            }
        }
        return new LanguageDetails();
    }

    public String getCounsellorSearchKey(Counsellor counsellor) {
        String searchKey = "";
        if (counsellor.getCounsellorName() != null) {
            searchKey = searchKey + " " + counsellor.getCounsellorName();
        }
        if (counsellor.getEmpId() != null) {
            searchKey = searchKey + " " + counsellor.getEmpId();
        }
        if (counsellor.getEmailId() != null) {
            searchKey = searchKey + " " + counsellor.getEmailId();
        }
        if (counsellor.getCountryCode() != null) {
            searchKey = searchKey + " " + counsellor.getCountryCode();
        }
        if (counsellor.getMobileNumber() != null) {
            searchKey = searchKey + " " + counsellor.getMobileNumber();
        }
        if (counsellor.getDesignation() != null) {
            searchKey = searchKey + " " + counsellor.getDesignation();
        }
        if (counsellor.getShiftStartAt() != null) {
            searchKey = searchKey + " " + counsellor.getShiftStartAt();
        }
        if (counsellor.getShiftEndAt() != null) {
            searchKey = searchKey + " " + counsellor.getShiftEndAt();
        }
        if(counsellor.getEducationalDetails() != null){
            searchKey = searchKey + " " +counsellor.getEducationalDetails();
        }
        if(counsellor.getLanguages() != null){
            searchKey = searchKey + " " +counsellor.getLanguages();
        }
        if (counsellor.getShiftTimings() != null) {
            searchKey = searchKey + " " + counsellor.getShiftTimings();
        }
        if (counsellor.getBio() != null) {
            searchKey = searchKey + " " + counsellor.getBio();
        }
        if (counsellor.getGender() != null) {
            searchKey = searchKey + " " + counsellor.getGender();
        }
        if (counsellor.getIsActive() != null) {
            searchKey = searchKey + " " + counsellor.getIsActive();
        }
        return searchKey;
    }


}
