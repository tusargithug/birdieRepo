package net.thrymr.services.impl;

import net.thrymr.dto.ChapterDto;
import net.thrymr.dto.UnitDto;
import net.thrymr.model.Chapter;
import net.thrymr.model.Unit;
import net.thrymr.repository.ChapterRepo;
import net.thrymr.repository.UnitRpo;
import net.thrymr.services.UnitAndChapterServices;
import net.thrymr.utils.ApiResponse;
import net.thrymr.utils.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
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
    public String  saveUnit(UnitDto request) {
        unitRpo.save(dtoToEntity(request));
        return  "unit saved successfully";
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
            if(optionalAddUnit.isPresent()) {
                unit=optionalAddUnit.get();
                if(Validator.isValid(dto.getUnitName())) {
                    unit.setUnitName(dto.getUnitName());
                }
            }
        if (dto.getStatus() != null) {
            unit.setIsActive(true);
        }
        return unit;
    }

    @Override
    public List<Unit> getAllUnit() {
        List<Unit> unitList = unitRpo.findAll();
        if(!unitList.isEmpty()) {
            return unitList.stream().filter(obj-> obj.getIsActive().equals(Boolean.TRUE)).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    @Override
    public List<Unit> getLearnPath(UnitDto unitDto) {

        Pageable pageable=null;
        if (unitDto.getPageSize() != null) {
            pageable = PageRequest.of(unitDto.getPageNumber(), unitDto.getPageSize());
        }
        if (unitDto.getAddOn()!= null) {
            pageable = PageRequest.of(unitDto.getPageNumber(),unitDto.getPageSize(),Sort.Direction.DESC,"createdOn");
        }
        //filters
        Specification<Unit> addUnitSpecification = ((root, criteriaQuery, criteriaBuilder)->{
            List<Predicate> addUnitPredicate = new ArrayList<>();
           if(unitDto.getId()!=null){
                Predicate id = criteriaBuilder.and(root.get("id").in(unitDto.getId()));
                addUnitPredicate.add(id);
            }
            if(unitDto.getUnitName()!=null && !unitDto.getUnitName().isEmpty()){
                Predicate unitName = criteriaBuilder.and(root.get("unitName").in(unitDto.getUnitName()));
                addUnitPredicate.add(unitName);
            }
            if(unitDto.getAddOn()!=null){
                Predicate createdOn = criteriaBuilder.and(root.get("createdOn").in(unitDto.getAddOn()));
                addUnitPredicate.add(createdOn);
            }
            return criteriaBuilder.and(addUnitPredicate.toArray(new Predicate[0]));
        });
        Page <Unit> unitObjectives = unitRpo.findAll(addUnitSpecification, pageable);
        List<Unit> unitList =null;
        if(unitObjectives.getContent()!=null){
            unitList = unitObjectives.stream().filter(obj-> obj.getIsActive().equals(Boolean.TRUE)).collect(Collectors.toList());;
        }
        return  unitList;
    }

    @Override
    public String deleteUnitById(Long id) {
        Optional<Unit> unitId=unitRpo.findById(id);
        if(unitId.isPresent()){
          Unit unit=unitId.get();
          unit.setIsDeleted(Boolean.TRUE);
          unit.setIsActive(Boolean.FALSE);
          unitRpo.save(unit);
        }
        return "record delete records successfully";
    }

    @Override
    public String  saveChapter(ChapterDto request) {
        Chapter chapter=chapterRepo.save(dtoToChapter(request));
        return "saved successfully";
    }

    @Override
    public String updateChaptersById(ChapterDto dto) {
        if(Validator.isValid(dto.getId())) {
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
                if (Validator.isValid(String.valueOf(dto.getProfilePicture()))) {
                    chapter.setProfilePicture(dto.getProfilePicture());
                }
                if (Validator.isValid(String.valueOf(dto.getVideo()))) {
                    chapter.setVideo(dto.getVideo());
                }
                chapterRepo.save(chapter);
            }
            return "update successfully";
        }
        return "id is not present in database";
    }

    @Override
    public List<Chapter> getAllChapters() {
        List<Chapter> chapterList=chapterRepo.findAll();
        if(!chapterList.isEmpty()) {
            return chapterList.stream().filter(obj -> obj.getIsActive().equals(Boolean.TRUE)).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    @Override
    public String deleteChapterById(Long id) {
        Optional<Chapter> chapterId=chapterRepo.findById(id);
        if(chapterId.isPresent()){
            Chapter chapter=chapterId.get();
            chapter.setIsDeleted(Boolean.TRUE);
            chapter.setIsActive(Boolean.FALSE);
            chapterRepo.save(chapter);
        }
        return "record delete records successfully";
    }

    @Override
    public List<Chapter> getAllChapterPagination(ChapterDto chapterDto) {
        Pageable pageable=null;
        if (chapterDto.getPageNumber() != null) {
            pageable = PageRequest.of(chapterDto.getPageNumber(), chapterDto.getPageSize());
        }
        if (chapterDto.getAddedOn()!= null) {
            pageable = PageRequest.of(chapterDto.getPageNumber(),chapterDto.getPageSize(),Sort.Direction.DESC,"createdOn");
        }
        //filters
        Specification<Chapter> chapterSpecification = ((root, criteriaQuery, criteriaBuilder)->{
            List<Predicate> addUnitPredicate = new ArrayList<>();
            if(chapterDto.getId()!=null){
                Predicate id = criteriaBuilder.and(root.get("id").in(chapterDto.getId()));
                addUnitPredicate.add(id);
            }
            if(chapterDto.getChapterName()!=null && !chapterDto.getChapterName().isEmpty()){
                Predicate unitName = criteriaBuilder.and(root.get("chapterName").in(chapterDto.getChapterName()));
                addUnitPredicate.add(unitName);
            }
            if(chapterDto.getAddedOn()!=null){
                Predicate createdOn = criteriaBuilder.and(root.get("createdOn").in(chapterDto.getAddedOn()));
                addUnitPredicate.add(createdOn);
            }
            return criteriaBuilder.and(addUnitPredicate.toArray(new Predicate[0]));
        });
        Page <Chapter> chapterObjectives = chapterRepo.findAll(chapterSpecification, pageable);
        List<Chapter> chapterList = null;
        if(chapterObjectives.getContent()!=null){
            chapterList = chapterObjectives.stream().filter(obj-> obj.getIsActive().equals(Boolean.TRUE)).collect(Collectors.toList());
        }
        return null;
    }

    public Unit dtoToEntity(UnitDto dto) {
        Unit unit = new Unit();
        unit.setUnitName(dto.getUnitName());
        unit.setIsActive(true);
        return unit;
    }
    public UnitDto entityToDto(Unit unit) {
        UnitDto unitDto = new UnitDto();
        unitDto.setId(unit.getId());
        unitDto.setUnitName(unit.getUnitName());
        unitDto.setStatus(unit.getIsActive());
        Optional<Unit> optionalAddUnit= unitRpo.findById(unit.getId());
        if(optionalAddUnit.isPresent()){
            unitDto.setChapterCount(optionalAddUnit.get().getChapters().size());
        }
        unitDto.setAddOn(dateToString(unit.getCreatedOn()));
        unitDto.setSearchKey(getUnitSearchKey(unit));
        return unitDto;
    }

    public UnitDto entityToDtoForGetAll(Unit unit) {
        UnitDto unitDto = new UnitDto();
        unitDto.setUnitName(unit.getUnitName());
        unitDto.setStatus(unit.getIsActive());
        Optional<Unit> optionalAddUnit= unitRpo.findById(unit.getId());
        unitDto.setChapterCount(optionalAddUnit.get().getChapters().size());
        unitDto.setAddOn(dateToString(unit.getCreatedOn()));
        return unitDto;
    }


    private String getUnitSearchKey(Unit unit) {
        String searchKey="";
        if(unit.getId()!=null){
            searchKey=searchKey+ unit.getId();
        }
        if(unit.getUnitName()!=null && !unit.getUnitName().isEmpty()){
            searchKey=searchKey+" "+unit.getUnitName();
        }
        if(unit.getIsActive()!=null){
            searchKey=searchKey+" "+unit.getIsActive();
        }
        if(unit.getCreatedOn()!=null){
            searchKey=searchKey+" "+unit.getCreatedOn();
        }
        return searchKey;
    }

    public Chapter dtoToChapter(ChapterDto chapterDto) {
        Chapter chapter = new Chapter();
        chapter.setChapterName(chapterDto.getChapterName());
        chapter.setDescription(chapterDto.getDescription());
        chapter.setProfilePicture(chapterDto.getProfilePicture());
        chapter.setVideo(chapterDto.getVideo());
        if(chapterDto.getUnitId()!=null) {
            Optional<Unit> unitId = unitRpo.findById(chapterDto.getUnitId());
            if(unitId.isPresent()) {
                chapter.setUnit(unitId.get());
            }
        }

        return chapter;
    }
    public ChapterDto entityToChapter(Chapter chapter){
        ChapterDto chapterDto=new ChapterDto();
        chapterDto.setChapterName(chapter.getChapterName());
        chapterDto.setDescription(chapter.getDescription());
        chapterDto.setProfilePicture(chapter.getProfilePicture());
        chapterDto.setVideo(chapter.getVideo());
        return chapterDto;
    }
}
