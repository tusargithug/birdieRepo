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
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class MoodInfoServiceImpl implements MoodInfoService {
    private final Logger logger = LoggerFactory.getLogger(MoodInfoServiceImpl.class);

    private final Environment environment;

    private final MoodInfoRepo moodInfoRepo;

    private final MoodIntensityRepo moodIntensityRepo;


    public MoodInfoServiceImpl(Environment environment, MoodInfoRepo moodInfoRepo, MoodIntensityRepo moodIntensityRepo) {
        this.environment = environment;
        this.moodInfoRepo = moodInfoRepo;
        this.moodIntensityRepo = moodIntensityRepo;
    }

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

    private void setUserSearchKey(MtMoodInfo mtMoodInfo) {
        String searchKey = "";
        if (Validator.isValid(mtMoodInfo.getName())) {
            searchKey = searchKey + mtMoodInfo.getName();
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
                    setUserSearchKey(mtMoodInfo);
                    mtMoodInfoList.add(mtMoodInfo);
                    mtMoodInfoList = moodInfoRepo.saveAll(mtMoodInfoList);

                } catch (Exception e) {
                    logger.error("Exception{}; " , e);
                    return new ApiResponse(HttpStatus.BAD_REQUEST, environment.getProperty("MOOD_IMPORT_FORMAT_FAILED"));
                }
            }
        }


        return new ApiResponse(HttpStatus.OK, environment.getProperty("MOOD_IMPORT_SUCCESS"));
    }

    @Override
    public ApiResponse getAllMoods() {
        List<MtMoodInfo> mtMoodInfos = moodInfoRepo.findAll();
        List<MoodInfoDto> moodInfoDtos = new ArrayList<>();
        if (!mtMoodInfos.isEmpty()) {
            mtMoodInfos.forEach(model -> moodInfoDtos.add(setModelToDto(model)));
            return new ApiResponse(HttpStatus.OK, environment.getProperty("MOOD_FOUND"), moodInfoDtos);
        }

            return new ApiResponse(HttpStatus.BAD_REQUEST, environment.getProperty("MOOD_NOT_FOUND"), moodInfoDtos);

    }

    @Override
    public ApiResponse getMoodInfoById(Long id) {
        Optional<MtMoodInfo>optionalMoodInfo=moodInfoRepo.findById(id);
        return optionalMoodInfo.map(moodInfo -> new ApiResponse(HttpStatus.OK, environment.getProperty("MOOD_FOUND"), this.setModelToDto(moodInfo))).orElseGet(() -> new ApiResponse(HttpStatus.BAD_REQUEST, environment.getProperty("MOOD_NOT_FOUND")));

    }

    @Override
    public ApiResponse deleteMoodInfoById(Long id) {
        Optional<MtMoodInfo>optionalMoodInfo=moodInfoRepo.findById(id);

        if(optionalMoodInfo.isPresent()){
            MtMoodInfo mtMoodInfo =optionalMoodInfo.get();
            moodInfoRepo.delete(mtMoodInfo);
            return new ApiResponse(HttpStatus.OK, environment.getProperty("MOOD_INFO_DELETE"));
        }

        return new ApiResponse(HttpStatus.BAD_REQUEST, environment.getProperty("MOOD_NOT_FOUND"));
    }
    private MoodInfoDto setModelToDto(MtMoodInfo mtMoodInfo) {
        MoodInfoDto moodInfoDto = new MoodInfoDto();
        moodInfoDto.setId(mtMoodInfo.getId());
        moodInfoDto.setMoodName(mtMoodInfo.getName());
        moodInfoDto.setSequence(mtMoodInfo.getSequence());
        moodInfoDto.setMoodType(mtMoodInfo.getMoodType().name());
        moodInfoDto.setIntensityName(mtMoodInfo.getIntensityName());
        return moodInfoDto;
    }
}

