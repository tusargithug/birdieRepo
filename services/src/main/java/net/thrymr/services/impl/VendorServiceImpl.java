package net.thrymr.services.impl;

import net.thrymr.dto.VendorDto;
import net.thrymr.dto.response.PaginationResponse;
import net.thrymr.model.AppUser;
import net.thrymr.model.Site;
import net.thrymr.model.Vendor;
import net.thrymr.model.VendorSite;
import net.thrymr.repository.SiteRepo;
import net.thrymr.repository.VendorRepo;
import net.thrymr.repository.VendorSiteRepo;
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
import java.util.HashSet;
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
    VendorSiteRepo vendorSiteRepo;


    @Override
    public String saveVendor(VendorDto request) {
        Vendor vendor = new Vendor();
        vendor.setVendorName(request.getVendorName());
        vendor.setVendorId(request.getVendorId());
        vendor.setCountryCode(request.getCountryCode());
        vendor.setMobileNumber(request.getMobileNumber());
        vendor.setEmail(request.getEmail());
        vendor.setPOC(request.getPOC());
        vendor.setSearchKey(getVendorSearchKey(vendor));
        vendor = vendorRepo.save(vendor);
        if (request.getSiteIdList() != null && vendor.getId() != null) {
            List<Site> siteList = siteRepo.findAllByIdInAndIsActiveAndIsDeleted(request.getSiteIdList(), Boolean.TRUE, Boolean.FALSE);
            if (siteList != null) {
                for (Site site : siteList) {
                    VendorSite vendorSite = new VendorSite();
                    vendorSite.setVendor(vendor);
                    vendorSite.setSite(site);
                    vendorSite.setSearchKey(getVendorSiteSearchKey(vendorSite));
                    vendorSiteRepo.save(vendorSite);
                }
            }
            return "vendor creation success";
        } else {
            return "Please select site";
        }
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
        Vendor vendor = null;
        if (vendorEntityId.isPresent()) {
            vendor = vendorEntityId.get();
            vendor.setIsActive(Boolean.FALSE);
            vendor.setIsDeleted(Boolean.TRUE);
            vendorRepo.save(vendor);
            List<VendorSite> vendorSiteList = vendorSiteRepo.findAllByVendorId(id);
            for (VendorSite vendorSite : vendorSiteList) {
                vendorSite.setIsActive(Boolean.FALSE);
                vendorSite.setIsDeleted(Boolean.TRUE);
                vendorSiteRepo.save(vendorSite);
            }
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
        Vendor vendor;
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
                if (Validator.isValid(request.getEmail())) {
                    vendor.setEmail(request.getEmail());
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
                vendor.setSearchKey(getVendorSearchKey(vendor));
                vendor = vendorRepo.save(vendor);

                if (request.getSiteIdList() != null && vendor.getId() != null) {
                    List<VendorSite> vendorSiteList = vendorSiteRepo.findAllByVendorId(vendor.getId());
                    for (Long newSiteIds : request.getSiteIdList()) {
                        VendorSite saveVendorSite = null;
                        if (!vendorSiteList.isEmpty()) {
                            for (VendorSite vendorSite : vendorSiteList) {
                                if (vendorSite.getSite() != null && vendorSiteRepo.existsByVendorIdAndSiteId(vendor.getId(), newSiteIds)) {
                                    saveVendorSite = vendorSite;
                                    saveVendorSite.setSite(vendorSite.getSite());
                                    saveVendorSite.setVendor(vendor);
                                } else {
                                    Optional<Site> optionalSite = siteRepo.findById(newSiteIds);
                                    if (optionalVendor.isPresent()) {
                                        saveVendorSite = new VendorSite();
                                        saveVendorSite.setVendor(vendor);
                                        saveVendorSite.setSite(optionalSite.get());
                                    }

                                }
                                if (Validator.isObjectValid(saveVendorSite)) {
                                    vendorSiteRepo.save(saveVendorSite);
                                }
                            }
                        } else {
                            Optional<Site> optionalSite = siteRepo.findById(newSiteIds);
                            if (optionalVendor.isPresent()) {
                                saveVendorSite = new VendorSite();
                                saveVendorSite.setVendor(vendor);
                                saveVendorSite.setSite(optionalSite.get());
                                if (Validator.isObjectValid(saveVendorSite)) {
                                    vendorSiteRepo.save(saveVendorSite);
                                }

                            }
                        }
                    }
                    return "vendor updated success";
                }
            }
        }
        return "This vendor id is not present in database";
    }


    @Override
    public PaginationResponse getAllVendorPagination(VendorDto response) {
        Pageable pageable = null;
        if (Validator.isValid(response.getPageSize())) {
            pageable = PageRequest.of(response.getPageNumber(), response.getPageSize());
        }
        if (response.getPageSize() != null && response.getPageNumber() != null) {
            pageable = PageRequest.of(response.getPageNumber(), response.getPageSize(), Sort.Direction.DESC, "createdOn");
        }
        if (response.getSortVendorName() != null && response.getSortVendorName().equals(Boolean.TRUE)) {
            pageable = PageRequest.of(response.getPageNumber(), response.getPageSize(), Sort.Direction.ASC, "vendor.vendorName");
        } else if (response.getSortVendorName() != null && response.getSortVendorName().equals(Boolean.FALSE)) {
            pageable = PageRequest.of(response.getPageNumber(), response.getPageSize(), Sort.Direction.DESC, "vendor.vendorName");
        }
        //filters
        Specification<VendorSite> addVendorSpecification = ((root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> addVendorPredicate = new ArrayList<>();
            Join<VendorSite, Site> siteJoin = root.join("site");
            Join<VendorSite, Vendor> vendorJoin = root.join("vendor");

            if (response.getVendorName() != null) {
                Predicate name = criteriaBuilder.and(vendorJoin.get("vendorName").in(response.getVendorName()));
                addVendorPredicate.add(name);
            }
            if (response.getVendorId() != null) {
                Predicate vendorId = criteriaBuilder.and(vendorJoin.get("vendorId").in(response.getVendorId()));
                addVendorPredicate.add(vendorId);
            }
            if (response.getPOC() != null && !response.getPOC().isEmpty()) {
                Predicate poc = criteriaBuilder.and(vendorJoin.get("POC").in(response.getPOC()));
                addVendorPredicate.add(poc);
            }
            if (response.getSiteIdList() != null && !response.getSiteIdList().isEmpty()) {
                Predicate site = criteriaBuilder.and(siteJoin.get("id").in(response.getSiteIdList()));
                addVendorPredicate.add(site);
            }
            if (response.getEmail() != null) {
                Predicate email = criteriaBuilder.and(vendorJoin.get("email").in(response.getEmail()));
                addVendorPredicate.add(email);
            }
            if (response.getMobileNumber() != null) {
                Predicate mobile = criteriaBuilder.and(vendorJoin.get("mobileNumber").in(response.getMobileNumber()));
                addVendorPredicate.add(mobile);
            }
            if (Validator.isValid(response.getSearchKey())) {
                Predicate searchPredicate = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("searchKey")),
                        "%" + response.getSearchKey().toLowerCase() + "%");
                addVendorPredicate.add(searchPredicate);
            }
            Predicate isDeletedPredicate = criteriaBuilder.equal(root.get("isDeleted"), Boolean.FALSE);
            addVendorPredicate.add(isDeletedPredicate);
            return criteriaBuilder.and(addVendorPredicate.toArray(new Predicate[0]));
        });
        PaginationResponse paginationResponse = new PaginationResponse();
        if (response.getPageSize() != null && response.getPageNumber() != null) {
            Page<VendorSite> vendorObjectives = vendorSiteRepo.findAll(addVendorSpecification, pageable);
            if (vendorObjectives.getContent() != null) {
                paginationResponse.setVendorSiteList(new HashSet<>(vendorObjectives.getContent()));
                paginationResponse.setTotalElements(vendorObjectives.getTotalElements());
                paginationResponse.setTotalPages(vendorObjectives.getTotalPages());
                return paginationResponse;
            }
        } else {
            List<VendorSite> vendorSiteList = vendorSiteRepo.findAll(addVendorSpecification);
            paginationResponse.setVendorSiteList(vendorSiteList.stream().filter(vendorSite -> vendorSite.getIsDeleted().equals(Boolean.FALSE)).collect(Collectors.toSet()));
            return paginationResponse;
        }
        return new PaginationResponse();
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
        if (vendor.getEmail() != null) {
            searchKey = searchKey + " " + vendor.getEmail();

        }
        return searchKey;
    }

    public String getVendorSiteSearchKey(VendorSite vendorSite) {
        String searchKey = "";
        if (vendorSite.getVendor().getPOC() != null) {
            searchKey = searchKey + " " + vendorSite.getVendor().getPOC();
        }
        if (vendorSite.getVendor().getVendorName() != null) {
            searchKey = searchKey + " " + vendorSite.getVendor().getVendorName();
        }
        if (vendorSite.getVendor().getVendorId() != null) {
            searchKey = searchKey + " " + vendorSite.getVendor().getVendorId();
        }
        if (vendorSite.getVendor().getCountryCode() != null) {
            searchKey = searchKey + " " + vendorSite.getVendor().getCountryCode();
        }
        if (vendorSite.getVendor().getMobileNumber() != null) {
            searchKey = searchKey + " " + vendorSite.getVendor().getMobileNumber();
        }
        if (vendorSite.getVendor().getIsActive() != null) {
            searchKey = searchKey + " " + vendorSite.getVendor().getIsActive();
        }
        if (vendorSite.getVendor().getEmail() != null) {
            searchKey = searchKey + " " + vendorSite.getVendor().getEmail();
        }
        if (vendorSite.getSite().getSiteName() != null) {
            searchKey = searchKey + " " + vendorSite.getSite().getSiteName();
        }
        return searchKey;
    }

}