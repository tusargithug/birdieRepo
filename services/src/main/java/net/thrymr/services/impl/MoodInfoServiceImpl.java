package net.thrymr.services.impl;

import net.thrymr.dto.MoodInfoDto;
import net.thrymr.enums.MoodType;
import net.thrymr.model.master.MtMoodInfo;
import net.thrymr.repository.MoodInfoRepo;
import net.thrymr.repository.MoodIntensityRepo;
import net.thrymr.services.MoodInfoService;
import net.thrymr.utils.ApiResponse;
import net.thrymr.utils.Validator;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class MoodInfoServiceImpl implements MoodInfoService {
    Logger logger = LoggerFactory.getLogger(MoodInfoServiceImpl.class);

    @Autowired
    Environment environment;
    @Autowired
    MoodInfoRepo moodInfoRepo;
    @Autowired
    MoodIntensityRepo moodIntensityRepo;


    private String getCellValue(XSSFCell cell) {
        String value;
        if (cell.getCellType().equals(CellType.NUMERIC)) {
            value = NumberToTextConverter.toText(cell.getNumericCellValue());
        } else if (cell.getCellType().equals(CellType.STRING)) {
            value = cell.getStringCellValue();
        } else if (cell.getCellType().equals(CellType.BOOLEAN)) {
            value = String.valueOf(cell.getBooleanCellValue());
        } else if (cell.getCellType().equals(CellType.BLANK) || cell.getCellType().equals(CellType.ERROR)) {
            value = "";
        } else {
            value = cell.getRawValue();
        }
        return value;
    }

    private void setUserSearchKey(MtMoodInfo mtMoodInfo) {
        String searchKey = "";
        if (Validator.isValid(mtMoodInfo.getName())) {
            searchKey = searchKey + mtMoodInfo.getName();
        }
        if (mtMoodInfo.getIsActive() != null && mtMoodInfo.getIsActive().equals(Boolean.FALSE)) {
            searchKey = searchKey + " " + "Inactive";
        } else {
            searchKey = searchKey + " " + "Active";
        }
        mtMoodInfo.setSearchKey(searchKey);
    }

    @Override
    public ApiResponse saveMoodInfo(MultipartFile file) {
        List<MtMoodInfo> mtMoodInfoList = new ArrayList<>();
        XSSFWorkbook workbook;
        try {
            workbook = new XSSFWorkbook(file.getInputStream());
        } catch (IOException e) {
            return new ApiResponse(HttpStatus.BAD_REQUEST, environment.getProperty("MOOD_IMPORT_FORMAT_FAILED"));
        }
        XSSFSheet worksheet = workbook.getSheetAt(0);
        if (worksheet.getLastRowNum() < 1) {
            return new ApiResponse(HttpStatus.BAD_REQUEST, environment.getProperty("MOOD_IMPORT_FORMAT_INVALID_DATA"));
        }
        System.out.println(worksheet.getLastRowNum());
        for (int index = 1; index <= worksheet.getLastRowNum(); index++) {
            if (index > 0) {
                try {
                    XSSFRow row = worksheet.getRow(index);

                    MtMoodInfo mtMoodInfo = new MtMoodInfo();

                    if (row.getCell(1) != null) {
                        mtMoodInfo.setName(getCellValue(row.getCell(1)));
                    }
                    if (row.getCell(2) != null) {
                        mtMoodInfo.setSequence(Integer.parseInt(getCellValue(row.getCell(2))));
                    }
                    if (row.getCell(3) != null) {
                        mtMoodInfo.setIntensityName(String.valueOf(row.getCell(3)));
                    }
                    if (row.getCell(4) != null) {
                        mtMoodInfo.setMoodType(MoodType.stringToEnum(getCellValue(row.getCell(4))));
                    }
                    if(row.getCell(5) != null) {
                        mtMoodInfo.setDescription(getCellValue(row.getCell(5)));
                    }
                    setUserSearchKey(mtMoodInfo);
                    mtMoodInfoList.add(mtMoodInfo);
                    mtMoodInfoList = moodInfoRepo.saveAll(mtMoodInfoList);

                } catch (Exception e) {
                    logger.error("Exception{}; ", e);
                    return new ApiResponse(HttpStatus.BAD_REQUEST, environment.getProperty("MOOD_IMPORT_FORMAT_FAILED"));
                }
            }
        }
        return new ApiResponse(HttpStatus.OK, environment.getProperty("MOOD_IMPORT_SUCCESS"));
    }


    @Override
    public MtMoodInfo getMoodInfoById(Long id) {
        MtMoodInfo mtMoodInfo = null;
        if (Validator.isValid(id)) {
            Optional<MtMoodInfo> optionalMoodInfo = moodInfoRepo.findById(id);
            if (optionalMoodInfo.isPresent() && optionalMoodInfo.get().getIsActive().equals(Boolean.TRUE)) {
                mtMoodInfo = optionalMoodInfo.get();
                return mtMoodInfo;
            }
        }
        return new MtMoodInfo();
    }

    @Override
    public String deleteMoodInfoById(Long id) {
        MtMoodInfo mtMoodInfo = null;
        if (Validator.isValid(id)) {
            Optional<MtMoodInfo> optionalMoodInfo = moodInfoRepo.findById(id);
            if (optionalMoodInfo.isPresent()) {
                mtMoodInfo = optionalMoodInfo.get();
                mtMoodInfo.setIsActive(Boolean.FALSE);
                mtMoodInfo.setIsDeleted(Boolean.TRUE);
                moodInfoRepo.save(mtMoodInfo);
                return "Mood info deleted successfully";
            }
        }
        return "This mood info id not present in database";
    }

    @Override
    public List<MtMoodInfo> getAllMoodInfo(String searchKey) {
        List<MtMoodInfo> mtMoodInfoList;
        if(Validator.isValid(searchKey)) {
            mtMoodInfoList = moodInfoRepo.findBySearchKeyContainingIgnoreCase(searchKey);
        } else {
            mtMoodInfoList = moodInfoRepo.findAll();
        }

        if (!mtMoodInfoList.isEmpty()) {
            return mtMoodInfoList.stream().filter(mtMoodInfo -> mtMoodInfo.getIsActive().equals(Boolean.TRUE)).collect(Collectors.toList());
        }

        return new ArrayList<>();
    }

    @Override
    public String updateMoodInfoById(MoodInfoDto request) {
        MtMoodInfo mtMoodInfo = null;
        if (Validator.isValid(request.getId())) {
            Optional<MtMoodInfo> optionalMtMoodInfo = moodInfoRepo.findById(request.getId());
            if (optionalMtMoodInfo.isPresent()) {
                mtMoodInfo = optionalMtMoodInfo.get();
                if (Validator.isValid(request.getMoodType())) {
                    mtMoodInfo.setMoodType(MoodType.valueOf(request.getMoodType()));
                }
                if (Validator.isValid(request.getIntensityName())) {
                    mtMoodInfo.setIntensityName(request.getIntensityName());
                }
                if (Validator.isValid(request.getSequence())) {
                    mtMoodInfo.setSequence(request.getSequence());
                }
                if (Validator.isValid(request.getEmoji())) {
                    mtMoodInfo.setEmoji(request.getEmoji());
                }
                moodInfoRepo.save(mtMoodInfo);
                return "Mood info updated successfully";
            }
        }
        return "This mood info id not present in database";
    }
}


