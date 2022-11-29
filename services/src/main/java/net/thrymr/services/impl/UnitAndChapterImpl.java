package net.thrymr.services.impl;

import net.thrymr.dto.ChapterDto;
import net.thrymr.dto.PaginationResponse;
import net.thrymr.dto.UnitDto;

import net.thrymr.model.Chapter;
import net.thrymr.model.FileEntity;
import net.thrymr.model.Unit;
import net.thrymr.model.master.MtQuestion;
import net.thrymr.repository.ChapterRepo;
import net.thrymr.repository.FileRepo;
import net.thrymr.repository.UnitRpo;
import net.thrymr.services.UnitAndChapterServices;
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
        return unit;
    }

    @Override
    public List<Unit> getAllUnit() {
        List<Unit> unitList = unitRpo.findAll();
        if (!unitList.isEmpty()) {
            return unitList.stream().filter(obj -> obj.getIsActive().equals(Boolean.TRUE)).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    @Override
    public Page<Unit> getLearnPath(UnitDto unitDto) {
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
            return criteriaBuilder.and(addUnitPredicate.toArray(new Predicate[0]));
        });
        Page<Unit> unitObjectives = unitRpo.findAll(addUnitSpecification, pageable);
        if (unitObjectives.getContent() != null) {
            return new org.springframework.data.domain.PageImpl<>(unitObjectives.getContent(), pageable, 0l);
        }
        return new org.springframework.data.domain.PageImpl<>(new ArrayList<>(), pageable, 0l);
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
    public String saveChapter(ChapterDto request) {
        Chapter chapter = chapterRepo.save(dtoToChapter(request));
        return "saved successfully";
    }

    @Override
    public String updateChaptersById(ChapterDto dto) {
        if (Validator.isValid(dto.getId())) {
            Optional<Chapter> optionalChapter = chapterRepo.findById(dto.getId());
            Chapter chapter;
            if (optionalChapter.isPresent()) {
                chapter = optionalChapter.get();
                if (Validator.isValid(dto.getChapterName())) {
                    chapter.setChapterName(dto.getChapterName());
                }
                if (Validator.isValid(dto.getDescription())) {
                    chapter.setDescription(dto.getDescription());
                }
                if (Validator.isValid(dto.getProfilePictureId())) {
                    Optional<FileEntity> optionalFileEntity=fileRepo.findByFileId(dto.getProfilePictureId());
                    if(optionalFileEntity.isPresent()) {
                        chapter.setProfilePicture(optionalFileEntity.get());
                    }
                }
                if (Validator.isValid(dto.getVideoId())) {
                    Optional<FileEntity> optionalFileEntity=fileRepo.findByFileId(dto.getVideoId());
                    if(optionalFileEntity.isPresent()) {
                        chapter.setVideo(optionalFileEntity.get());
                    }
                }
                if (dto.getIsActive() != null && dto.getIsActive().equals(Boolean.TRUE) || dto.getIsActive().equals(Boolean.FALSE)) {
                    chapter.setIsActive(dto.getIsActive());
                }
                chapterRepo.save(chapter);
            }
            return "update successfully";
        }
        return "id is not present in database";
    }

    @Override
    public List<Chapter> getAllChapters() {
        List<Chapter> chapterList = chapterRepo.findAll();
        if (!chapterList.isEmpty()) {
            return chapterList.stream().filter(obj -> obj.getIsActive().equals(Boolean.TRUE)).collect(Collectors.toList());
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
        if (chapterDto.getPageNumber() != null) {
            pageable = PageRequest.of(chapterDto.getPageNumber(), chapterDto.getPageSize());
        }
        if (chapterDto.getIsSorting() != null && chapterDto.getIsSorting().equals(Boolean.TRUE)) {
            pageable = PageRequest.of(chapterDto.getPageNumber(), chapterDto.getPageSize(), Sort.Direction.ASC, "chapterName");
        } else if (chapterDto.getIsSorting() != null && chapterDto.getIsSorting().equals(Boolean.FALSE)) {
            pageable = PageRequest.of(chapterDto.getPageNumber(), chapterDto.getPageSize(), Sort.Direction.DESC, "chapterName");
        }
        //filters
        Specification<Chapter> chapterSpecification = ((root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> addUnitPredicate = new ArrayList<>();
            Join<Chapter, MtQuestion> questionJoin = root.join("questionList");
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
                Predicate createdOn = criteriaBuilder.and(questionJoin.get("createdOn").in(chapterDto.getAddedOn()));
                addUnitPredicate.add(createdOn);
            }
            if (chapterDto.getQuestionId() != null) {
                Predicate question = criteriaBuilder.and(root.get("question").in(chapterDto.getQuestionId()));
                addUnitPredicate.add(question);
            }

            return criteriaBuilder.and(addUnitPredicate.toArray(new Predicate[0]));
        });
        Page<Chapter> chapterObjectives = chapterRepo.findAll(chapterSpecification, pageable);
        if (chapterObjectives.getContent() != null) {
            PaginationResponse paginationResponse = new PaginationResponse();
            paginationResponse.setChapterList(new ArrayList<>(chapterObjectives.getContent()));
            paginationResponse.setTotalPages(chapterObjectives.getTotalPages());
            paginationResponse.setTotalElements(chapterObjectives.getTotalElements());
            return paginationResponse;
        }
        return new PaginationResponse();
    }

    public Unit dtoToEntity(UnitDto dto) {
        Unit unit = new Unit();
        unit.setUnitName(dto.getUnitName());
        if (dto.getIsActive() != null && dto.getIsActive().equals(Boolean.TRUE)) {
            unit.setIsActive(dto.getIsActive());
        }
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
        return searchKey;
    }

    public Chapter dtoToChapter(ChapterDto chapterDto) {
        Chapter chapter = new Chapter();
        chapter.setChapterName(chapterDto.getChapterName());
        chapter.setDescription(chapterDto.getDescription());
        if (Validator.isValid(chapterDto.getProfilePictureId())) {
            Optional<FileEntity> optionalFileEntity=fileRepo.findByFileId(chapterDto.getProfilePictureId());
            if(optionalFileEntity.isPresent()) {
                chapter.setProfilePicture(optionalFileEntity.get());
            }
        }
        if (Validator.isValid(chapterDto.getVideoId())) {
            Optional<FileEntity> optionalFileEntity=fileRepo.findByFileId(chapterDto.getVideoId());
            if(optionalFileEntity.isPresent()) {
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
