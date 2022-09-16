package net.thrymr.services.impl;

import net.thrymr.dto.MoodIntensityDto;
import net.thrymr.model.master.MoodInfo;
import net.thrymr.model.master.MoodIntensity;
import net.thrymr.repository.MoodInfoRepo;
import net.thrymr.repository.MoodIntensityRepo;
import net.thrymr.services.MoodIntensityService;
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
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class MoodIntensityServiceImpl implements MoodIntensityService {
    private final Logger logger = LoggerFactory.getLogger(MoodIntensityServiceImpl.class);

    private final Environment environment;


    private final MoodInfoRepo moodInfoRepo;

    private final MoodIntensityRepo moodIntensityRepo;

    public MoodIntensityServiceImpl(Environment environment, MoodInfoRepo moodInfoRepo, MoodIntensityRepo moodIntensityRepo) {
        this.environment = environment;
        this.moodInfoRepo = moodInfoRepo;
        this.moodIntensityRepo = moodIntensityRepo;
    }

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

    private void setMoodIntensitySearchKey(MoodIntensity moodIntensity) {
        String searchKey = "";
        if (Validator.isValid(moodIntensity.getName())) {
            searchKey = searchKey + moodIntensity.getName();
        }
        moodIntensity.setSearchKey(searchKey);
    }

    @Override
    public ApiResponse saveintensity(MultipartFile file) {

        List<MoodIntensity> moodIntensityList = new ArrayList<>();
        XSSFWorkbook workbook;
        try {
            workbook = new XSSFWorkbook(file.getInputStream());
        } catch (IOException e) {
            return new ApiResponse(HttpStatus.BAD_REQUEST, environment.getProperty("MOOD_INTENSITY_IMPORT_FORMAT_FAILED"));
        }
        XSSFSheet worksheet = workbook.getSheetAt(0);

        if (worksheet.getLastRowNum() < 1) {
            return new ApiResponse(HttpStatus.BAD_REQUEST, environment.getProperty("MOOD_INTENSITY_IMPORT_FORMAT_INVALID_DATA"));
        }

        for (int index = 1; index <= worksheet.getLastRowNum(); index++) {
            if (index > 0) {
                try {
                    XSSFRow row = worksheet.getRow(index);
                    MoodIntensity moodIntensity = new MoodIntensity();

                    if (row.getCell(1) != null) {

                        moodIntensity.setName(getCellValue(row.getCell(1)));
                    }
                    if (row.getCell(2) != null) {

                        moodIntensity.setScore(Float.valueOf(getCellValue(row.getCell(2))));
                    }
                    if (row.getCell(3) != null) {
                        Optional<MoodInfo> moodInfoOptional = moodInfoRepo.findById(Long.valueOf(getCellValue(row.getCell(3))));

                        moodInfoOptional.ifPresent(moodInfo -> moodIntensity.setSequence(Math.toIntExact(moodInfo.getSequence())));
                    }
                    if (row.getCell(4) != null) {
                        if (moodInfoRepo.existsById(Long.valueOf(getCellValue(row.getCell(4))))) {
                            Optional<MoodInfo> optionalMoodInfo = moodInfoRepo.findById(Long.valueOf(getCellValue(row.getCell(4))));

                            moodIntensity.setMoodInfo(optionalMoodInfo.get());
                        }
                    }
                    moodIntensityList.add(moodIntensity);
                    setMoodIntensitySearchKey(moodIntensity);
                    moodIntensityList = moodIntensityRepo.saveAll(moodIntensityList);

                } catch (Exception e) {
                    logger.error("Exception{} " , e);
                    return new ApiResponse(HttpStatus.BAD_REQUEST, environment.getProperty("MOOD_INTENSITY_IMPORT_FORMAT_FAILED"));
                }
            }
        }

        return new ApiResponse(HttpStatus.OK, environment.getProperty("MOOD_INTENSITY_FOUND"));
    }

    public ApiResponse getMoodIntensityByMoodInfoId(Long id) {
        Optional<MoodInfo> moodInfoOptional = moodInfoRepo.findById(id);
        List<MoodIntensityDto> moodIntensityDtos = new ArrayList<>();
        if (moodInfoOptional.isPresent()) {
            MoodInfo moodInfo = moodInfoOptional.get();
            List<MoodIntensity> moodIntensityList = moodInfo.getIntensities();
            for (MoodIntensity moodIntensity1 : moodIntensityList) {
                MoodIntensityDto moodIntensityDto = new MoodIntensityDto();
                moodIntensityDto.setName(moodIntensity1.getName());
                moodIntensityDto.setScore(moodIntensity1.getScore());
                moodIntensityDto.setSequence(moodIntensity1.getSequence());
                moodIntensityDtos.add(moodIntensityDto);}}
        return new ApiResponse(HttpStatus.OK,moodIntensityDtos);
        }
}

