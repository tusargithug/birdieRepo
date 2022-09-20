package net.thrymr.services.impl;

import net.thrymr.dto.MoodIntensityDto;
import net.thrymr.dto.request.MoodSourceIntensityRequestDto;
import net.thrymr.model.AppUser;
import net.thrymr.model.UserMoodCheckIn;
import net.thrymr.model.master.MoodInfo;
import net.thrymr.model.master.MoodIntensity;
import net.thrymr.model.master.MoodSource;
import net.thrymr.repository.MoodInfoRepo;
import net.thrymr.repository.MoodIntensityRepo;
import net.thrymr.repository.MoodSourceRepo;
import net.thrymr.repository.UserMoodCheckInRepo;
import net.thrymr.services.MoodIntensityService;
import net.thrymr.utils.ApiResponse;
import net.thrymr.utils.CommonUtil;
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

    private final UserMoodCheckInRepo userMoodCheckInRepo;

    private final MoodSourceRepo moodSourceRepo;

    public MoodIntensityServiceImpl(Environment environment, MoodInfoRepo moodInfoRepo, MoodIntensityRepo moodIntensityRepo, UserMoodCheckInRepo userMoodCheckInRepo, MoodSourceRepo moodSourceRepo) {
        this.environment = environment;
        this.moodInfoRepo = moodInfoRepo;
        this.moodIntensityRepo = moodIntensityRepo;
        this.userMoodCheckInRepo = userMoodCheckInRepo;
        this.moodSourceRepo = moodSourceRepo;
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
        return new ApiResponse(HttpStatus.OK,"",moodIntensityDtos);
        }

    @Override
    public ApiResponse moodIntensitySave(MoodIntensityDto request) {
        MoodIntensity intensity=new MoodIntensity();
        intensity.setName(request.getName());
        intensity.setDescription(request.getDescription());
        intensity.setScore(request.getScore());
        intensity.setEmoji(request.getEmoji());
        intensity.setSequence(request.getSequence());
        // need to set mood info
       // intensity.setMoodInfo(request.getMoodInfoDto());

        moodIntensityRepo.save(intensity);

        return new ApiResponse(HttpStatus.OK,environment.getProperty("MOOD_INTENSITY_SAVED"));
    }

    @Override
    public ApiResponse getMoodIntensitiesById(Long id) {
        Optional<MoodIntensity> optionalMoodIntensity=moodIntensityRepo.findById(id);
        if(optionalMoodIntensity.isPresent()){
            MoodIntensityDto dto=entityToDto(optionalMoodIntensity.get());
            return new ApiResponse(HttpStatus.OK,environment.getProperty("SUCCESS"),dto);
        }

        return new ApiResponse(HttpStatus.OK,environment.getProperty("MOOD_INTENSITY_NOT_FOUND"));
    }

    @Override
    public ApiResponse deleteMoodIntensitiesById(Long id) {
        Optional<MoodIntensity> optionalMoodIntensity=moodIntensityRepo.findById(id);
        if(optionalMoodIntensity.isPresent()){
            moodIntensityRepo.delete(optionalMoodIntensity.get());
            return new ApiResponse(HttpStatus.OK,environment.getProperty("MOOD_INTENSITY_DELETED"));
        }

        return new ApiResponse(HttpStatus.OK,environment.getProperty("MOOD_INTENSITY_NOT_FOUND"));
    }

    @Override
    public ApiResponse getAllMoodIntensities() {
        List<MoodIntensity> moodIntensityList=moodIntensityRepo.findAll();

        if(!moodIntensityList.isEmpty()){
          List<MoodIntensityDto>  moodIntensityDtoList =  moodIntensityList.stream().map(this::entityToDto).toList();
            return new ApiResponse(HttpStatus.OK,environment.getProperty("SUCCESS"),moodIntensityDtoList);
        }


        return new ApiResponse(HttpStatus.OK,environment.getProperty("MOOD_INTENSITY_NOT_FOUND"));
    }

    @Override
    public ApiResponse getAllMoodIntensitiesByMoodInfoId(Long id) {
        List<MoodIntensity>moodIntensityList=moodIntensityRepo.findByMoodInfoId(id);
        if(!moodIntensityList.isEmpty()){
            List<MoodIntensityDto>  moodIntensityDtoList =  moodIntensityList.stream().map(this::entityToDto).toList();
            return new ApiResponse(HttpStatus.OK,"",moodIntensityDtoList);
        }
        return new ApiResponse(HttpStatus.OK,environment.getProperty("MOOD_INTENSITY_NOT_FOUND"));
    }

    @Override
    public ApiResponse updateMoodIntensity(MoodSourceIntensityRequestDto request) {

        AppUser user= CommonUtil.getAppUser();
        Optional<MoodIntensity>optionalMoodIntensity=moodIntensityRepo.findById(request.getIntensityId());
        UserMoodCheckIn userMoodCheckIn=new UserMoodCheckIn();
        userMoodCheckIn.setAppUser(user);
        if(optionalMoodIntensity.isPresent()){
            if(Validator.isValid(request.getIntensityDescription())){
                optionalMoodIntensity.get().setDescription(request.getIntensityDescription());
            }
            userMoodCheckIn.setIntensities(optionalMoodIntensity.stream().toList());
        }
        List<MoodSource>moodSourceList=moodSourceRepo.findAllByIdIn(request.getSourceIds());
          if(!moodSourceList.isEmpty()){
              userMoodCheckIn.setSources(moodSourceList);
          }
          if(Validator.isValid(request.getMoreInfo())){
              userMoodCheckIn.setMoreInfo(request.getMoreInfo());
          }

          userMoodCheckInRepo.save(userMoodCheckIn);

        return new ApiResponse(HttpStatus.OK,environment.getProperty("USER_CHECKED_IN_SAVE"));
    }


    private MoodIntensityDto entityToDto(MoodIntensity request){
        MoodIntensityDto dto=new MoodIntensityDto();
        dto.setId(request.getId());
        dto.setName(request.getName());
        dto.setDescription(request.getDescription());
        dto.setEmoji(request.getEmoji());
      //  dto.setMoodInfoDto(request.getMoodInfo());
       return dto;
    }
}

