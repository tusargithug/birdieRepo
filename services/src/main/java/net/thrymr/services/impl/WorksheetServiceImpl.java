package net.thrymr.services.impl;

import net.thrymr.dto.WorksheetDto;
import net.thrymr.model.FileEntity;
import net.thrymr.model.master.MtWorksheet;
import net.thrymr.repository.FileRepo;
import net.thrymr.repository.WorksheetRepo;
import net.thrymr.services.WorksheetService;
import net.thrymr.utils.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class WorksheetServiceImpl implements WorksheetService {

    @Autowired
    WorksheetRepo worksheetRepo;
    @Autowired
    FileRepo fileRepo;



    @Override
    public List<MtWorksheet> getAllWorksheet() {

        List<MtWorksheet> worksheetList = worksheetRepo.findAll();
        if (!worksheetList.isEmpty()) {
            return worksheetList.stream().filter(obj -> obj.getIsActive().equals(Boolean.TRUE)).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    @Override
    public MtWorksheet getWorksheetById(Long id) {

        if (Validator.isValid(id)) {
            Optional<MtWorksheet> optionalMtWorksheet = worksheetRepo.findById(id);
            if (optionalMtWorksheet.isPresent() && optionalMtWorksheet.get().getIsActive().equals(Boolean.TRUE)) {
                return optionalMtWorksheet.get();
            }
        }
        return new MtWorksheet();
    }

    @Override
    public String createWorksheet(WorksheetDto request) {
        worksheetRepo.save(dtoToWorksheetEntity(request));
        return "create Worksheet successfully";
    }

    @Override
    public String updateWorksheetById(WorksheetDto request) {

        MtWorksheet mtWorksheet = null;

        if (Validator.isValid(request.getId())) {
            Optional<MtWorksheet> optionalMtWorksheet = worksheetRepo.findById(request.getId());
            if (optionalMtWorksheet.isPresent()) {
                mtWorksheet = optionalMtWorksheet.get();
                if (Validator.isValid(request.getName())) {
                    mtWorksheet.setName(request.getName());
                }
                if (Validator.isValid(request.getDescription())) {
                    mtWorksheet.setDescription(request.getDescription());
                }
                if (request.getIsActive() != null) {
                    mtWorksheet.setIsActive(request.getIsActive());
                }
                if(Validator.isValid(request.getFileId())){
                    Optional<FileEntity> fileEntity=fileRepo.findByFileId(request.getFileId());
                    if(fileEntity.isPresent()){
                        mtWorksheet.setFile(fileEntity.get());
                    }
                }
                mtWorksheet.setSearchKey(getAppUserSearchKey(mtWorksheet));
                worksheetRepo.save(mtWorksheet);
                return "Worksheet updated successfully";
            }
        }
        return "Worksheet id not found";
    }

    @Override
    public String deleteWorksheetById(Long id) {

        MtWorksheet mtWorksheet = null;

        if (Validator.isValid(id)) {
            Optional<MtWorksheet> optionalMtWorksheet = worksheetRepo.findById(id);
            if (optionalMtWorksheet.isPresent()) {
                mtWorksheet = optionalMtWorksheet.get();
                mtWorksheet.setIsActive(Boolean.FALSE);
                mtWorksheet.setIsDeleted(Boolean.TRUE);
                worksheetRepo.save(mtWorksheet);
                return "Worksheet deleted successfully";
            }
        }
        return "Worksheet id not found";
    }

    private MtWorksheet dtoToWorksheetEntity(WorksheetDto request) {
        MtWorksheet mtWorksheet = new MtWorksheet();
        mtWorksheet.setName(request.getName());
        mtWorksheet.setDescription(request.getDescription());
        mtWorksheet.setIsActive(request.getIsActive());
        if(Validator.isValid(request.getFileId())){
            Optional<FileEntity> fileEntity=fileRepo.findByFileId(request.getFileId());
            if(fileEntity.isPresent()){
                mtWorksheet.setFile(fileEntity.get());
            }
        }
        mtWorksheet.setSearchKey(getAppUserSearchKey(mtWorksheet));
        return mtWorksheet;
    }

    public String getAppUserSearchKey(MtWorksheet worksheet) {
        String searchKey = "";
        if (worksheet.getName() != null) {
            searchKey = searchKey + " " + worksheet.getName();
        }
        if (worksheet.getDescription() != null) {
            searchKey = searchKey + " " + worksheet.getDescription();
        }
        if (worksheet.getFile() != null) {
            searchKey = searchKey + " " + worksheet.getFile();
        }
        return searchKey;
    }
}
