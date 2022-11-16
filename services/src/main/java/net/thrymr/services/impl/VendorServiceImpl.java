package net.thrymr.services.impl;

import net.thrymr.dto.VendorDto;
import net.thrymr.enums.Roles;
import net.thrymr.model.AppUser;
import net.thrymr.model.master.MtSite;
import net.thrymr.model.master.MtVendor;
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
        MtVendor mtVendor = new MtVendor();
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
            Optional<MtSite> optionalSite = siteRepo.findById(request.getSiteId());
            if (optionalSite.isPresent()) {
                user.setMtSite(optionalSite.get());
            }

        }
        appUserRepo.save(user);
        if (Validator.isValid(request.getPOC())) {
            mtVendor.setPOC(request.getPOC());
        }
        mtVendor.setAppUser(user);

        vendorRepo.save(mtVendor);
        return "vendor creation success";
    }

    @Override
    public List<MtVendor> getAllVendor() {
        return vendorRepo.findAll();
    }

    @Override
    public String deleteVendorById(Long id) {
        Optional<MtVendor> vendorEntityId = vendorRepo.findById(id);
        MtVendor mtVendor;
        if (vendorEntityId.isPresent()) {
            mtVendor = vendorEntityId.get();
            mtVendor.setIsActive(Boolean.FALSE);
            mtVendor.setIsDeleted(Boolean.TRUE);
            vendorRepo.save(mtVendor);
            return "deleted record successfully";
        }
        return "this id is not in the database";
    }

    @Override
    public MtVendor getVendorById(Long id) {
        return vendorRepo.findById(id).orElse(null);
    }

    @Override

    public String updateVendor(VendorDto request) {
        MtVendor mtVendor;
        AppUser user = null;
        if (Validator.isValid(request.getId())) {
            Optional<MtVendor> optionalVendor = vendorRepo.findById(request.getId());
            if (optionalVendor.isPresent()) {
                mtVendor = optionalVendor.get();
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
                    Optional<MtSite> optionalSite = siteRepo.findById(request.getSiteId());
                    if (optionalSite.isPresent()) {
                        user.setMtSite(optionalSite.get());
                    }
                }
                if (Validator.isObjectValid(user)) {
                    mtVendor.setAppUser(user);
                }
                if (Validator.isValid(request.getPOC())) {
                    mtVendor.setPOC(request.getPOC());
                }
                vendorRepo.save(mtVendor);
                return "Vendor updated successfully";
            }
        }
        return "No data found";
    }

    @Override
    public List<MtVendor> getAllVendorPagination(VendorDto response) {
        Pageable pageable = null;
        if (Validator.isValid(response.getPageSize())) {
            System.out.println("Entered");
            pageable = PageRequest.of(response.getPageNumber(), response.getPageSize());
        }
        if (Validator.isValid(response.getAddedOn())) {
            pageable = PageRequest.of(response.getPageNumber(), response.getPageSize(), Sort.Direction.DESC, "createdOn");
        }
        //filters
        Specification<MtVendor> addVendorSpecification = ((root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> addVendorPredicate = new ArrayList<>();
            if (response.getName() != null) {
                Predicate name = criteriaBuilder.and(root.get("userName").in(response.getName()));
                addVendorPredicate.add(name);
            }
            if (response.getPOC() != null && !response.getPOC().isEmpty()) {
                Predicate poc = criteriaBuilder.and(root.get("POC").in(response.getPOC()));
                addVendorPredicate.add(poc);
            }
            if (response.getSite() != null) {
                Predicate site = criteriaBuilder.and(root.get("site").in(response.getSite()));
                addVendorPredicate.add(site);
            }

            return criteriaBuilder.and(addVendorPredicate.toArray(new Predicate[0]));
        });
        Page<MtVendor> vendorObjectives = vendorRepo.findAll(addVendorSpecification, pageable);
        List<MtVendor> mtVendorList = new ArrayList<>();

        if (vendorObjectives.getContent() != null) {
            mtVendorList = vendorObjectives.stream().toList();
        }

        return mtVendorList;
    }


}


