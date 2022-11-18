package net.thrymr.services;

import net.thrymr.dto.ChapterDto;
import net.thrymr.dto.UnitDto;
import net.thrymr.model.master.MtChapter;
import net.thrymr.model.master.MtUnit;

import java.util.List;

public interface UnitAndChapterServices {
    String saveUnit(UnitDto request);

    String updateUnitById(UnitDto unitDto);

    List<MtUnit> getAllUnit();


    List<MtUnit> getLearnPath(UnitDto unitDto);

    String deleteUnitById(Long id);

    String saveChapter(ChapterDto request);

    String updateChaptersById(ChapterDto dto);

    List<MtChapter> getAllChapters();

    String deleteChapterById(Long id);

    List<MtChapter> getAllChapterPagination(ChapterDto chapterDto);
}
