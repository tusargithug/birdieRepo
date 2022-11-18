package net.thrymr.services.impl;

import net.thrymr.dto.MoodSourceDto;
import net.thrymr.dto.request.MoodSourceIntensityRequestDto;
import net.thrymr.enums.Category;
import net.thrymr.enums.MoodType;
import net.thrymr.model.AppUser;
import net.thrymr.model.UserMoodSourceCheckedIn;
import net.thrymr.model.master.MtMoodSource;
import net.thrymr.repository.MoodSourceRepo;
import net.thrymr.repository.UserMoodSourceCheckInRepo;
import net.thrymr.services.MoodSourceService;
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
public class MoodSourceServiceImpl implements MoodSourceService {
    Logger logger = LoggerFactory.getLogger(MoodSourceServiceImpl.class);

    @Autowired
    Environment environment;
    @Autowired
    MoodSourceRepo moodSourceRepo;
    @Autowired
    UserMoodSourceCheckInRepo userMoodSourceCheckInRepo;

    @Override
    public ApiResponse addMoodSourceByExcel(MultipartFile file) {
        List<MtMoodSource> mtMoodSourceList = new ArrayList<>();
        XSSFWorkbook workbook;
        try {
            workbook = new XSSFWorkbook(file.getInputStream());
        } catch (IOException e) {
            return new ApiResponse(HttpStatus.BAD_REQUEST, environment.getProperty("MOOD_SOURCE_IMPORT_FORMAT_FAILED"));
        }
        XSSFSheet worksheet = workbook.getSheetAt(0);
        if (worksheet.getLastRowNum() < 1) {
            return new ApiResponse(HttpStatus.BAD_REQUEST, environment.getProperty("MOOD_SOURCE_IMPORT_FORMAT_INVALID_DATA"));
        }
        for (int index = 1; index <= worksheet.getLastRowNum(); index++) {
            if (index > 0) {
                try {
                    XSSFRow row = worksheet.getRow(index);

                    MtMoodSource mtMoodSource = new MtMoodSource();

                    if (row.getCell(1) != null) {
                        mtMoodSource.setName(getCellValue(row.getCell(1)));
                    }

                    if (row.getCell(2) != null) {
                        mtMoodSource.setCategory(Category.stringToEnum(getCellValue(row.getCell(2))));
                    }

                    if (row.getCell(3) != null) {
                        mtMoodSource.setSequence(Integer.parseInt(getCellValue(row.getCell(3))));
                    }
                    mtMoodSourceList.add(mtMoodSource);
                    mtMoodSourceList = moodSourceRepo.saveAll(mtMoodSourceList);
                } catch (Exception e) {
                    logger.error("Exception{} " , e);
                    return new ApiResponse(HttpStatus.BAD_REQUEST, environment.getProperty("MOOD_SOURCE_IMPORT_FORMAT_FAILED"));
                }
            }
        }
        return new ApiResponse(HttpStatus.OK, environment.getProperty("MOOD_SOURCE_FOUND"));
    }

    @Override
    public List<MtMoodSource> getAllMoodSources() {
        List<MtMoodSource> mtMoodSourceList = moodSourceRepo.findAll();
        if (!mtMoodSourceList.isEmpty()) {
            return mtMoodSourceList.stream().filter(mtMoodSource -> mtMoodSource.getIsActive().equals(Boolean.TRUE)).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    @Override
    public MtMoodSource getMoodSourceById(Long id) {

        MtMoodSource mtMoodSource = null;
        if (Validator.isValid(id)) {
            Optional<MtMoodSource> optionalMtMoodSource = moodSourceRepo.findById(id);
            if (optionalMtMoodSource.isPresent() && optionalMtMoodSource.get().getIsActive().equals(Boolean.TRUE)) {
                mtMoodSource = optionalMtMoodSource.get();
                return mtMoodSource;
            }

        }
        return new MtMoodSource();
    }

    @Override
    public ApiResponse moodSourceSave(MoodSourceDto request) {


        return null;
    }

    @Override
    public String updateMoodSourceById(MoodSourceDto request) {

        MtMoodSource mtMoodSource = null;

        if (Validator.isValid(request.getId())) {

            Optional<MtMoodSource> optionalMtMoodSource = moodSourceRepo.findById(request.getId());

            if (optionalMtMoodSource.isPresent()) {
                mtMoodSource = optionalMtMoodSource.get();
                if (Validator.isValid(request.getName())) {
                    mtMoodSource.setName(request.getName());
                }
                if (Validator.isValid(request.getDescription())) {
                    mtMoodSource.setDescription(request.getDescription());
                }
                if (Validator.isValid(request.getSequence())) {
                    mtMoodSource.setSequence(request.getSequence());
                }
                if (Validator.isValid(request.getEmoji())) {
                    mtMoodSource.setEmoji(request.getEmoji());
                }
                if (Validator.isValid(request.getCategory())) {
                    mtMoodSource.setCategory(Category.valueOf(request.getCategory()));
                }
                moodSourceRepo.save(mtMoodSource);
                return "Mood source updated successfully";
            }
        }
        return "This mood source id not present in database";
    }

    @Override
    public String deleteMoodSourceById(Long id) {

        MtMoodSource mtMoodSource = null;
        if (Validator.isValid(id)) {
            Optional<MtMoodSource> optionalMtMoodSource = moodSourceRepo.findById(id);
            if (optionalMtMoodSource.isPresent()) {
                mtMoodSource = optionalMtMoodSource.get();
                mtMoodSource.setIsActive(Boolean.FALSE);
                mtMoodSource.setIsDeleted(Boolean.TRUE);
                moodSourceRepo.save(mtMoodSource);
                return "Mood source deleted successfully";
            }
        }

        return "This mood source id not present in database";
    }

/*    @Override
    public ApiResponse updateMoodSource(MoodSourceIntensityRequestDto request) {


//       // List<MtMoodSource> mtMoodSourceList =moodSourceRepo.findAllByIdIn(request.getSourceIds());
//        UserMoodSourceCheckedIn checkedIn=new UserMoodSourceCheckedIn();
//          if(!mtMoodSourceList.isEmpty()){
//              checkedIn.setSources(mtMoodSourceList);
//         }
//          if(Validator.isValid(request.getDescription())){
//              checkedIn.setDescription(request.getDescription());
//          }
//        userMoodSourceCheckInRepo.save(checkedIn);
        return new ApiResponse(HttpStatus.OK, environment.getProperty("MOOD_SOURCE_UPDATED"));
    }*/

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

    private MoodSourceDto setModelToDto(MtMoodSource mtMoodSource) {
        MoodSourceDto moodSourceDto = new MoodSourceDto();
        moodSourceDto.setName(mtMoodSource.getName());
        moodSourceDto.setCategory(mtMoodSource.getCategory().name());
        moodSourceDto.setDescription(mtMoodSource.getDescription());
        moodSourceDto.setSequence(mtMoodSource.getSequence());
        return moodSourceDto;
    }

    @Override
    public String createUserMoodSourceCheckIn(MoodSourceIntensityRequestDto request) {
        List<MtMoodSource> mtMoodSourceList = moodSourceRepo.findAllByIdIn(request.getMoodSourceIdList());
        UserMoodSourceCheckedIn checkedIn = new UserMoodSourceCheckedIn();
        // checkedIn.setAppUser(user);
        if (!mtMoodSourceList.isEmpty()) {
            checkedIn.setMtMoodSourceList(mtMoodSourceList);
        }
        if (Validator.isValid(request.getDescription())) {
            checkedIn.setDescription(request.getDescription());
        }
        userMoodSourceCheckInRepo.save(checkedIn);
        return "Mood source update successfully";
    }

    @Override
    public String deleteUserMoodSourceCheckInById(Long id) {
        Optional<MtMoodSource> optionalMtMoodSource = moodSourceRepo.findById(id);
        optionalMtMoodSource.ifPresent(moodSourceRepo::delete);
        return "Source deleted successfully";
    }
}
