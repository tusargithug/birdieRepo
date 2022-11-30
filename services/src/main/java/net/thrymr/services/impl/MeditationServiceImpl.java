package net.thrymr.services.impl;

import net.thrymr.dto.MeditationDto;
import net.thrymr.dto.response.PaginationResponse;
import net.thrymr.model.FileDetails;
import net.thrymr.model.FileEntity;
import net.thrymr.model.Site;
import net.thrymr.model.Vendor;
import net.thrymr.model.master.MtMeditation;
import net.thrymr.repository.FileRepo;
import net.thrymr.repository.MeditationRepo;
import net.thrymr.services.MeditationService;
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
public class MeditationServiceImpl implements MeditationService {

    @Autowired
    private MeditationRepo meditationRepo;
    @Autowired
    FileRepo fileRepo;

    @Override
    public List<MtMeditation> getAllMeditation() {

        List<MtMeditation> meditationList = meditationRepo.findAll();

        if (!meditationList.isEmpty()) {
            return meditationList.stream().filter(obj -> obj.getIsActive().equals(Boolean.TRUE)).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    @Override
    public MtMeditation getMeditationById(Long id) {

        if (Validator.isValid(id)) {
            Optional<MtMeditation> optionalMtMeditation = meditationRepo.findById(id);
            if (optionalMtMeditation.isPresent() && optionalMtMeditation.get().getIsActive().equals(Boolean.TRUE)) {
                return optionalMtMeditation.get();
            }
        }
        return new MtMeditation();
    }

    @Override
    public String createMeditation(MeditationDto request) {
        meditationRepo.save(dtoToMeditationEntity(request));
        return "create Meditation successfully";
    }

    @Override
    public String updateMeditationById(MeditationDto request) {

        MtMeditation mtMeditation = null;
        if (Validator.isValid(request.getId())) {
            Optional<MtMeditation> optionalMtMeditation = meditationRepo.findById(request.getId());
            if (optionalMtMeditation.isPresent()) {
                mtMeditation = optionalMtMeditation.get();
                if (Validator.isValid(request.getName())) {
                    mtMeditation.setName(request.getName());
                }
                if (Validator.isValid(request.getMeditationVideoLink())) {
                    mtMeditation.setMeditationVideoLink(request.getMeditationVideoLink());
                }
                if (request.getIsActive() != null) {
                    mtMeditation.setIsActive(request.getIsActive());
                }
                if(Validator.isValid(request.getFileId())){
                    Optional<FileEntity> fileEntity=fileRepo.findByFileId(request.getFileId());
                    if(fileEntity.isPresent()){
                        mtMeditation.setFile(fileEntity.get());
                    }
                }
                meditationRepo.save(mtMeditation);
                return "Meditation updated successfully";
            }
        }

        return "Meditation id not found";
    }

    @Override
    public String deleteMeditationById(Long id) {
        MtMeditation mtMeditation = null;
        if (Validator.isValid(id)) {
            Optional<MtMeditation> optionalMtMeditation = meditationRepo.findById(id);
            if(optionalMtMeditation.isPresent()) {
                mtMeditation = optionalMtMeditation.get();
                mtMeditation.setIsActive(Boolean.FALSE);
                mtMeditation.setIsDeleted(Boolean.TRUE);
                meditationRepo.save(mtMeditation);
                return "Meditation deleted successfully";

            }
        }
        return "Meditation id not found";
    }

    @Override
    public PaginationResponse getAllMeditationPagination(MeditationDto response) {
        Pageable pageable = null;
        if ( Validator.isValid(response.getPageSize())) {
            pageable = PageRequest.of(response.getPageNumber(), response.getPageSize());
        }
        if (response.getSortName() != null && response.getSortName().equals(Boolean.TRUE)) {
            pageable=PageRequest.of(response.getPageNumber(), response.getPageSize(), Sort.Direction.ASC, "name");
        } else if (response.getSortName() != null && response.getSortName().equals(Boolean.FALSE)) {
            pageable=PageRequest.of(response.getPageNumber(), response.getPageSize(), Sort.Direction.DESC, "name");
        }
        Specification<MtMeditation> meditationSpecification = ((root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> addSitePredicate = new ArrayList<>();
            Join<MtMeditation, FileEntity> fileJoin = root.join("file");
            if (Validator.isValid(response.getName())) {
                Predicate name = criteriaBuilder.and(root.get("name").in(response.getName()));
                addSitePredicate.add(name);
            }
            if (Validator.isValid(response.getCreatedOn())) {
                Predicate siteName = criteriaBuilder.and(root.get("createdOn").in(response.getCreatedOn()));
                addSitePredicate.add(siteName);
            }
            if (response.getIsActive() != null && response.getIsActive().equals(Boolean.TRUE)) {
                Predicate isActive = criteriaBuilder.and(root.get("isActive").in(response.getIsActive()));
                addSitePredicate.add(isActive);
            } else if (response.getIsActive() != null && response.getIsActive().equals(Boolean.FALSE)) {
                Predicate isActive = criteriaBuilder.and(root.get("isActive").in(response.getIsActive()));
                addSitePredicate.add(isActive);
            }
            if (response.getFileType() != null) {
                Predicate file = criteriaBuilder.and(fileJoin.get("fileType").in(response.getFileType()));
                addSitePredicate.add(file);
            }
            if (Validator.isValid(response.getSearchKey())) {
                Predicate searchPredicate = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("searchKey")),
                        "%" + response.getSearchKey().toLowerCase() + "%");
                addSitePredicate.add(searchPredicate);
            }
            return criteriaBuilder.and(addSitePredicate.toArray(new Predicate[0]));
        });
        Page<MtMeditation> meditationObject = meditationRepo.findAll(meditationSpecification, pageable);
        if(meditationObject != null){
           PaginationResponse paginationResponse=new PaginationResponse();
           paginationResponse.setMeditationList(meditationObject.getContent());
           paginationResponse.setTotalPages(meditationObject.getTotalPages());
           paginationResponse.setTotalElements(meditationObject.getTotalElements());
           return paginationResponse;
        }
        return new PaginationResponse();
    }

    private MtMeditation dtoToMeditationEntity(MeditationDto request) {
        MtMeditation mtMeditation = new MtMeditation();
        mtMeditation.setName(request.getName());
        mtMeditation.setMeditationVideoLink(request.getMeditationVideoLink());
        mtMeditation.setIsActive(request.getIsActive());
        mtMeditation.setSearchKey(saveMeditationSearchKey(mtMeditation));
        if(Validator.isValid(request.getFileId())){
            Optional<FileEntity> fileEntity=fileRepo.findByFileId(request.getFileId());
            if(fileEntity.isPresent()){
                mtMeditation.setFile(fileEntity.get());
            }
        }
        return mtMeditation;
    }

    public String saveMeditationSearchKey(MtMeditation mtMeditation) {
        String searchKey = "";
        if (mtMeditation.getMeditationVideoLink() != null) {
            searchKey = searchKey + " " + mtMeditation.getMeditationVideoLink();
        }
        if (mtMeditation.getName() != null) {
            searchKey = searchKey + " " + mtMeditation.getName();
        }
        if (mtMeditation.getFile() != null) {
            searchKey = searchKey + " " + mtMeditation.getFile();
        }
        if(mtMeditation.getCreatedOn() !=null){
            searchKey = searchKey + " " + mtMeditation.getCreatedOn();
        }
        return searchKey;
    }
}