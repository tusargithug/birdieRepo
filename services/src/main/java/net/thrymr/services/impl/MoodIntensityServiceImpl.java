package net.thrymr.services.impl;

import net.thrymr.dto.request.MoodSourceIntensityRequestDto;
import net.thrymr.model.AppUser;
import net.thrymr.model.UserMoodCheckIn;
import net.thrymr.model.UserMoodSourceCheckedIn;
import net.thrymr.model.master.MtMoodInfo;
import net.thrymr.model.master.MtMoodIntensity;
import net.thrymr.model.master.MtMoodSource;
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
public class MoodIntensityServiceImpl implements MoodIntensityService {
    Logger logger = LoggerFactory.getLogger(MoodIntensityServiceImpl.class);

    @Autowired
    Environment environment;
    @Autowired
    MoodInfoRepo moodInfoRepo;
    @Autowired
    MoodIntensityRepo moodIntensityRepo;
    @Autowired
    UserMoodCheckInRepo userMoodCheckInRepo;
    @Autowired
    MoodSourceRepo moodSourceRepo;
    @Autowired
    AppUserRepo appUserRepo;
    @Autowired
    UserMoodSourceCheckInRepo userMoodSourceCheckInRepo;


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

    @Override
    public MtMoodIntensity getMoodIntensitiesById(Long id) {
        MtMoodIntensity mtMoodIntensity;
        if (Validator.isValid(id)) {
            Optional<MtMoodIntensity> optionalMoodIntensity = moodIntensityRepo.findById(id);
            if (optionalMoodIntensity.isPresent()) {
                mtMoodIntensity = optionalMoodIntensity.get();
                return mtMoodIntensity;
            }
        }
        return new MtMoodIntensity();
    }


    @Override
    public String deleteMoodIntensitiesById(Long id) {
        MtMoodIntensity mtMoodIntensity;
        if (Validator.isValid(id)) {
            Optional<MtMoodIntensity> optionalMoodIntensity = moodIntensityRepo.findById(id);
            if (optionalMoodIntensity.isPresent()) {
                mtMoodIntensity = optionalMoodIntensity.get();
                mtMoodIntensity.setIsDeleted(Boolean.FALSE);
                mtMoodIntensity.setIsDeleted(Boolean.TRUE);
                moodIntensityRepo.save(mtMoodIntensity);
                return "Mood Intensity Deleted Successfully";
            }
        }
        return "this id not present in database";
    }

    @Override
    public List<MtMoodIntensity> getAllMoodIntensities() {
        List<MtMoodIntensity> mtMoodIntensityList = moodIntensityRepo.findAll();
        if (!mtMoodIntensityList.isEmpty()) {
            return mtMoodIntensityList.stream().filter(o -> o.getIsActive().equals(Boolean.TRUE)).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    @Override
    public String updateMoodIntensity(MoodSourceIntensityRequestDto request) {
        UserMoodCheckIn userMoodCheckIn = null;
        if (Validator.isValid(request.getId())) {
            Optional<UserMoodCheckIn> optionalUserMoodCheckIn = userMoodCheckInRepo.findById(request.getId());
            if (optionalUserMoodCheckIn.isPresent()) {
                userMoodCheckIn = optionalUserMoodCheckIn.get();
                if (Validator.isValid(request.getIntensityId())) {
                    Optional<MtMoodIntensity> optionalMtMoodIntensity = moodIntensityRepo.findByIdAndMtMoodInfoIdAndIsActiveAndIsDeleted(request.getIntensityId(), request.getMoodInfoId(), Boolean.TRUE, Boolean.FALSE);
                    if (optionalMtMoodIntensity.isPresent()) {
                        userMoodCheckIn.setMtMoodIntensity(optionalMtMoodIntensity.get());
                    }
                }
                if (Validator.isValid(request.getAppUserId())) {
                    Optional<AppUser> optionalAppUser = appUserRepo.findById(request.getAppUserId());
                    if (optionalAppUser.isPresent()) {
                        userMoodCheckIn.setAppUser(optionalAppUser.get());
                    }
                }
                if (Validator.isValid(request.getMoodInfoId())) {
                    Optional<MtMoodInfo> optionalMtMoodInfo = moodInfoRepo.findById(request.getMoodInfoId());
                    if (optionalMtMoodInfo.isPresent()) {
                        userMoodCheckIn.setMtMoodInfo(optionalMtMoodInfo.get());
                    }
                }
                if (Validator.isValid(request.getUserSourceCheckedInId())) {
                    Optional<UserMoodSourceCheckedIn> userMoodSourceCheckedIn = userMoodSourceCheckInRepo.findById(request.getUserSourceCheckedInId());
                    if (userMoodSourceCheckedIn.isPresent()) {
                        userMoodCheckIn.setUserMoodSourceCheckedIn(userMoodSourceCheckedIn.get());
                    }
                }
                userMoodCheckIn.setDescription(request.getDescription());
                userMoodCheckInRepo.save(userMoodCheckIn);
            }
            return "User mood updated successfully";
        }
        return "This id not present in database";
    }
    @Override
    public String deleteUserMoodCheckInById(Long id) {
        Optional<MtMoodIntensity>optionalMtMoodIntensity=moodIntensityRepo.findById(id);
        optionalMtMoodIntensity.ifPresent(moodIntensityRepo::delete);
        return "User mood check in details deleted successfully";
    }

    @Override
    public List<UserMoodCheckIn> getAllMoodCheckIn() {
        List<UserMoodCheckIn> userMoodCheckInList=userMoodCheckInRepo.findAll();
        if(!userMoodCheckInList.isEmpty()){
            return userMoodCheckInList.stream().filter(userMoodCheckIn ->userMoodCheckIn.getIsActive().equals(Boolean.TRUE)).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    @Override
    public String createUserMoodCheckIn(MoodSourceIntensityRequestDto request) {
        UserMoodCheckIn userMoodCheckIn = new UserMoodCheckIn();
        if (Validator.isValid(request.getIntensityId())) {
            Optional<MtMoodIntensity> optionalMtMoodIntensity = moodIntensityRepo.findByIdAndMtMoodInfoIdAndIsActiveAndIsDeleted(request.getIntensityId(), request.getMoodInfoId(), Boolean.TRUE, Boolean.FALSE);
            if (optionalMtMoodIntensity.isPresent()) {
                userMoodCheckIn.setMtMoodIntensity(optionalMtMoodIntensity.get());
            }
        }
        if (Validator.isValid(request.getAppUserId())) {
            Optional<AppUser> optionalAppUser = appUserRepo.findById(request.getAppUserId());
            if (optionalAppUser.isPresent()) {
                userMoodCheckIn.setAppUser(optionalAppUser.get());
            }
        }
        if (Validator.isValid(request.getMoodInfoId())) {
            Optional<MtMoodInfo> optionalMtMoodInfo = moodInfoRepo.findById(request.getMoodInfoId());
            if (optionalMtMoodInfo.isPresent()) {
                userMoodCheckIn.setMtMoodInfo(optionalMtMoodInfo.get());
            }
        }
        if (Validator.isValid(request.getUserSourceCheckedInId())) {
            Optional<UserMoodSourceCheckedIn> userMoodSourceCheckedIn = userMoodSourceCheckInRepo.findById(request.getUserSourceCheckedInId());
            if (userMoodSourceCheckedIn.isPresent()) {
                userMoodCheckIn.setUserMoodSourceCheckedIn(userMoodSourceCheckedIn.get());
            }
        }
        userMoodCheckIn.setDescription(request.getDescription());
        userMoodCheckInRepo.save(userMoodCheckIn);
        return "create mood checking details";
    }
}

