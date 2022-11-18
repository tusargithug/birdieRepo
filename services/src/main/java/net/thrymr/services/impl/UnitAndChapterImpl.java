package net.thrymr.services.impl;

import net.thrymr.dto.ChapterDto;
import net.thrymr.dto.UnitDto;
import net.thrymr.model.master.MtChapter;
import net.thrymr.model.master.MtUnit;
import net.thrymr.repository.ChapterRepo;
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

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static net.thrymr.utils.DateUtils.dateToString;

@Service
public class UnitAndChapterImpl implements UnitAndChapterServices {
    @Autowired
    UnitRpo unitRpo;

    @Autowired
    ChapterRepo chapterRepo;

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

    public MtUnit dtoToEntityForUpdate(UnitDto dto) {
        Optional<MtUnit> optionalAddUnit = unitRpo.findById(dto.getId());
        MtUnit unit = optionalAddUnit.orElse(new MtUnit());
        if (optionalAddUnit.isPresent()) {
            unit = optionalAddUnit.get();
            if (Validator.isValid(dto.getUnitName())) {
                unit.setUnitName(dto.getUnitName());
            }
        }
        if (dto.getIsActive()!=null && dto.getIsActive().equals(Boolean.TRUE) || dto.getIsActive().equals(Boolean.FALSE)) {
            unit.setIsActive(dto.getIsActive());
        }
        return unit;
    }

    @Override
    public List<MtUnit> getAllUnit() {
        List<MtUnit> unitList = unitRpo.findAll();
        if (!unitList.isEmpty()) {
            return unitList.stream().filter(obj -> obj.getIsActive().equals(Boolean.TRUE)).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    @Override
    public List<MtUnit> getLearnPath(UnitDto unitDto) {

        Pageable pageable = null;
        if (unitDto.getPageSize() != null) {
            pageable = PageRequest.of(unitDto.getPageNumber(), unitDto.getPageSize());
        }
        if (unitDto.getAddOn() != null) {
            pageable = PageRequest.of(unitDto.getPageNumber(), unitDto.getPageSize(), Sort.Direction.DESC, "createdOn");
        }
        //filters
        Specification<MtUnit> addUnitSpecification = ((root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> addUnitPredicate = new ArrayList<>();
            if (unitDto.getId() != null) {
                Predicate id = criteriaBuilder.and(root.get("id").in(unitDto.getId()));
                addUnitPredicate.add(id);
            }
            if (unitDto.getUnitName() != null && !unitDto.getUnitName().isEmpty()) {
                Predicate unitName = criteriaBuilder.and(root.get("unitName").in(unitDto.getUnitName()));
                addUnitPredicate.add(unitName);
            }
            if (unitDto.getAddOn() != null) {
                Predicate createdOn = criteriaBuilder.and(root.get("createdOn").in(unitDto.getAddOn()));
                addUnitPredicate.add(createdOn);
            }
            return criteriaBuilder.and(addUnitPredicate.toArray(new Predicate[0]));
        });
        Page<MtUnit> unitObjectives = unitRpo.findAll(addUnitSpecification, pageable);
        List<MtUnit> unitList = null;
        if (unitObjectives.getContent() != null) {
            unitList = unitObjectives.stream().filter(obj -> obj.getIsActive().equals(Boolean.TRUE)).collect(Collectors.toList());
        }
        return unitList;
    }

    @Override
    public String deleteUnitById(Long id) {
        Optional<MtUnit> unitId = unitRpo.findById(id);
        if (unitId.isPresent()) {
            MtUnit unit = unitId.get();
            unit.setIsDeleted(Boolean.TRUE);
            unit.setIsActive(Boolean.FALSE);
            unitRpo.save(unit);
        }
        return "record delete records successfully";
    }

    @Override
    public String saveChapter(ChapterDto request) {
        MtChapter mtChapter = chapterRepo.save(dtoToChapter(request));
        return "saved successfully";
    }

    @Override
    public String updateChaptersById(ChapterDto dto) {
        if (Validator.isValid(dto.getId())) {
            Optional<MtChapter> optionalChapter = chapterRepo.findById(dto.getId());
            MtChapter mtChapter;
            if (optionalChapter.isPresent()) {
                mtChapter = optionalChapter.get();
                if (Validator.isValid(dto.getChapterName())) {
                    mtChapter.setChapterName(dto.getChapterName());
                }
                if (Validator.isValid(dto.getDescription())) {
                    mtChapter.setDescription(dto.getDescription());
                }
                if (Validator.isValid(String.valueOf(dto.getProfilePicture()))) {
                    mtChapter.setProfilePicture(dto.getProfilePicture());
                }
                if (Validator.isValid(String.valueOf(dto.getVideo()))) {
                    mtChapter.setVideo(dto.getVideo());
                }
                if (dto.getIsActive()!=null && dto.getIsActive().equals(Boolean.TRUE) || dto.getIsActive().equals(Boolean.FALSE)) {
                    mtChapter.setIsActive(dto.getIsActive());
                }
                chapterRepo.save(mtChapter);
            }
            return "update successfully";
        }
        return "id is not present in database";
    }

    @Override
    public List<MtChapter> getAllChapters() {
        List<MtChapter> mtChapterList = chapterRepo.findAll();
        if (!mtChapterList.isEmpty()) {
            return mtChapterList.stream().filter(obj -> obj.getIsActive().equals(Boolean.TRUE)).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    @Override
    public String deleteChapterById(Long id) {
        Optional<MtChapter> chapterId = chapterRepo.findById(id);
        if (chapterId.isPresent()) {
            MtChapter mtChapter = chapterId.get();
            mtChapter.setIsDeleted(Boolean.TRUE);
            mtChapter.setIsActive(Boolean.FALSE);
            chapterRepo.save(mtChapter);
        }
        return "record delete records successfully";
    }

    @Override
    public List<MtChapter> getAllChapterPagination(ChapterDto chapterDto) {
        Pageable pageable = null;
        if (chapterDto.getPageNumber() != null) {
            pageable = PageRequest.of(chapterDto.getPageNumber(), chapterDto.getPageSize());
        }
        if (chapterDto.getAddedOn() != null) {
            pageable = PageRequest.of(chapterDto.getPageNumber(), chapterDto.getPageSize(), Sort.Direction.DESC, "createdOn");
        }
        //filters
        Specification<MtChapter> chapterSpecification = ((root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> addUnitPredicate = new ArrayList<>();
            if (chapterDto.getId() != null) {
                Predicate id = criteriaBuilder.and(root.get("id").in(chapterDto.getId()));
                addUnitPredicate.add(id);
            }
            if (chapterDto.getChapterName() != null && !chapterDto.getChapterName().isEmpty()) {
                Predicate unitName = criteriaBuilder.and(root.get("chapterName").in(chapterDto.getChapterName()));
                addUnitPredicate.add(unitName);
            }
            if (chapterDto.getAddedOn() != null) {
                Predicate createdOn = criteriaBuilder.and(root.get("createdOn").in(chapterDto.getAddedOn()));
                addUnitPredicate.add(createdOn);
            }
            return criteriaBuilder.and(addUnitPredicate.toArray(new Predicate[0]));
        });
        Page<MtChapter> chapterObjectives = chapterRepo.findAll(chapterSpecification, pageable);
        List<MtChapter> mtChapterList = null;
        if (chapterObjectives.getContent() != null) {
            mtChapterList = chapterObjectives.stream().filter(obj -> obj.getIsActive().equals(Boolean.TRUE)).collect(Collectors.toList());
        }
        return mtChapterList;
    }

    public MtUnit dtoToEntity(UnitDto dto) {
        MtUnit unit = new MtUnit();
        unit.setUnitName(dto.getUnitName());
        if (dto.getIsActive()!=null && dto.getIsActive().equals(Boolean.TRUE)) {
            unit.setIsActive(dto.getIsActive());
        }
        return unit;
    }


    public UnitDto entityToDto(MtUnit unit) {
        UnitDto unitDto = new UnitDto();
        unitDto.setId(unit.getId());
        unitDto.setUnitName(unit.getUnitName());
        unitDto.setIsActive(unit.getIsActive());
        Optional<MtUnit> optionalAddUnit = unitRpo.findById(unit.getId());
        if (optionalAddUnit.isPresent()) {
            unitDto.setChapterCount(optionalAddUnit.get().getMtChapters().size());
        }
        unitDto.setAddOn(dateToString(unit.getCreatedOn()));
        unitDto.setSearchKey(getUnitSearchKey(unit));
        return unitDto;
    }

    public UnitDto entityToDtoForGetAll(MtUnit unit) {
        UnitDto unitDto = new UnitDto();
        unitDto.setUnitName(unit.getUnitName());
        unitDto.setIsActive(unit.getIsActive());
        Optional<MtUnit> optionalAddUnit = unitRpo.findById(unit.getId());
        unitDto.setChapterCount(optionalAddUnit.get().getMtChapters().size());
        unitDto.setAddOn(dateToString(unit.getCreatedOn()));
        return unitDto;
    }


    private String getUnitSearchKey(MtUnit unit) {
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

    public MtChapter dtoToChapter(ChapterDto chapterDto) {
        MtChapter mtChapter = new MtChapter();
        mtChapter.setChapterName(chapterDto.getChapterName());
        mtChapter.setDescription(chapterDto.getDescription());
        mtChapter.setProfilePicture(chapterDto.getProfilePicture());
        mtChapter.setVideo(chapterDto.getVideo());
        if (chapterDto.getUnitId() != null) {
            Optional<MtUnit> unitId = unitRpo.findById(chapterDto.getUnitId());
            if (unitId.isPresent()) {
                mtChapter.setMtUnit(unitId.get());
            }
        }
        if (mtChapter.getIsActive() != null && chapterDto.getIsActive().equals(Boolean.TRUE)) {
            mtChapter.setIsActive(chapterDto.getIsActive());
        }
        return mtChapter;
    }
}
