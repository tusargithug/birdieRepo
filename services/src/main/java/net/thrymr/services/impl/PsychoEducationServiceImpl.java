package net.thrymr.services.impl;

import net.thrymr.dto.PsychoEducationDto;
import net.thrymr.model.FileEntity;
import net.thrymr.model.Site;
import net.thrymr.model.Vendor;
import net.thrymr.model.master.MtPsychoEducation;
import net.thrymr.repository.FileRepo;
import net.thrymr.repository.PsychoEducationRepo;
import net.thrymr.services.PsychoEducationService;
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
public class PsychoEducationServiceImpl implements PsychoEducationService {

    @Autowired
    PsychoEducationRepo psychoEducationRepo;
    @Autowired
    FileRepo fileRepo;


    @Override
    public List<MtPsychoEducation> getAllPsychoEducation() {

        List<MtPsychoEducation> psychoEducationList = psychoEducationRepo.findAll();

        if (!psychoEducationList.isEmpty()) {
            return psychoEducationList.stream().filter(obj -> obj.getIsActive().equals(Boolean.TRUE)).collect(Collectors.toList());
        }

        return new ArrayList<>();
    }

    @Override
    public MtPsychoEducation getPsychoEducationById(Long id) {

        if (Validator.isValid(id)) {
            Optional<MtPsychoEducation> optionalMtPsychoEducation = psychoEducationRepo.findById(id);
            if (optionalMtPsychoEducation.isPresent() && optionalMtPsychoEducation.get().getIsActive().equals(Boolean.TRUE)) {
                return optionalMtPsychoEducation.get();
            }
        }

        return new MtPsychoEducation();
    }

    @Override
    public String createPsychoEducation(PsychoEducationDto request) {

        psychoEducationRepo.save(dtoToPsychoEducation(request));

        return "created psycho education successfully";
    }

    @Override
    public String updatePsychoEducationById(PsychoEducationDto request) {

        MtPsychoEducation mtPsychoEducation = null;

        if (Validator.isValid(request.getId())) {

            Optional<MtPsychoEducation> optionalMtPsychoEducation = psychoEducationRepo.findById(request.getId());
            if (optionalMtPsychoEducation.isPresent()) {
                mtPsychoEducation = optionalMtPsychoEducation.get();
                if (Validator.isValid(request.getName())) {
                    mtPsychoEducation.setName(request.getName());
                }
                if (Validator.isValid(request.getDescription())) {
                    mtPsychoEducation.setDescription(request.getDescription());
                }
                if (request.getIsActive() != null) {
                    mtPsychoEducation.setIsActive(request.getIsActive());
                }
                if (Validator.isValid(request.getFileId())) {
                    Optional<FileEntity> fileEntity = fileRepo.findByFileId(request.getFileId());
                    if (fileEntity.isPresent()) {
                        mtPsychoEducation.setFile(fileEntity.get());
                    }
                }
                mtPsychoEducation.setSearchKey(getAppUserSearchKey(mtPsychoEducation));
                psychoEducationRepo.save(mtPsychoEducation);
                return "Psycho Education updated successfully";
            }
        }
        return "Psycho education id not found";
    }

    @Override
    public String deletePsychoEducationById(Long id) {

        MtPsychoEducation mtPsychoEducation = null;
        if (Validator.isValid(id)) {
            Optional<MtPsychoEducation> optionalMtPsychoEducation = psychoEducationRepo.findById(id);
            if (optionalMtPsychoEducation.isPresent()) {
                mtPsychoEducation = optionalMtPsychoEducation.get();
                mtPsychoEducation.setIsActive(Boolean.FALSE);
                mtPsychoEducation.setIsDeleted(Boolean.TRUE);
                psychoEducationRepo.save(mtPsychoEducation);
                return "Psycho education deleted successfully";
            }
        }
        return "Psycho education id not found";
    }

    @Override
    public Page<MtPsychoEducation> getPaginationPsychoEducation(PsychoEducationDto request) {

        Pageable pageable = null;
        if (Validator.isValid(request.getPageSize())) {
            pageable = PageRequest.of(request.getPageNumber(), request.getPageSize());
        }

        if (request.getSortPsychoEducationName() != null && request.getSortPsychoEducationName().equals(Boolean.TRUE)) {
            pageable = PageRequest.of(request.getPageNumber(), request.getPageSize(), Sort.Direction.ASC, "name");
        } else if (request.getSortPsychoEducationName() != null && request.getSortPsychoEducationName().equals(Boolean.FALSE)) {
            pageable = PageRequest.of(request.getPageNumber(), request.getPageSize(), Sort.Direction.DESC, "name");
        }

        Specification<MtPsychoEducation> addPsychoEducationSpecification = ((root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> addPsyEducationPredicate = new ArrayList<>();
            Join<MtPsychoEducation, FileEntity> fileJoin = root.join("file");
            if (request.getName() != null) {
                Predicate name = criteriaBuilder.and(root.get("name").in(request.getName()));
                addPsyEducationPredicate.add(name);
            }

            if (request.getFileId()!=null && !request.getFileId().isEmpty()) {
                Predicate file = criteriaBuilder.and(fileJoin.get("fileType").in(request.getFileId()));
                addPsyEducationPredicate.add(file);
            }
            return criteriaBuilder.and(addPsyEducationPredicate.toArray(new Predicate[0]));
        });

        Page<MtPsychoEducation> psychoEducationPage = psychoEducationRepo.findAll(addPsychoEducationSpecification, pageable);

        if (psychoEducationPage.getContent() != null) {
            return new org.springframework.data.domain.PageImpl<>(psychoEducationPage.getContent(), pageable, 0l);
        }
        return new org.springframework.data.domain.PageImpl<>(new ArrayList<>(), pageable, 0l);

    }

    private MtPsychoEducation dtoToPsychoEducation(PsychoEducationDto request) {
        MtPsychoEducation mtPsychoEducation = new MtPsychoEducation();
        mtPsychoEducation.setName(request.getName());
        mtPsychoEducation.setDescription(request.getDescription());
        mtPsychoEducation.setIsActive(request.getIsActive());
        if (Validator.isValid(request.getFileId())) {
            Optional<FileEntity> fileEntity = fileRepo.findByFileId(request.getFileId());
            if (fileEntity.isPresent()) {
                mtPsychoEducation.setFile(fileEntity.get());
            }
        }
        mtPsychoEducation.setSearchKey(getAppUserSearchKey(mtPsychoEducation));
        return mtPsychoEducation;
    }

    public String getAppUserSearchKey(MtPsychoEducation psychoEducation) {
        String searchKey = "";
        if (psychoEducation.getName() != null) {
            searchKey = searchKey + " " + psychoEducation.getName();
        }
        if (psychoEducation.getDescription() != null) {
            searchKey = searchKey + " " + psychoEducation.getDescription();
        }
        if (psychoEducation.getFile() != null) {
            searchKey = searchKey + " " + psychoEducation.getFile();
        }
        return searchKey;
    }
}
