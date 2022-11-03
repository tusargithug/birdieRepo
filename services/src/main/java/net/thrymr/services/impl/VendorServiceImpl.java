package net.thrymr.services.impl;

import net.thrymr.dto.VendorDto;
import net.thrymr.enums.Roles;
import net.thrymr.model.AppUser;
import net.thrymr.model.Site;
import net.thrymr.model.Vendor;
import net.thrymr.repository.AppUserRepo;
import net.thrymr.repository.SiteRepo;
import net.thrymr.repository.VendorRepo;
import net.thrymr.services.VendorService;
import net.thrymr.utils.Validator;
import org.springframework.core.env.Environment;
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

@Service
public class VendorServiceImpl implements VendorService {
    private final VendorRepo vendorRepo;
    private final SiteRepo siteRepo;

    private final AppUserRepo appUserRepo;

    private final Environment environment;

    public VendorServiceImpl(VendorRepo vendorRepo, SiteRepo siteRepo, AppUserRepo appUserRepo, Environment environment) {
        this.vendorRepo = vendorRepo;
        this.siteRepo = siteRepo;
        this.appUserRepo = appUserRepo;
        this.environment = environment;
    }

    @Override
    public String saveVendor(VendorDto request) {
        Vendor vendor = new Vendor();
        AppUser user = new AppUser();

        if (Validator.isValid(request.getName())) {
            user.setUserName(request.getName());
            user.setRoles(Roles.VENDOR);
        }
        if (Validator.isValid(request.getEmpId())) {
            user.setEmpId(request.getEmpId());
        }
        if (Validator.isValid(request.getEmail())) {
            user.setEmail(request.getEmail());
        }
        if (Validator.isValid(request.getMobile())) {
            user.setMobile(request.getMobile());
        }
        if (Validator.isValid(request.getSiteId())) {
            Optional<Site> optionalSite = siteRepo.findById(request.getSiteId());
            if (optionalSite.isPresent()) {
                user.setSite(optionalSite.get());
            }

        }
        appUserRepo.save(user);
        if (Validator.isValid(request.getPOC())) {
            vendor.setPOC(request.getPOC());
        }
        vendor.setAppUser(user);

        vendorRepo.save(vendor);
        return "vendor creation success";
    }

    @Override
    public List<Vendor> getAllVendor() {
        return vendorRepo.findAll();
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
        return vendorRepo.findById(id).orElse(null);
    }

    @Override

    public String updateVendor(VendorDto request) {
        Vendor vendor;
        AppUser user=null;
        if (Validator.isValid(request.getId())) {
            Optional<Vendor> optionalVendor = vendorRepo.findById(request.getId());
            if (optionalVendor.isPresent()) {
                vendor = optionalVendor.get();
                if (Validator.isValid(request.getName())) {
                    user.setUserName(request.getName());
                }
                if (Validator.isValid(request.getRole())) {
                    user.setRoles(Roles.VENDOR);
                }
                if (Validator.isValid(request.getEmpId())) {
                    user.setEmpId(request.getEmpId());
                }
                if (Validator.isValid(request.getEmail())) {
                    user.setEmail(request.getEmail());
                }
                if (Validator.isValid(request.getMobile())) {
                    user.setMobile(request.getMobile());
                }
                if (Validator.isValid(request.getSiteId())) {
                    Optional<Site> optionalSite = siteRepo.findById(request.getSiteId());
                    if (optionalSite.isPresent()) {
                        user.setSite(optionalSite.get());
                    }
                }
                if (Validator.isObjectValid(user)) {
                    vendor.setAppUser(user);
                }
                if (Validator.isValid(request.getPOC())) {
                    vendor.setPOC(request.getPOC());
                }
                vendorRepo.save(vendor);
                return "Vendor updated successfully";
            }
        }
        return "No data found";
    }

    @Override
    public List<Vendor> getAllVendorPagination(VendorDto response) {
        Pageable pageable = null;
        if (Validator.isValid(response.getPageSize())) {
            System.out.println("Entered");
            pageable = PageRequest.of(response.getPageNumber(), response.getPageSize());
        }
        if (Validator.isValid(response.getAddedOn())) {
            pageable = PageRequest.of(response.getPageNumber(),response.getPageSize(),Sort.Direction.DESC,"createdOn");
        }
        //filters
        Specification<Vendor> addVendorSpecification = ((root, criteriaQuery, criteriaBuilder)->{
            List<Predicate> addVendorPredicate = new ArrayList<>();
            if(response.getName()!=null){
                Predicate name = criteriaBuilder.and(root.get("userName").in(response.getName()));
                addVendorPredicate.add(name);
            }
            if(response.getPOC()!=null && !response.getPOC().isEmpty()){
                Predicate poc = criteriaBuilder.and(root.get("POC").in(response.getPOC()));
                addVendorPredicate.add(poc);
            }
            if(response.getSite()!=null){
                Predicate site = criteriaBuilder.and(root.get("site").in(response.getSite()));
                addVendorPredicate.add(site);
            }

            return criteriaBuilder.and(addVendorPredicate.toArray(new Predicate[0]));
        });
        Page <Vendor> vendorObjectives = vendorRepo.findAll(addVendorSpecification,pageable);
        List<Vendor> vendorList = new ArrayList<>();

        if(vendorObjectives.getContent()!=null){
            vendorList = vendorObjectives.stream().toList();
        }

        return  vendorList;
    }



}


