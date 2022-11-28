package net.thrymr.controller;

import net.thrymr.dto.*;
import net.thrymr.dto.response.PaginationResponse;
import net.thrymr.model.Chapter;
import net.thrymr.model.Unit;
import net.thrymr.services.UnitAndChapterServices;
import net.thrymr.utils.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UnitAndChapterController {
    private final Logger logger = LoggerFactory.getLogger(AppUserController.class);
    @Autowired
    private UnitAndChapterServices unitAndChapterServices;

    @MutationMapping(name="saveUnit")
    public String saveUnit(@Argument(name = "input") UnitDto request) {
        return unitAndChapterServices.saveUnit(request);
    }

    @MutationMapping(name = "saveChapter")
    public String  saveChapter(@Argument(name = "input") ChapterDto request) {
        return unitAndChapterServices.saveChapter(request);
    }

    @MutationMapping(name="updateUnitById")
    public String updateUnitById(@Argument(name = "input") UnitDto unitDto) {
        return unitAndChapterServices.updateUnitById(unitDto);
    }
    @MutationMapping(name="updateChaptersById")
    public String updateChaptersById(@Argument(name = "input") ChapterDto dto) {
        return unitAndChapterServices.updateChaptersById(dto);
    }
    @MutationMapping(name="deleteUnitById")
    public String deleteUnitById(@Argument Long id){
        return unitAndChapterServices.deleteUnitById(id);

    }
    @MutationMapping(name="deleteChapterById")
    public String deleteChapterById(@Argument Long id){
        return unitAndChapterServices.deleteChapterById(id);
    }
    @QueryMapping("getAllUnit")
    public List<Unit> getAllUnit(){
        List<Unit> unitList = unitAndChapterServices.getAllUnit();
        return unitList;
    }
    @QueryMapping("getAllChapters")
    public List<Chapter> getAllChapters(){
        List<Chapter> chapterList = unitAndChapterServices.getAllChapters();
        return chapterList;
    }

    @QueryMapping("getAllChapterPagination")
    public PaginationResponse getAllChapterPagination(@Argument(name = "input") ChapterDto response){
        return unitAndChapterServices.getAllChapterPagination(response);
    }



    @QueryMapping(name = "getLearnPath")
    public Page<Unit> getLearnPath(@Argument(name = "input")UnitDto unitDto){
        return unitAndChapterServices.getLearnPath(unitDto);
    }
}
