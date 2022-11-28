package net.thrymr.services.impl;

import net.thrymr.dto.VendorDto;
import net.thrymr.enums.Roles;
import net.thrymr.model.Site;
import net.thrymr.model.Vendor;
import net.thrymr.repository.AppUserRepo;
import net.thrymr.repository.SiteRepo;
import net.thrymr.repository.VendorRepo;
import net.thrymr.services.VendorService;
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
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VendorServiceImpl implements VendorService {
    @Autowired
    VendorRepo vendorRepo;
    @Autowired
    SiteRepo siteRepo;

    @Autowired
    AppUserRepo appUserRepo;


    @Override
    public String saveVendor(VendorDto request) {
        Vendor vendor = new Vendor();
        vendor.setVendorName(request.getVendorName());
        vendor.setVendorId(request.getVendorId());
        vendor.setCountryCode(request.getCountryCode());
        vendor.setMobileNumber(request.getMobileNumber());
        vendor.setPOC(request.getPOC());
        if (request.getSiteIdList() != null) {
            List<Site> siteList = siteRepo.findAllById(request.getSiteIdList());
            if (!siteList.isEmpty()) {
                vendor.setSite(siteList);
            }
        }
        vendor.setSearchKey(getVendorSearchKey(vendor));
        vendorRepo.save(vendor);
        return "vendor creation success";
    }

    @Override
    public List<Vendor> getAllVendor() {
        List<Vendor> vendorList = vendorRepo.findAll();
        if (!vendorList.isEmpty()) {
            return vendorList.stream().filter(obj -> obj.getIsActive().equals(Boolean.TRUE)).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    @Override
    public String deleteVendorById(Long id) {
        Optional<Vendor> vendorEntityId = vendorRepo.findById(id);
        Vendor vendor;
        if (vendorEntityId.isPresent()) {
            vendor = vendorEntityId.get();
            vendor.setIsActive(Boolean.FALSE);
            vendor.setIsDeleted(Boolean.TRUE);
            vendorRepo.save(vendor);
            return "deleted record successfully";
        }
        return "this id is not in the database";
    }

    @Override
    public Vendor getVendorById(Long id) {
        if (Validator.isValid(id)) {
            Optional<Vendor> optionalVendor = vendorRepo.findById(id);
            if (optionalVendor.isPresent() && optionalVendor.get().getIsActive().equals(Boolean.TRUE)) {
                return optionalVendor.get();
            }
        }
        return new Vendor();
    }

    @Override

    public String updateVendor(VendorDto request) {
        Vendor vendor = null;
        if (Validator.isValid(request.getId())) {
            Optional<Vendor> optionalVendor = vendorRepo.findById(request.getId());
            if (optionalVendor.isPresent()) {
                vendor = optionalVendor.get();
                if (Validator.isValid(request.getVendorName())) {
                    vendor.setVendorName(request.getVendorName());
                }
                if (Validator.isValid(request.getVendorId())) {
                    vendor.setVendorId(request.getVendorId());
                }
                if (Validator.isValid(request.getCountryCode())) {
                    vendor.setCountryCode(request.getCountryCode());
                }
                if (Validator.isValid(request.getMobileNumber())) {
                    vendor.setMobileNumber(request.getMobileNumber());
                }
                if (Validator.isValid(request.getPOC())) {
                    vendor.setPOC(request.getPOC());
                }
                if (request.getSiteIdList() != null) {
                    List<Site> siteList = siteRepo.findAllById(request.getSiteIdList());
                    if (!siteList.isEmpty()) {
                        vendor.setSite(siteList);
                    }
                }
                vendor.setSearchKey(getVendorSearchKey(vendor));
                vendorRepo.save(vendor);
                return "Vendor updated successfully";
            }
        }
        return "This vendor id is not present in database";
    }


    @Override
    public Page<Vendor> getAllVendorPagination(VendorDto response) {
        Pageable pageable = null;
        if (Validator.isValid(response.getPageSize())) {
            pageable = PageRequest.of(response.getPageNumber(), response.getPageSize());
        }
        if (response.getSortVendorName() != null && response.getSortVendorName().equals(Boolean.TRUE)) {
            pageable = PageRequest.of(response.getPageNumber(), response.getPageSize(), Sort.Direction.ASC, "vendorName");
        } else if (response.getSortVendorName() != null && response.getSortVendorName().equals(Boolean.FALSE)) {
            pageable = PageRequest.of(response.getPageNumber(), response.getPageSize(), Sort.Direction.DESC, "vendorName");
        }
        //filters
        Specification<Vendor> addVendorSpecification = ((root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> addVendorPredicate = new ArrayList<>();
            Join<Vendor, Site> siteJoin = root.join("site");
            if (response.getVendorName() != null) {
                Predicate name = criteriaBuilder.and(root.get("userName").in(response.getVendorName()));
                addVendorPredicate.add(name);
            }
            if (response.getPOC() != null && !response.getPOC().isEmpty()) {
                Predicate poc = criteriaBuilder.and(root.get("POC").in(response.getPOC()));
                addVendorPredicate.add(poc);
            }
            if (response.getSiteIdList() != null && !response.getSiteIdList().isEmpty()) {
                Predicate site = criteriaBuilder.and(siteJoin.get("id").in(response.getSiteIdList()));
                addVendorPredicate.add(site);
            }
            if (Validator.isValid(response.getSearchKey())) {
                Predicate searchPredicate = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("searchKey")),
                        "%" + response.getSearchKey().toLowerCase() + "%");
                addVendorPredicate.add(searchPredicate);
            }
            return criteriaBuilder.and(addVendorPredicate.toArray(new Predicate[0]));
        });
        Page<Vendor> vendorObjectives = vendorRepo.findAll(addVendorSpecification, pageable);
        List<Vendor> vendorList = null;
        if (vendorObjectives.getContent() != null) {
            return new org.springframework.data.domain.PageImpl<>(vendorObjectives.getContent(), pageable, 0l);
        }
        return new org.springframework.data.domain.PageImpl<>(new ArrayList<>(), pageable, 0l);
    }

    public String getVendorSearchKey(Vendor vendor) {
        String searchKey = "";
        if (vendor.getPOC() != null) {
            searchKey = searchKey + " " + vendor.getPOC();
        }
        if (vendor.getVendorName() != null) {
            searchKey = searchKey + " " + vendor.getVendorName();
        }
        if (vendor.getVendorId() != null) {
            searchKey = searchKey + " " + vendor.getVendorId();
        }
        if (vendor.getCountryCode() != null) {
            searchKey = searchKey + " " + vendor.getCountryCode();
        }
        if (vendor.getMobileNumber() != null) {
            searchKey = searchKey + " " + vendor.getMobileNumber();
        }
        if (vendor.getIsActive() != null) {
            searchKey = searchKey + " " + vendor.getIsActive();
        }
        if (vendor.getSite() != null) {
            searchKey = searchKey + " " + vendor.getSite();
        }
        return searchKey;
    }
}