package net.thrymr.impl;

import net.thrymr.dto.MoodInfoDto;
import net.thrymr.enums.MoodType;
import net.thrymr.model.master.MoodInfo;
import net.thrymr.repository.MoodInfoRepo;
import net.thrymr.repository.MoodIntensityRepo;
import net.thrymr.service.MoodInfoService;
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


/*
 *@author Chanda Veeresh
 *@version 1.0
 *@since  14-07-2022
 */
@Service
public class MoodInfoServiceImpl implements MoodInfoService {
    @Autowired
    private Environment environment;
    @Autowired
    private MoodInfoRepo moodInfoRepo;
    @Autowired
    private MoodIntensityRepo moodIntensityRepo;
    final Logger log = LoggerFactory.getLogger(MoodInfoServiceImpl.class);

//    private ApiResponse validateMoodRequest(MultipartFile request) {
//        if (!Validator.isObjectValid(request)) {
//            return new ApiResponse(HttpStatus.BAD_REQUEST, environment.getProperty("INVALID_REQUEST"));
//        }
//        return null;
//    }
//
//    private ApiResponse validateMoodRequestDto(List<MoodInfoDto> request) {
//        if (!Validator.isObjectValid(request)) {
//            return new ApiResponse(HttpStatus.BAD_REQUEST, environment.getProperty("INVALID_REQUEST"));
//        }
//        return null;
//    }

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

    private void setUserSearchKey(MoodInfo moodInfo) {
        String searchKey = "";
        if (Validator.isValid(moodInfo.getName())) {
            searchKey = searchKey + moodInfo.getName();
        }
        moodInfo.setSearchKey(searchKey);
    }

    @Override
    public ApiResponse saveMoodInfo(MultipartFile file) {
        List<MoodInfo> moodInfoList = new ArrayList<>();
        XSSFWorkbook workbook;
        try {
            workbook = new XSSFWorkbook(file.getInputStream());
        } catch (IOException e) {
            return new ApiResponse(HttpStatus.BAD_REQUEST, environment.getProperty("MOOD.IMPORT.FORMAT.FAILED"));
        }
        XSSFSheet worksheet = workbook.getSheetAt(0);
        if (worksheet.getLastRowNum() < 1) {
            return new ApiResponse(HttpStatus.BAD_REQUEST, environment.getProperty("MOOD.IMPORT.FORMAT.INVALID.DATA"));
        }
        System.out.println(worksheet.getLastRowNum());
        for (int index = 1; index <= worksheet.getLastRowNum(); index++) {
            if (index > 0) {
                try {
                    XSSFRow row = worksheet.getRow(index);

                    MoodInfo moodInfo = new MoodInfo();

                    if (row.getCell(1) != null) {
                        moodInfo.setName(getCellValue(row.getCell(1)));
                    }
                    if (row.getCell(2) != null) {
                        moodInfo.setSequence(Integer.parseInt(getCellValue(row.getCell(2))));
                    }
                    if (row.getCell(3) != null) {
                        moodInfo.setIntensityName(String.valueOf(row.getCell(3)));
                    }
                    if (row.getCell(4) != null) {
                        moodInfo.setMoodType(MoodType.stringToEnum(getCellValue(row.getCell(4))));
                    }
                    setUserSearchKey(moodInfo);
                    moodInfoList.add(moodInfo);
                    moodInfoList = moodInfoRepo.saveAll(moodInfoList);

                } catch (Exception e) {
                    log.error("Exception " + e);
                    return new ApiResponse(HttpStatus.BAD_REQUEST, environment.getProperty("MOOD.IMPORT.FORMAT.FAILED"));
                }
            }
        }


        return new ApiResponse(HttpStatus.OK, environment.getProperty("MOOD.IMPORT.SUCCESS"));
    }
    private MoodInfoDto setModelToDto(MoodInfo moodInfo) {
        MoodInfoDto moodInfoDto = new MoodInfoDto();
        moodInfoDto.setMoodName(moodInfo.getName());
        moodInfoDto.setSequence(moodInfo.getSequence());
        moodInfoDto.setMoodType(moodInfo.getMoodType().name());
        moodInfoDto.setIntensityName(moodInfo.getIntensityName());
        return moodInfoDto;
    }
    @Override
    public ApiResponse getAllMoods() {
        List<MoodInfo> moodInfos = moodInfoRepo.findAll();
        List<MoodInfoDto> moodInfoDtos = new ArrayList<>();
        if (!moodInfos.isEmpty()) {
            moodInfos.forEach(model -> moodInfoDtos.add(setModelToDto(model)));
            return new ApiResponse(HttpStatus.OK, environment.getProperty("MOOD_FOUND"), moodInfoDtos);
        } else {
            return new ApiResponse(HttpStatus.BAD_REQUEST, environment.getProperty("MOOD_NOTFOUND"), moodInfoDtos);
        }
    }

}

