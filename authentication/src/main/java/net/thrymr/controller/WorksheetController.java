package net.thrymr.controller;

import net.thrymr.dto.PaginationResponse;
import net.thrymr.dto.WorksheetDto;
import net.thrymr.model.master.MtWorksheet;
import net.thrymr.services.WorksheetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class WorksheetController {


    @Autowired
    WorksheetService worksheetService;

    @QueryMapping("getAllWorksheet")
    public List<MtWorksheet> getAllWorksheet() {
        return worksheetService.getAllWorksheet();
    }

    @QueryMapping("getWorksheetById")
    MtWorksheet getWorksheetById(@Argument Long id) {
        return worksheetService.getWorksheetById(id);
    }

    @MutationMapping("createWorksheet")
    public String createWorksheet(@Argument(name = "input") WorksheetDto request) {
        return worksheetService.createWorksheet(request);
    }

    @MutationMapping("updateWorksheetById")
    public String updateWorksheetById(@Argument(name = "input") WorksheetDto request) {
        return worksheetService.updateWorksheetById(request);
    }

    @QueryMapping("getAllWorkSheetPagination")
    public PaginationResponse getAllWorkSheetPagination(@Argument(name = "input") WorksheetDto request) {
        return worksheetService.getAllWorkSheetPagination(request);
    }

    @MutationMapping("deleteWorksheetById")
    public String deleteWorksheetById(@Argument Long id) {
        return worksheetService.deleteWorksheetById(id);
    }

}