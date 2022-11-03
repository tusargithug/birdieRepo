package net.thrymr.services.impl;

import net.thrymr.dto.MoodIntensityDto;
import net.thrymr.dto.request.MoodSourceIntensityRequestDto;
import net.thrymr.model.AppUser;
import net.thrymr.model.UserMoodCheckIn;
import net.thrymr.model.master.MtMoodInfo;
import net.thrymr.model.master.MtMoodIntensity;
import net.thrymr.repository.*;
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
    private final AppUserRepo appUserRepo;

    public MoodIntensityServiceImpl(Environment environment, MoodInfoRepo moodInfoRepo, MoodIntensityRepo moodIntensityRepo, UserMoodCheckInRepo userMoodCheckInRepo, MoodSourceRepo moodSourceRepo, AppUserRepo appUserRepo) {
        this.environment = environment;
        this.moodInfoRepo = moodInfoRepo;
        this.moodIntensityRepo = moodIntensityRepo;
        this.userMoodCheckInRepo = userMoodCheckInRepo;
        this.moodSourceRepo = moodSourceRepo;
        this.appUserRepo = appUserRepo;
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

    private void setMoodIntensitySearchKey(MtMoodIntensity mtMoodIntensity) {
        String searchKey = "";
        if (Validator.isValid(mtMoodIntensity.getName())) {
            searchKey = searchKey + mtMoodIntensity.getName();
        }
        mtMoodIntensity.setSearchKey(searchKey);
    }

    @Override
    public ApiResponse saveintensity(MultipartFile file) {

        List<MtMoodIntensity> mtMoodIntensityList = new ArrayList<>();
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
                    MtMoodIntensity mtMoodIntensity = new MtMoodIntensity();

                    if (row.getCell(1) != null) {

                        mtMoodIntensity.setName(getCellValue(row.getCell(1)));
                    }
                    if (row.getCell(2) != null) {

                        mtMoodIntensity.setScore(Float.valueOf(getCellValue(row.getCell(2))));
                    }
                    if (row.getCell(3) != null) {
//                        Optional<MtMoodInfo> moodInfoOptional = moodInfoRepo.findById(Long.valueOf(getCellValue(row.getCell(3))));
//
//                        moodInfoOptional.ifPresent(moodInfo -> mtMoodIntensity.setSequence(Math.toIntExact(moodInfo.getSequence())));
                        mtMoodIntensity.setSequence(Integer.parseInt(getCellValue(row.getCell(3))));

                    }
                    if (row.getCell(4) != null) {
                        if (moodInfoRepo.existsById(Long.valueOf(getCellValue(row.getCell(4))))) {
                            Optional<MtMoodInfo> optionalMoodInfo = moodInfoRepo.findById(Long.valueOf(getCellValue(row.getCell(4))));

                            mtMoodIntensity.setMtMoodInfo(optionalMoodInfo.get());
                        }
                    }
                    mtMoodIntensityList.add(mtMoodIntensity);
                    setMoodIntensitySearchKey(mtMoodIntensity);
                    mtMoodIntensityList = moodIntensityRepo.saveAll(mtMoodIntensityList);

                } catch (Exception e) {
                    logger.error("Exception{} " , e);
                    return new ApiResponse(HttpStatus.BAD_REQUEST, environment.getProperty("MOOD_INTENSITY_IMPORT_FORMAT_FAILED"));
                }
            }
        }

        return new ApiResponse(HttpStatus.OK, environment.getProperty("MOOD_INTENSITY_FOUND"));
    }

    public ApiResponse getMoodIntensityByMoodInfoId(Long id) {
        Optional<MtMoodInfo> moodInfoOptional = moodInfoRepo.findById(id);
        List<MoodIntensityDto> moodIntensityDtos = new ArrayList<>();
        if (moodInfoOptional.isPresent()) {
            MtMoodInfo mtMoodInfo = moodInfoOptional.get();
            List<MtMoodIntensity> mtMoodIntensityList = mtMoodInfo.getIntensities();
            for (MtMoodIntensity mtMoodIntensity1 : mtMoodIntensityList) {
                MoodIntensityDto moodIntensityDto = new MoodIntensityDto();
                moodIntensityDto.setName(mtMoodIntensity1.getName());
                moodIntensityDto.setScore(mtMoodIntensity1.getScore());
                moodIntensityDto.setSequence(mtMoodIntensity1.getSequence());
                moodIntensityDtos.add(moodIntensityDto);}}
        return new ApiResponse(HttpStatus.OK,environment.getProperty("SUCCESS"),moodIntensityDtos);
        }

    @Override
    public ApiResponse moodIntensitySave(MoodIntensityDto request) {
        MtMoodIntensity intensity=new MtMoodIntensity();
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
        Optional<MtMoodIntensity> optionalMoodIntensity=moodIntensityRepo.findById(id);
        if(optionalMoodIntensity.isPresent()){
            MoodIntensityDto dto=entityToDto(optionalMoodIntensity.get());
            return new ApiResponse(HttpStatus.OK,environment.getProperty("SUCCESS"),dto);
        }

        return new ApiResponse(HttpStatus.OK,environment.getProperty("MOOD_INTENSITY_NOT_FOUND"));
    }

    @Override
    public ApiResponse deleteMoodIntensitiesById(Long id) {
        Optional<MtMoodIntensity> optionalMoodIntensity=moodIntensityRepo.findById(id);
        if(optionalMoodIntensity.isPresent()){
            moodIntensityRepo.delete(optionalMoodIntensity.get());
            return new ApiResponse(HttpStatus.OK,environment.getProperty("MOOD_INTENSITY_DELETED"));
        }

        return new ApiResponse(HttpStatus.OK,environment.getProperty("MOOD_INTENSITY_NOT_FOUND"));
    }

    @Override
    public ApiResponse getAllMoodIntensities() {
        List<MtMoodIntensity> mtMoodIntensityList =moodIntensityRepo.findAll();

        if(!mtMoodIntensityList.isEmpty()){
          List<MoodIntensityDto>  moodIntensityDtoList =  mtMoodIntensityList.stream().map(this::entityToDto).toList();
            return new ApiResponse(HttpStatus.OK,environment.getProperty("SUCCESS"),moodIntensityDtoList);
        }


        return new ApiResponse(HttpStatus.OK,environment.getProperty("MOOD_INTENSITY_NOT_FOUND"));
    }

    @Override
    public ApiResponse getAllMoodIntensitiesByMoodInfoId(Long id) {
        List<MtMoodIntensity> mtMoodIntensityList =moodIntensityRepo.findByMtMoodInfoId(id);
        if(!mtMoodIntensityList.isEmpty()){
            List<MoodIntensityDto>  moodIntensityDtoList =  mtMoodIntensityList.stream().map(this::entityToDto).toList();
            return new ApiResponse(HttpStatus.OK,"",moodIntensityDtoList);
        }
        return new ApiResponse(HttpStatus.OK,environment.getProperty("MOOD_INTENSITY_NOT_FOUND"));
    }

    @Override
    public ApiResponse updateMoodIntensity(MoodSourceIntensityRequestDto request) {

        AppUser user= CommonUtil.getAppUser();
        Optional<MtMoodIntensity>optionalMoodIntensity=moodIntensityRepo.findById(request.getIntensityId());
        UserMoodCheckIn userMoodCheckIn=new UserMoodCheckIn();
        userMoodCheckIn.setAppUser(user);
        if(optionalMoodIntensity.isPresent()){
            if(Validator.isValid(request.getIntensityDescription())){
                optionalMoodIntensity.get().setDescription(request.getIntensityDescription());
            }
            userMoodCheckIn.setIntensities(optionalMoodIntensity.stream().toList());
        }
          if(Validator.isValid(request.getDescription())){
              userMoodCheckIn.setDescription(request.getDescription());
          }

          userMoodCheckInRepo.save(userMoodCheckIn);

        return new ApiResponse(HttpStatus.OK,environment.getProperty("USER_CHECKED_IN_SAVE"));
    }


    private MoodIntensityDto entityToDto(MtMoodIntensity request){
        MoodIntensityDto dto=new MoodIntensityDto();
        dto.setId(request.getId());
        dto.setName(request.getName());
        dto.setDescription(request.getDescription());
        dto.setEmoji(request.getEmoji());
      //  dto.setMoodInfoDto(request.getMoodInfo());
       return dto;
    }

    @Override
    public List<MtMoodIntensity> getAllMoodIntensity() {
        return moodIntensityRepo.findAll();
    }

    @Override
    public String deleteUserMoodCheckInById(Long id) {
        Optional<MtMoodIntensity>optionalMtMoodIntensity=moodIntensityRepo.findById(id);
        optionalMtMoodIntensity.ifPresent(moodIntensityRepo::delete);
        return "Intensity deleted successfully";
    }

    @Override
    public String createUserMoodCheckIn(MoodSourceIntensityRequestDto request) {
        UserMoodCheckIn userMoodCheckIn = new UserMoodCheckIn();
        if (Validator.isValid(request.getIntensityId())) {
            List<MtMoodIntensity> optionalMoodIntensity = moodIntensityRepo.findAll();
            if (!optionalMoodIntensity.isEmpty()) {
                userMoodCheckIn.setIntensities(optionalMoodIntensity);
            }
                userMoodCheckIn.setDescription(request.getIntensityDescription());
                userMoodCheckIn.setIntensities(optionalMoodIntensity.stream().toList());
            }
            if (Validator.isValid(request.getDescription())) {
                userMoodCheckIn.setDescription(request.getDescription());
            }
            if(Validator.isValid(request.getAppUserId())){
                Optional<AppUser> optionalAppUser=appUserRepo.findById(request.getAppUserId());
                if(optionalAppUser.isPresent()){
                    userMoodCheckIn.setAppUser(optionalAppUser.get());
                }
            }
            userMoodCheckInRepo.save(userMoodCheckIn);

            return environment.getProperty("USER_CHECKED_IN_SAVE");
        }
}

