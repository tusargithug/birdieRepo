package net.thrymr.services;

import net.thrymr.dto.ChapterDto;
import net.thrymr.dto.UnitDto;
import net.thrymr.dto.response.ChapterResponse;
import net.thrymr.dto.response.PaginationResponse;
import net.thrymr.dto.response.UnitResponse;
import net.thrymr.model.Chapter;
import net.thrymr.model.Unit;

import java.util.List;

public interface UnitAndChapterServices {
    String saveUnit(UnitDto request);

    String updateUnitById(UnitDto unitDto);

    List<Unit> getAllUnit();


    PaginationResponse getLearnPath(UnitDto unitDto);

    String deleteUnitById(Long id);

    Chapter saveChapter(ChapterDto request);

    Chapter updateChaptersById(ChapterDto  dto);

    List<Chapter> getAllChapters();

    String deleteChapterById(Long id);
    PaginationResponse getAllChapterPagination(ChapterDto chapterDto);

    ChapterResponse getChapterById(Long id);

    Unit getUnitById(Long id);

    Unit getUnitBySequence(UnitDto request);

    Chapter getChapterBySequence(Integer sequence);
}
