package net.thrymr.services.impl;

import net.thrymr.dto.VendorDto;
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
        if(request.getSiteIdList() != null){
            List<Site> siteList=siteRepo.findAllById(request.getSiteIdList());
            if(!siteList.isEmpty()){
                vendor.setSite(siteList);
            }
        }
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
        if(Validator.isValid(request.getId())) {
            Optional<Vendor> optionalVendor = vendorRepo.findById(request.getId());
            if (optionalVendor.isPresent()) {
                vendor=optionalVendor.get();
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
                if(request.getSiteIdList() != null){
                    List<Site> siteList=siteRepo.findAllById(request.getSiteIdList());
                    if(!siteList.isEmpty()){
                        vendor.setSite(siteList);
                    }
                }
                vendorRepo.save(vendor);
                return "Vendor updated successfully";
            }
        }
        return "This vendor id is not present in database";
    }


    @Override
    public List<Vendor> getAllVendorPagination(VendorDto response) {
        Pageable pageable = null;
        if (Validator.isValid(response.getPageSize())) {
            pageable = PageRequest.of(response.getPageNumber(), response.getPageSize());
        }
        if (response.getSortVendorName() != null && response.getSortVendorName().equals(Boolean.TRUE)) {
            pageable = PageRequest.of(response.getPageNumber(), response.getPageSize(), Sort.Direction.ASC, "vendorName");
        }else if(response.getSortVendorName() != null && response.getSortVendorName().equals(Boolean.FALSE)) {
            pageable = PageRequest.of(response.getPageNumber(), response.getPageSize(), Sort.Direction.DESC, "vendorName");
        }
        //filters
        Specification<Vendor> addVendorSpecification = ((root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> addVendorPredicate = new ArrayList<>();
            if (response.getVendorName() != null) {
                Predicate name = criteriaBuilder.and(root.get("userName").in(response.getVendorName()));
                addVendorPredicate.add(name);
            }
            if (response.getPOC() != null && !response.getPOC().isEmpty()) {
                Predicate poc = criteriaBuilder.and(root.get("POC").in(response.getPOC()));
                addVendorPredicate.add(poc);
            }
            if (!response.getSiteIdList().isEmpty()) {
                Predicate site = criteriaBuilder.and(root.get("site").in(response.getSiteIdList()));
                addVendorPredicate.add(site);
            }

            return criteriaBuilder.and(addVendorPredicate.toArray(new Predicate[0]));
        });
        Page<Vendor> vendorObjectives = vendorRepo.findAll(addVendorSpecification, pageable);
        List<Vendor> vendorList = null;
        if (vendorObjectives.getContent() != null) {
            vendorList = vendorObjectives.stream().filter(obj -> obj.getIsActive().equals(Boolean.TRUE)).toList();
        }
        return vendorList;
    }
}