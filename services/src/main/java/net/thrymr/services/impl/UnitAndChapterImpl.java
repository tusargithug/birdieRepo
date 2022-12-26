package net.thrymr.services.impl;

import net.thrymr.dto.ChapterDto;
import net.thrymr.dto.UnitDto;

import net.thrymr.dto.response.PaginationResponse;
import net.thrymr.model.AppUser;
import net.thrymr.model.Chapter;
import net.thrymr.model.FileEntity;
import net.thrymr.model.Unit;
import net.thrymr.model.master.MtQuestion;
import net.thrymr.repository.ChapterRepo;
import net.thrymr.repository.FileRepo;
import net.thrymr.repository.UnitRpo;
import net.thrymr.services.UnitAndChapterServices;
import net.thrymr.utils.Validator;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class UnitAndChapterImpl implements UnitAndChapterServices {
    @Autowired
    UnitRpo unitRpo;

    @Autowired
    ChapterRepo chapterRepo;

    @Autowired
    FileRepo fileRepo;

    @Override
    public String saveUnit(UnitDto request) {
        unitRpo.save(dtoToEntity(request));
        return "unit saved successfully";
    }

    @Override
    public String updateUnitById(UnitDto request) {
        if (Validator.isValid(request.getId())) {
            unitRpo.save(dtoToEntityForUpdate(request));
            return "unit update successfully";
        }
        return "record not found";
    }

    public Unit dtoToEntityForUpdate(UnitDto dto) {
        Optional<Unit> optionalAddUnit = unitRpo.findById(dto.getId());
        Unit unit = optionalAddUnit.orElse(new Unit());
        if (optionalAddUnit.isPresent()) {
            unit = optionalAddUnit.get();
            if (Validator.isValid(dto.getUnitName())) {
                unit.setUnitName(dto.getUnitName());
            }
        }
        if (dto.getIsActive() != null && dto.getIsActive().equals(Boolean.TRUE) || dto.getIsActive().equals(Boolean.FALSE)) {
            unit.setIsActive(dto.getIsActive());
        }
        unit.setSearchKey(getUnitSearchKey(unit));
        return unit;
    }

    @Override
    public List<Unit> getAllUnit() {
        List<Unit> unitList = unitRpo.findAll();
        if (!unitList.isEmpty()) {
            return unitList.stream().filter(obj -> obj.getIsDeleted().equals(Boolean.FALSE)).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    @Override
    public PaginationResponse getLearnPath(UnitDto unitDto) {
        Pageable pageable = null;
        if (unitDto.getPageSize() != null) {
            pageable = PageRequest.of(unitDto.getPageNumber(), unitDto.getPageSize());
        }
        if (unitDto.getSortUserName() != null && unitDto.getSortUserName().equals(Boolean.TRUE)) {
            pageable = PageRequest.of(unitDto.getPageNumber(), unitDto.getPageSize(), Sort.Direction.ASC, "unitName");
        } else if (unitDto.getSortUserName() != null && unitDto.getSortUserName().equals(Boolean.FALSE)) {
            pageable = PageRequest.of(unitDto.getPageNumber(), unitDto.getPageSize(), Sort.Direction.DESC, "unitName");
        }
        //filters
        Specification<Unit> addUnitSpecification = ((root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> addUnitPredicate = new ArrayList<>();
            if (unitDto.getId() != null) {
                Predicate id = criteriaBuilder.and(root.get("id").in(unitDto.getId()));
                addUnitPredicate.add(id);
            }
            if (unitDto.getCreatedOn() != null) {
                Predicate createdOn = criteriaBuilder.and(root.get("createdOn").in(unitDto.getCreatedOn()));
                addUnitPredicate.add(createdOn);
            }
            if (unitDto.getIsActive() != null && unitDto.getIsActive().equals(Boolean.TRUE)) {
                Predicate status = criteriaBuilder.and(root.get("isActive").in(unitDto.getIsActive()));
                addUnitPredicate.add(status);
            } else if (unitDto.getIsActive() != null && unitDto.getIsActive().equals(Boolean.FALSE)) {
                Predicate status = criteriaBuilder.and(root.get("isActive").in(unitDto.getIsActive()));
                addUnitPredicate.add(status);
            }
            if (unitDto.getChapterCount() != null) {
                Predicate chapters = criteriaBuilder.and(root.get("chapters").in(unitDto.getChapterCount()));
                addUnitPredicate.add(chapters);
            }
            if (Validator.isValid(unitDto.getSearchKey())) {
                Predicate searchPredicate = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("searchKey")),
                        "%" + unitDto.getSearchKey().toLowerCase() + "%");
                addUnitPredicate.add(searchPredicate);
            }
            return criteriaBuilder.and(addUnitPredicate.toArray(new Predicate[0]));
        });
        Page<Unit> unitObjectives = unitRpo.findAll(addUnitSpecification, pageable);
        if (unitObjectives.getContent() != null) {
            PaginationResponse paginationResponse = new PaginationResponse();
            paginationResponse.setUnitList(unitObjectives.getContent());
            paginationResponse.setTotalPages(unitObjectives.getTotalPages());
            paginationResponse.setTotalElements(unitObjectives.getTotalElements());
            return paginationResponse;
        }
        return new PaginationResponse();
    }

    @Override
    public String deleteUnitById(Long id) {
        Optional<Unit> unitId = unitRpo.findById(id);
        if (unitId.isPresent()) {
            Unit unit = unitId.get();
            unit.setIsDeleted(Boolean.TRUE);
            unit.setIsActive(Boolean.FALSE);
            unitRpo.save(unit);
        }
        return "record delete records successfully";
    }

    @Override
    public Chapter saveChapter(ChapterDto request) {
        Chapter chapter = chapterRepo.save(dtoToChapter(request));
        return chapter;
    }

    @Override
    public Chapter updateChaptersById(ChapterDto dto) {
        if (Validator.isValid(dto.getId())) {
            Optional<Chapter> optionalChapter = chapterRepo.findById(dto.getId());
            Chapter chapter = null;
            if (optionalChapter.isPresent()) {
                chapter = optionalChapter.get();
                if (Validator.isValid(dto.getChapterName())) {
                    chapter.setChapterName(dto.getChapterName());
                }
                if (Validator.isValid(dto.getDescription())) {
                    chapter.setDescription(dto.getDescription());
                }
                if (Validator.isValid(dto.getProfilePictureId())) {
                    Optional<FileEntity> optionalFileEntity = fileRepo.findByFileId(dto.getProfilePictureId());
                    if (optionalFileEntity.isPresent()) {
                        chapter.setProfilePicture(optionalFileEntity.get());
                    }
                }
                if (Validator.isValid(dto.getVideoId())) {
                    Optional<FileEntity> optionalFileEntity = fileRepo.findByFileId(dto.getVideoId());
                    if (optionalFileEntity.isPresent()) {
                        chapter.setVideo(optionalFileEntity.get());
                    }
                }
                if (dto.getIsActive() != null && dto.getIsActive().equals(Boolean.TRUE) || dto.getIsActive().equals(Boolean.FALSE)) {
                    chapter.setIsActive(dto.getIsActive());
                }
                chapterRepo.save(chapter);
            }
            return chapter;
        }
        return new Chapter();
    }

    @Override
    public List<Chapter> getAllChapters() {
        List<Chapter> chapterList = chapterRepo.findAll();
        if (!chapterList.isEmpty()) {
            return chapterList.stream().filter(obj -> obj.getIsDeleted().equals(Boolean.FALSE)).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    @Override
    public String deleteChapterById(Long id) {
        Optional<Chapter> chapterId = chapterRepo.findById(id);
        if (chapterId.isPresent()) {
            Chapter chapter = chapterId.get();
            chapter.setIsDeleted(Boolean.TRUE);
            chapter.setIsActive(Boolean.FALSE);
            chapterRepo.save(chapter);
        }
        return "record delete records successfully";
    }

    @Override
    public PaginationResponse getAllChapterPagination(ChapterDto chapterDto) {
        Pageable pageable = null;
        if (chapterDto.getPageNumber() != null && chapterDto.getPageSize() != null) {
            pageable = PageRequest.of(chapterDto.getPageNumber(), chapterDto.getPageSize());
        }
        if (chapterDto.getPageNumber() != null && chapterDto.getPageSize() != null) {
            pageable = PageRequest.of(chapterDto.getPageNumber(), chapterDto.getPageSize(), Sort.Direction.DESC, "createdOn");
        }
        if (chapterDto.getIsSorting() != null && chapterDto.getIsSorting().equals(Boolean.TRUE)) {
            pageable = PageRequest.of(chapterDto.getPageNumber(), chapterDto.getPageSize(), Sort.Direction.ASC, "chapterName");
        } else if (chapterDto.getIsSorting() != null && chapterDto.getIsSorting().equals(Boolean.FALSE)) {
            pageable = PageRequest.of(chapterDto.getPageNumber(), chapterDto.getPageSize(), Sort.Direction.DESC, "chapterName");
        }
        //filters
        Specification<Unit> chapterSpecification = ((root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> addUnitPredicate = new ArrayList<>();
            if (chapterDto.getIsActive() != null && chapterDto.getIsActive().equals(Boolean.TRUE)) {
                Predicate isActive = criteriaBuilder.and(root.get("isActive").in(chapterDto.getIsActive()));
                addUnitPredicate.add(isActive);
            } else if (chapterDto.getIsActive() != null && chapterDto.getIsActive().equals(Boolean.FALSE)) {
                Predicate isActive = criteriaBuilder.and(root.get("isActive").in(chapterDto.getIsActive()));
                addUnitPredicate.add(isActive);
            }
            if (chapterDto.getChapterName() != null && !chapterDto.getChapterName().isEmpty()) {
                Predicate unitName = criteriaBuilder.and(root.get("chapterName").in(chapterDto.getChapterName()));
                addUnitPredicate.add(unitName);
            }
            if (chapterDto.getAddedOn() != null) {
                Predicate createdOn = criteriaBuilder.and(root.get("createdOn").in(chapterDto.getAddedOn()));
                addUnitPredicate.add(createdOn);
            }
            Predicate isDeletedPredicate = criteriaBuilder.equal(root.get("isDeleted"), Boolean.FALSE);
            addUnitPredicate.add(isDeletedPredicate);
            if (Validator.isValid(chapterDto.getSearchKey())) {
                Predicate searchPredicate = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("searchKey")),
                        "%" + chapterDto.getSearchKey().toLowerCase() + "%");
                addUnitPredicate.add(searchPredicate);
            }
            Predicate isDeleted = criteriaBuilder.equal(root.get("isDeleted"), Boolean.FALSE);
            addUnitPredicate.add(isDeleted);
            return criteriaBuilder.and(addUnitPredicate.toArray(new Predicate[0]));
        });
        PaginationResponse paginationResponse = new PaginationResponse();
        if (chapterDto.getPageSize() != null && chapterDto.getPageNumber() != null) {
            Page<Unit> unitObjectives = unitRpo.findAll(chapterSpecification, pageable);
            if (!unitObjectives.getContent().isEmpty()) {
                if (chapterDto.getUnitId() != null) {
                    paginationResponse.setUnitList(unitObjectives.stream().filter(unit -> unit.getId().equals(chapterDto.getUnitId())).collect(Collectors.toList()));
                    paginationResponse.setTotalPages(unitObjectives.getTotalPages());
                    paginationResponse.setTotalElements(unitObjectives.getTotalElements());
                    return paginationResponse;
                }
            }
        }else {
            List<Unit> unitObjectives = unitRpo.findAll(chapterSpecification);
            paginationResponse.setUnitList(unitObjectives.stream().filter(unit -> unit.getId().equals(chapterDto.getUnitId())).collect(Collectors.toList()));
            return paginationResponse;
        }
        return new PaginationResponse();
    }

    @Override
    public Chapter getChapterById(Long id) {
       Chapter chapter = null;
       if (Validator.isValid(id)) {
           Optional<Chapter> optionalAppUser = chapterRepo.findById(id);
           if (optionalAppUser.isPresent() && optionalAppUser.get().getIsDeleted().equals(Boolean.FALSE)) {
               chapter = optionalAppUser.get();
               return chapter;
           }
       }
        return new Chapter();
    }

    public Unit dtoToEntity(UnitDto dto) {
        Unit unit = new Unit();
        unit.setUnitName(dto.getUnitName());
        if (dto.getIsActive() != null && dto.getIsActive().equals(Boolean.TRUE)) {
            unit.setIsActive(dto.getIsActive());
        }
        unit.setSearchKey(getUnitSearchKey(unit));
        return unit;
    }

    private String getUnitSearchKey(Unit unit) {
        String searchKey = "";
        if (unit.getId() != null) {
            searchKey = searchKey + unit.getId();
        }
        if (unit.getUnitName() != null && !unit.getUnitName().isEmpty()) {
            searchKey = searchKey + " " + unit.getUnitName();
        }
        if (unit.getIsActive() != null) {
            searchKey = searchKey + " " + unit.getIsActive();
        }
        if (unit.getCreatedOn() != null) {
            searchKey = searchKey + " " + unit.getCreatedOn();
        }
        if (unit.getChapters() != null) {
            searchKey = searchKey + " " + unit.getChapters().stream().map(Chapter::getChapterName);
        }
        if (unit.getChapters() != null) {
            searchKey = searchKey + " " + unit.getChapters().stream().map(Chapter::getDescription);
        }
        if (unit.getChapters() != null) {
            searchKey = searchKey + " " + unit.getChapters().stream().map(Chapter::getVideo);
        }
        if (unit.getChapters() != null) {
            searchKey = searchKey + " " + unit.getChapters().stream().map(Chapter::getProfilePicture);
        }
        return searchKey;
    }

    public Chapter dtoToChapter(ChapterDto chapterDto) {
        Chapter chapter = new Chapter();
        chapter.setChapterName(chapterDto.getChapterName());
        chapter.setDescription(chapterDto.getDescription());
        if (Validator.isValid(chapterDto.getProfilePictureId())) {
            Optional<FileEntity> optionalFileEntity = fileRepo.findByFileId(chapterDto.getProfilePictureId());
            if (optionalFileEntity.isPresent()) {
                chapter.setProfilePicture(optionalFileEntity.get());
            }
        }
        if (Validator.isValid(chapterDto.getVideoId())) {
            Optional<FileEntity> optionalFileEntity = fileRepo.findByFileId(chapterDto.getVideoId());
            if (optionalFileEntity.isPresent()) {
                chapter.setVideo(optionalFileEntity.get());
            }
        }
        if (chapterDto.getUnitId() != null) {
            Optional<Unit> unitId = unitRpo.findById(chapterDto.getUnitId());
            if (unitId.isPresent()) {
                chapter.setUnit(unitId.get());
            }
        }
        if (chapter.getIsActive() != null && chapterDto.getIsActive().equals(Boolean.TRUE)) {
            chapter.setIsActive(chapterDto.getIsActive());
        }
        return chapter;
    }
}
