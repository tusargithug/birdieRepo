package net.thrymr.services;

import net.thrymr.dto.ChapterDto;
import net.thrymr.dto.UnitDto;
import net.thrymr.model.Chapter;
import net.thrymr.model.Unit;
import net.thrymr.utils.ApiResponse;

import java.util.List;

public interface UnitAndChapterServices {
    String saveUnit(UnitDto request);

    String updateUnitById(Long id,UnitDto unitDto);

    List<Unit> getAllUnit();


    List<Unit> getLearnPath(UnitDto unitDto);

    String deleteUnitById(Long id);

    String saveChapter(ChapterDto request);

    String updateChaptersById(Long id,ChapterDto  dto);

    List<Chapter> getAllChapters();

    String deleteChapterById(Long id);
    List<Chapter> getAllChapterPagination(ChapterDto chapterDto);
}
