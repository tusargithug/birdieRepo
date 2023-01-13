package net.thrymr.services.impl;

import net.thrymr.dto.ChapterDto;
import net.thrymr.dto.UnitDto;

import net.thrymr.dto.response.*;
import net.thrymr.model.*;
import net.thrymr.model.master.MtOptions;
import net.thrymr.model.master.MtQuestion;
import net.thrymr.repository.ChapterRepo;
import net.thrymr.repository.FileRepo;
import net.thrymr.repository.UnitRpo;
import net.thrymr.services.UnitAndChapterServices;
import net.thrymr.utils.Validator;
import org.apache.poi.sl.draw.geom.GuideIf;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
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
        if(Validator.isValid(dto.getId())) {
            Optional<Unit> optionalAddUnit = unitRpo.findById(dto.getId());
            Unit unit;
            if (optionalAddUnit.isPresent()) {
                unit = optionalAddUnit.get();
                if (Validator.isValid(dto.getUnitName())) {
                    unit.setUnitName(dto.getUnitName());
                }
                if (dto.getIsActive() != null && dto.getIsActive().equals(Boolean.TRUE) || dto.getIsActive().equals(Boolean.FALSE)) {
                    unit.setIsActive(dto.getIsActive());
                }
                if(Validator.isValid(dto.getSequence())){
                    unit.setSequence(dto.getSequence());
                }
                unit.setSearchKey(getUnitSearchKey(unit));
                return unit;
            }
        }
        return new Unit();
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
            Predicate isDeletedPredicate = criteriaBuilder.equal(root.get("isDeleted"), Boolean.FALSE);
            addUnitPredicate.add(isDeletedPredicate);

            return criteriaBuilder.and(addUnitPredicate.toArray(new Predicate[0]));
        });
        Page<Unit> unitObjectives = unitRpo.findAll(addUnitSpecification, pageable);
        if (unitObjectives.getContent() != null) {
            PaginationResponse paginationResponse = new PaginationResponse();
            paginationResponse.setUnitList(unitObjectives.stream().collect(Collectors.toSet()));
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
                if(Validator.isValid(dto.getUnitId())){
                    Optional<Unit> optionalUnit = unitRpo.findById(dto.getUnitId());
                    if(optionalUnit.isPresent()){
                        chapter.setUnit(optionalUnit.get());
                    }
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
                if(Validator.isValid(dto.getSequence())){
                    chapter.setSequence(dto.getSequence());
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
        return "Chapter deleted successfully";
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
                Join<Unit, Chapter> unitChapterJoin = root.join("chapters", JoinType.LEFT);
                Predicate unitName = criteriaBuilder.and(unitChapterJoin.get("chapterName").in(chapterDto.getChapterName()));
                addUnitPredicate.add(unitName);
            }
            Predicate isDeletedUnit = criteriaBuilder.equal(root.get("isDeleted"), Boolean.FALSE);
            addUnitPredicate.add(isDeletedUnit);

            Join<Unit, Chapter> unitChapterJoin = root.join("chapters");
            Predicate chapterIsDeletedPredicate=criteriaBuilder.and(unitChapterJoin.get("isDeleted").in(Boolean.FALSE));
            addUnitPredicate.add(chapterIsDeletedPredicate);

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
            return criteriaBuilder.and(addUnitPredicate.toArray(new Predicate[0]));
        });
        PaginationResponse paginationResponse = new PaginationResponse();
        if (chapterDto.getPageSize() != null && chapterDto.getPageNumber() != null) {
            Page<Unit> unitObjectives = unitRpo.findAll(chapterSpecification, pageable);
            if (!unitObjectives.getContent().isEmpty()) {
                if (chapterDto.getUnitId() != null) {
                    paginationResponse.setUnitList(unitObjectives.stream().filter(unit -> unit.getId().equals(chapterDto.getUnitId())).collect(Collectors.toSet()));
                    paginationResponse.setTotalPages(unitObjectives.getTotalPages());
                    paginationResponse.setTotalElements(unitObjectives.getTotalElements());
                    return paginationResponse;
                }
            }
        } else {
            List<Unit> unitObjectives = unitRpo.findAll(chapterSpecification);
            System.out.println(unitObjectives.size());
            paginationResponse.setUnitList(unitObjectives.stream().filter(unit -> unit.getId().equals(chapterDto.getUnitId())).collect(Collectors.toSet()));
            return paginationResponse;
        }
        return new PaginationResponse();
    }
    @Override
    public ChapterResponse getChapterById(Long id) {
        Chapter chapter = null;
        if (Validator.isValid(id)) {
            ChapterResponse chapterResponse = new ChapterResponse();
            List<QuestionResponse> questionResponseList = new ArrayList<>();
            Optional<Chapter> optionalAppUser = chapterRepo.findByIdAndIsDeletedAndQuestionListIsDeletedAndQuestionListMtOptionsIsDeleted(id, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE);
            if (optionalAppUser.isPresent() && optionalAppUser.get().getIsDeleted().equals(Boolean.FALSE)) {
                chapter = optionalAppUser.get();
                chapterResponse.setId(chapter.getId());
                chapterResponse.setChapterName(chapter.getChapterName());
                chapterResponse.setDescription(chapter.getDescription());
                chapterResponse.setSequence(chapter.getSequence());
                if (optionalAppUser.get().getQuestionList() != null) {
                    for (MtQuestion mtQuestion : optionalAppUser.get().getQuestionList()) {
                        if (mtQuestion.getIsDeleted().equals(Boolean.FALSE)) {
                            QuestionResponse questionResponse = new QuestionResponse();
                            questionResponse.setId(mtQuestion.getId());
                            questionResponse.setQuestion(mtQuestion.getQuestion());
                            questionResponse.setSequence(mtQuestion.getSequence());
                            questionResponse.setQuestionCalType(mtQuestion.getQuestionCalType());
                            if (mtQuestion.getMtOptions() != null && !mtQuestion.getMtOptions().isEmpty()) {
                                List<OptionResponse> optionResponseList = new ArrayList<>();
                                for (MtOptions mtOptions : mtQuestion.getMtOptions()) {
                                    if (mtOptions.getIsDeleted().equals(Boolean.FALSE)) {
                                        OptionResponse optionResponse = new OptionResponse();
                                        optionResponse.setId(optionResponse.getId());
                                        optionResponse.setIsCorrect(mtOptions.getIsCorrect());
                                        optionResponse.setTextAnswer(mtOptions.getTextAnswer());
                                        optionResponseList.add(optionResponse);
                                    }
                                }
                                questionResponse.setMtOptions(optionResponseList);
                            }
                            questionResponseList.add(questionResponse);
                        }
                    }
                }
                chapterResponse.setQuestionList(questionResponseList);

                return chapterResponse;
            }
        }
        return new ChapterResponse();
    }

    @Override
    public Unit getUnitById(Long id) {
        Unit unit = null;
        if(Validator.isValid(id)) {
            Optional<Unit> optionalUnit = unitRpo.findById(id);
            if (optionalUnit.isPresent() && optionalUnit.get().getIsDeleted().equals(Boolean.FALSE)){
                unit = optionalUnit.get();
                return unit;
            }
        }
        return new Unit();
    }

    @Override
    public Unit getUnitBySequence(Integer sequence) {
        Unit unit = null;
        if(Validator.isValid(sequence)) {
            Optional<Unit> optionalUnit = unitRpo.findBySequence(sequence);
            if (optionalUnit.isPresent() && optionalUnit.get().getIsDeleted().equals(Boolean.FALSE)){
                unit = optionalUnit.get();
                return unit;
            }
        }
        return new Unit();
    }
    @Override
    public Chapter getChapterBySequence(ChapterDto request) {
        Chapter Chapter = null;
        if(Validator.isValid(request.getUnitId()) && Validator.isValid(request.getSequence())) {
            Optional<Chapter> optionalChapter = chapterRepo.findByUnitIdAndSequence(request.getUnitId(),request.getSequence());
            if (optionalChapter.isPresent() && optionalChapter.get().getIsDeleted().equals(Boolean.FALSE)){
                Chapter = optionalChapter.get();
                return Chapter;
            }
        }
        return new Chapter();
    }


    public Unit dtoToEntity(UnitDto dto) {
        Unit unit = new Unit();
        unit.setUnitName(dto.getUnitName());
        unit.setSequence(dto.getSequence());
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
        if (unit.getIsActive() != null && unit.getIsActive().equals(Boolean.FALSE)) {
            searchKey = searchKey + " " + "Inactive";
        } else {
            searchKey = searchKey + " " + "Active";
        }
        return searchKey;
    }

    public Chapter dtoToChapter(ChapterDto chapterDto) {
        Chapter chapter = new Chapter();
        chapter.setChapterName(chapterDto.getChapterName());
        chapter.setDescription(chapterDto.getDescription());
        chapter.setSequence(chapterDto.getSequence());
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
