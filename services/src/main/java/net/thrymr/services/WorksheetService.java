package net.thrymr.services;

import net.thrymr.dto.WorksheetDto;
import net.thrymr.model.master.MtWorksheet;

import java.util.List;

public interface WorksheetService {

    List<MtWorksheet> getAllWorksheet();

    MtWorksheet getWorksheetById(Long id);

    String createWorksheet(WorksheetDto request);

    String updateWorksheetById(WorksheetDto request);

    String deleteWorksheetById(Long id);


}
