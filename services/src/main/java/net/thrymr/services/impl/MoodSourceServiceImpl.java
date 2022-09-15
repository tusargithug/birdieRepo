package net.thrymr.services.impl;

import net.thrymr.dto.MoodSourceDto;
import net.thrymr.enums.Category;
import net.thrymr.model.master.MoodSource;
import net.thrymr.repository.MoodSourceRepo;
import net.thrymr.services.MoodSourceService;
import net.thrymr.utils.ApiResponse;
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


@Service
public class MoodSourceServiceImpl implements MoodSourceService {
    private final Logger logger = LoggerFactory.getLogger(MoodSourceServiceImpl.class);

   private final Environment environment;

    private final MoodSourceRepo moodSourceRepo;

    public MoodSourceServiceImpl(Environment environment, MoodSourceRepo moodSourceRepo) {
        this.environment = environment;
        this.moodSourceRepo = moodSourceRepo;
    }


    @Override
    public ApiResponse addMoodSourceByExcel(MultipartFile file) {
        List<MoodSource> moodSourceList = new ArrayList<>();
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
        for (int index = 1; index <= worksheet.getLastRowNum(); index++) {
            if (index > 0) {
                try {
                    XSSFRow row = worksheet.getRow(index);

                    MoodSource moodSource = new MoodSource();

                    if (row.getCell(1) != null) {
                        moodSource.setName(getCellValue(row.getCell(1)));
                    }

                    if (row.getCell(2) != null) {
                        moodSource.setCategory(Category.stringToEnum(getCellValue(row.getCell(2))));
                    }

                    if (row.getCell(3) != null) {
                        moodSource.setSequence(Integer.parseInt(getCellValue(row.getCell(3))));
                    }
                    moodSourceList.add(moodSource);
                    moodSourceList = moodSourceRepo.saveAll(moodSourceList);
                } catch (Exception e) {
                    logger.error("Exception{} " , e);
                    return new ApiResponse(HttpStatus.BAD_REQUEST, environment.getProperty("MOOD.IMPORT.FORMAT.FAILED"));
                }
            }
        }
        return new ApiResponse(HttpStatus.OK, environment.getProperty("MOOD.IMPORT.SUCCESS"));
    }

    @Override
    public ApiResponse getAllMoodSources() {
        List<MoodSource> moodSourceList = moodSourceRepo.findAll();
       List<MoodSourceDto> moodSourceDtoList = new ArrayList<>();
       if (!moodSourceList.isEmpty()) {
    moodSourceList.forEach(moodSource -> moodSourceDtoList.add(setModelToDto(moodSource)));
           return new ApiResponse(HttpStatus.OK, environment.getProperty("MOOD_SOURCE_FOUND"), moodSourceDtoList);
       }
       else {
           return new ApiResponse(HttpStatus.BAD_REQUEST, environment.getProperty("MOOD_SOURCE_NOTFOUND"), moodSourceDtoList);
       }
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

    private MoodSourceDto setModelToDto(MoodSource moodSource) {
        MoodSourceDto moodSourceDto = new MoodSourceDto();
        moodSourceDto.setName(moodSource.getName());
        moodSourceDto.setCategory(moodSource.getCategory().name());
        moodSourceDto.setDescription(moodSource.getDescription());
        moodSourceDto.setSequence(moodSource.getSequence());
        return moodSourceDto;
    }

}
