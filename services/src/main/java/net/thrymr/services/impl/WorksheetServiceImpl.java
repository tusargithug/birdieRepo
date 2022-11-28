package net.thrymr.services.impl;

import net.thrymr.dto.WorksheetDto;
import net.thrymr.dto.response.PaginationResponse;
import net.thrymr.model.FileEntity;
import net.thrymr.model.master.MtMeditation;
import net.thrymr.model.master.MtWorksheet;
import net.thrymr.repository.FileRepo;
import net.thrymr.repository.WorksheetRepo;
import net.thrymr.services.WorksheetService;
import net.thrymr.utils.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Join;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.persistence.criteria.Predicate;
import java.util.stream.Collectors;

@Service
public class WorksheetServiceImpl implements WorksheetService {

    @Autowired
    WorksheetRepo worksheetRepo;
    @Autowired
    FileRepo fileRepo;


    @Override
    public List<MtWorksheet> getAllWorksheet() {

        List<MtWorksheet> worksheetList = worksheetRepo.findAll();
        if (!worksheetList.isEmpty()) {
            return worksheetList.stream().filter(obj -> obj.getIsActive().equals(Boolean.TRUE)).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    @Override
    public MtWorksheet getWorksheetById(Long id) {

        if (Validator.isValid(id)) {
            Optional<MtWorksheet> optionalMtWorksheet = worksheetRepo.findById(id);
            if (optionalMtWorksheet.isPresent() && optionalMtWorksheet.get().getIsActive().equals(Boolean.TRUE)) {
                return optionalMtWorksheet.get();
            }
        }
        return new MtWorksheet();
    }

    @Override
    public String createWorksheet(WorksheetDto request) {
        worksheetRepo.save(dtoToWorksheetEntity(request));
        return "create Worksheet successfully";
    }

    @Override
    public String updateWorksheetById(WorksheetDto request) {

        MtWorksheet mtWorksheet = null;

        if (Validator.isValid(request.getId())) {
            Optional<MtWorksheet> optionalMtWorksheet = worksheetRepo.findById(request.getId());
            if (optionalMtWorksheet.isPresent()) {
                mtWorksheet = optionalMtWorksheet.get();
                if (Validator.isValid(request.getName())) {
                    mtWorksheet.setName(request.getName());
                }
                if (Validator.isValid(request.getDescription())) {
                    mtWorksheet.setDescription(request.getDescription());
                }
                if (request.getIsActive() != null) {
                    mtWorksheet.setIsActive(request.getIsActive());
                }
                if (Validator.isValid(request.getFileId())) {
                    Optional<FileEntity> fileEntity = fileRepo.findByFileId(request.getFileId());
                    if (fileEntity.isPresent()) {
                        mtWorksheet.setFile(fileEntity.get());
                    }
                }
                worksheetRepo.save(mtWorksheet);
                return "Worksheet updated successfully";
            }
        }
        return "Worksheet id not found";
    }

    @Override
    public String deleteWorksheetById(Long id) {

        MtWorksheet mtWorksheet = null;

        if (Validator.isValid(id)) {
            Optional<MtWorksheet> optionalMtWorksheet = worksheetRepo.findById(id);
            if (optionalMtWorksheet.isPresent()) {
                mtWorksheet = optionalMtWorksheet.get();
                mtWorksheet.setIsActive(Boolean.FALSE);
                mtWorksheet.setIsDeleted(Boolean.TRUE);
                worksheetRepo.save(mtWorksheet);
                return "Worksheet deleted successfully";
            }
        }
        return "Worksheet id not found";
    }

    @Override
    public PaginationResponse getAllWorkSheetPagination(WorksheetDto response) {
        Pageable pageable = null;
        if (Validator.isValid(response.getPageSize())) {
            pageable = PageRequest.of(response.getPageNumber(), response.getPageSize());
        }
        if (response.getSortName() != null && response.getSortName().equals(Boolean.TRUE)) {
            pageable = PageRequest.of(response.getPageNumber(), response.getPageSize(), Sort.Direction.ASC, "name");
        } else if (response.getSortName() != null && response.getSortName().equals(Boolean.FALSE)) {
            pageable = PageRequest.of(response.getPageNumber(), response.getPageSize(), Sort.Direction.DESC, "name");
        }
        Specification<MtWorksheet> worksheetSpecification=((root, query, criteriaBuilder) -> {
            List<Predicate> addWorkSheetPredicateList=new ArrayList<>();
            Join<MtWorksheet,FileEntity> fileJoin=root.join("file");
            if(Validator.isValid(response.getName())){
                Predicate name = criteriaBuilder.and(root.get("name").in(response.getName()));
                addWorkSheetPredicateList.add(name);
            }
            if(Validator.isValid(response.getCreatedOn())){
                Predicate createdOn=criteriaBuilder.and(root.get("createdOn").in(response.getCreatedOn()));
                addWorkSheetPredicateList.add(createdOn);
            }
            if (Validator.isValid(response.getFileType())) {
                Predicate file=criteriaBuilder.and(fileJoin.get("fileType").in(response.getFileType()));
                addWorkSheetPredicateList.add(file);
            }
            if(response.getIsActive() != null && response.getIsActive().equals(Boolean.TRUE)){
                Predicate isActive=criteriaBuilder.and(root.get("isActive").in(response.getIsActive()));
                addWorkSheetPredicateList.add(isActive);
            } else if (response.getIsActive() != null && response.getIsActive().equals(Boolean.FALSE)) {
                Predicate isActive=criteriaBuilder.and(root.get("isActive").in(response.getIsActive()));
                addWorkSheetPredicateList.add(isActive);
            }
            return criteriaBuilder.and(addWorkSheetPredicateList.toArray(new Predicate[0]));

        });
        Page<MtWorksheet> worksheetsObject = worksheetRepo.findAll(worksheetSpecification, pageable);
        if(worksheetsObject != null){
            PaginationResponse paginationResponse=new PaginationResponse();
            paginationResponse.setWorksheetList(worksheetsObject.getContent());
            paginationResponse.setTotalPages(worksheetsObject.getTotalPages());
            paginationResponse.setTotalElements(worksheetsObject.getTotalElements());
            return paginationResponse;
        }
        return new PaginationResponse();
    }

    private MtWorksheet dtoToWorksheetEntity(WorksheetDto request) {
        MtWorksheet mtWorksheet = new MtWorksheet();
        mtWorksheet.setName(request.getName());
        mtWorksheet.setDescription(request.getDescription());
        mtWorksheet.setIsActive(request.getIsActive());
        if (Validator.isValid(request.getFileId())) {
            Optional<FileEntity> fileEntity = fileRepo.findByFileId(request.getFileId());
            if (fileEntity.isPresent()) {
                mtWorksheet.setFile(fileEntity.get());
            }
        }
        return mtWorksheet;
    }
}
