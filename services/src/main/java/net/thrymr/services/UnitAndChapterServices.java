package net.thrymr.services;

import net.thrymr.dto.ChapterDto;
import net.thrymr.dto.UnitDto;
import net.thrymr.dto.response.PaginationResponse;
import net.thrymr.model.Chapter;
import net.thrymr.model.Unit;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UnitAndChapterServices {
    String saveUnit(UnitDto request);

    String updateUnitById(UnitDto unitDto);

    List<Unit> getAllUnit();


    Page<Unit> getLearnPath(UnitDto unitDto);

    String deleteUnitById(Long id);

    String saveChapter(ChapterDto request);

    String updateChaptersById(ChapterDto  dto);

    List<Chapter> getAllChapters();

    String deleteChapterById(Long id);
    PaginationResponse getAllChapterPagination(ChapterDto chapterDto);
}
