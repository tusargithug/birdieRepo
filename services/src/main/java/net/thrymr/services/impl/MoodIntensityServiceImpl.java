package net.thrymr.services.impl;

import kotlin.reflect.jvm.internal.impl.renderer.ClassifierNamePolicy;
import net.thrymr.dto.request.MoodSourceIntensityRequestDto;
import net.thrymr.dto.response.MoodCheckInMonthResponse;
import net.thrymr.dto.response.UserMoodAverageInfo;
import net.thrymr.dto.response.UserMoodAverages;
import net.thrymr.dto.response.UserMoodCheckInResponse;
import net.thrymr.model.AppUser;
import net.thrymr.model.UserMoodCheckIn;
import net.thrymr.model.UserMoodCheckInMoodSources;
import net.thrymr.model.master.MtMoodInfo;
import net.thrymr.model.master.MtMoodIntensity;
import net.thrymr.model.master.MtMoodSource;
import net.thrymr.repository.*;
import net.thrymr.services.MoodIntensityService;
import net.thrymr.utils.ApiResponse;
import net.thrymr.utils.CommonUtil;
import net.thrymr.utils.Validator;
import org.apache.poi.sl.draw.geom.GuideIf;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.representations.AccessToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.trace.http.HttpTrace;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.groupingBy;


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
    AppUserRepo appUserRepo;
    @Autowired
    MoodSourceRepo moodSourceRepo;
    @Autowired
    UserMoodCheckInMoodSourcesRepo userMoodCheckInMoodSourcesRepo;


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
        if (mtMoodIntensity.getIsActive() != null && mtMoodIntensity.getIsActive().equals(Boolean.FALSE)) {
            searchKey = searchKey + " " + "Inactive";
        } else {
            searchKey = searchKey + " " + "Active";
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
                    if (row.getCell(5) != null) {
                        mtMoodIntensity.setDescription(getCellValue(row.getCell(5)));
                    }
                    mtMoodIntensityList.add(mtMoodIntensity);
                    setMoodIntensitySearchKey(mtMoodIntensity);
                    mtMoodIntensityList = moodIntensityRepo.saveAll(mtMoodIntensityList);

                } catch (Exception e) {
                    logger.error("Exception{} ", e);
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
    public List<MtMoodIntensity> getAllMoodIntensitiesByMoodInfoId(Long id) {
        List<MtMoodIntensity> mtMoodIntensityList = moodIntensityRepo.findByMtMoodInfoId(id);
        if (!mtMoodIntensityList.isEmpty()) {
            mtMoodIntensityList = mtMoodIntensityList.stream().filter(o -> o.getIsActive().equals(Boolean.TRUE)).collect(Collectors.toList());
            return mtMoodIntensityList;
        }
        return new ArrayList<>();
    }

    @Override
    public String UpdateUserMoodCheckIn(MoodSourceIntensityRequestDto request) {
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
                userMoodCheckIn.setSearchKey(getUserMoodCheckedSearchKey(userMoodCheckIn));
                userMoodCheckInRepo.save(userMoodCheckIn);
            }
            return "User mood updated successfully";
        }
        return "This id not present in database";
    }

    @Override
    public String deleteUserMoodCheckInById(Long id) {
        Optional<MtMoodIntensity> optionalMtMoodIntensity = moodIntensityRepo.findById(id);
        optionalMtMoodIntensity.ifPresent(moodIntensityRepo::delete);
        return "Intensity deleted successfully";
    }

    @Override
    public UserMoodCheckInResponse getAllUserMoodInfo() {

        List<UserMoodCheckIn> userMoodCheckInList = userMoodCheckInRepo.findAll();

        if (!userMoodCheckInList.isEmpty()) {

            UserMoodCheckInResponse userMoodCheckInResponse = new UserMoodCheckInResponse();

            userMoodCheckInResponse.setTotalEmployees(userMoodCheckInList.size());

            Map<String, List<UserMoodCheckIn>> moodsByMonth = userMoodCheckInList.stream().collect(groupingBy(UserMoodCheckIn::getCreatedDate));

            List<MoodCheckInMonthResponse> moodCheckInMonthResponseList = new ArrayList<>();
            List<UserMoodAverages> userMoodAveragesList = new ArrayList<>();

            moodsByMonth.forEach(new BiConsumer<String, List<UserMoodCheckIn>>() {
                @Override
                public void accept(String date, List<UserMoodCheckIn> userMoodCheckIns) {

                    MoodCheckInMonthResponse moodCheckInMonthResponse = new MoodCheckInMonthResponse();
                    moodCheckInMonthResponse.setMonthName(date);
                    moodCheckInMonthResponse.setPositiveMoodRes(userMoodCheckIns.stream().filter(obj -> obj.getMtMoodInfo().getMoodType().name().equals("POSITIVE")).count());
                    moodCheckInMonthResponse.setNegativeMoodRes(userMoodCheckIns.stream().filter(obj -> obj.getMtMoodInfo().getMoodType().name().equals("NEGATIVE")).count());
                    moodCheckInMonthResponse.setPositiveHighIntensityRes(userMoodCheckIns.stream().filter(obj -> obj.getMtMoodInfo().getMoodType().name().equals("POSITIVE") && obj.getMtMoodIntensity().getScore().equals(5.0f)).count());
                    moodCheckInMonthResponse.setNegativeHighIntensityRes(userMoodCheckIns.stream().filter(obj -> obj.getMtMoodInfo().getMoodType().name().equals("NEGATIVE") && obj.getMtMoodIntensity().getScore().equals(5.0f)).count());
                    moodCheckInMonthResponse.setPositiveLowIntensityRes(userMoodCheckIns.stream().filter(obj -> obj.getMtMoodInfo().getMoodType().name().equals("POSITIVE") && obj.getMtMoodIntensity().getScore().equals(1.0f)).count());
                    moodCheckInMonthResponse.setNegativeLowIntensityRes(userMoodCheckIns.stream().filter(obj -> obj.getMtMoodInfo().getMoodType().name().equals("NEGATIVE") && obj.getMtMoodIntensity().getScore().equals(1.0f)).count());

                    moodCheckInMonthResponseList.add(moodCheckInMonthResponse);
                }
            });

            if (!moodCheckInMonthResponseList.isEmpty()) {
                userMoodCheckInResponse.setMoodCheckInMonthResponseList(moodCheckInMonthResponseList);
            }

            List<UserMoodCheckIn> positiveUserMoodCheckIn = userMoodCheckInList.stream().filter(obj -> obj.getMtMoodInfo().getMoodType().name().equals("POSITIVE")).toList();
            List<UserMoodCheckIn> negativeUserMoodCheckIn = userMoodCheckInList.stream().filter(obj -> obj.getMtMoodInfo().getMoodType().name().equals("NEGATIVE")).toList();

            List<UserMoodCheckIn> positiveHighIntensityMoods = userMoodCheckInList.stream().filter(obj -> obj.getMtMoodInfo().getMoodType().name().equals("POSITIVE") && obj.getMtMoodIntensity().getScore().equals(5.0f)).toList();
            List<UserMoodCheckIn> negativeHighIntensityMoods = userMoodCheckInList.stream().filter(obj -> obj.getMtMoodInfo().getMoodType().name().equals("NEGATIVE") && obj.getMtMoodIntensity().getScore().equals(5.0f)).toList();

            List<UserMoodCheckIn> positiveLowIntensityMoods = userMoodCheckInList.stream().filter(obj -> obj.getMtMoodInfo().getMoodType().name().equals("POSITIVE") && obj.getMtMoodIntensity().getScore().equals(1.0f)).toList();
            List<UserMoodCheckIn> negativeLowIntensityMoods = userMoodCheckInList.stream().filter(obj -> obj.getMtMoodInfo().getMoodType().name().equals("NEGATIVE") && obj.getMtMoodIntensity().getScore().equals(1.0f)).toList();


            List<Long> positivesIds = new ArrayList<>();
            List<Long> negativeIds = new ArrayList<>();
            List<Long> positiveHighIntensityIds = new ArrayList<>();
            List<Long> negativeHighIntensityIds = new ArrayList<>();
            List<Long> positiveLowIntensityIds = new ArrayList<>();
            List<Long> negativeLowIntensityIds = new ArrayList<>();


            Map<Long, Integer> positiveFreqMap = new HashMap<>();
            Map<Long, Integer> negativeFreqMap = new HashMap<>();
            Map<Long, Integer> positiveHighIntensityFreqMap = new HashMap<>();
            Map<Long, Integer> negativeHighIntensityFreqMap = new HashMap<>();
            Map<Long, Integer> positiveLowIntensityFreqMap = new HashMap<>();
            Map<Long, Integer> negativeLowIntensityFreqMap = new HashMap<>();

            for (UserMoodCheckIn userMoodCheckIn : positiveUserMoodCheckIn) {
                positivesIds.add(userMoodCheckIn.getMtMoodInfo().getId());
            }
            for (UserMoodCheckIn userMoodCheckIn : negativeUserMoodCheckIn) {
                negativeIds.add(userMoodCheckIn.getMtMoodInfo().getId());
            }
            for(UserMoodCheckIn userMoodCheckIn : positiveHighIntensityMoods) {
                positiveHighIntensityIds.add(userMoodCheckIn.getMtMoodIntensity().getId());
            }
            for(UserMoodCheckIn userMoodCheckIn : negativeHighIntensityMoods) {
                negativeHighIntensityIds.add(userMoodCheckIn.getMtMoodIntensity().getId());
            }
            for(UserMoodCheckIn userMoodCheckIn : positiveLowIntensityMoods) {
                positiveLowIntensityIds.add(userMoodCheckIn.getMtMoodIntensity().getId());
            }
            for(UserMoodCheckIn userMoodCheckIn : negativeLowIntensityMoods) {
                negativeLowIntensityIds.add(userMoodCheckIn.getMtMoodIntensity().getId());
            }

            Set<Long> possitiveSet = new HashSet<Long>(positivesIds);
            for (Long r : possitiveSet) {
                positiveFreqMap.put(r, Collections.frequency(positivesIds, r));
            }

            Set<Long> negativeSet = new HashSet<Long>(negativeIds);
            for (Long r : negativeSet) {
                negativeFreqMap.put(r, Collections.frequency(negativeIds, r));
            }

            Set<Long> possitiveHighIntensitySet = new HashSet<Long>(positiveHighIntensityIds);
            for (Long r : possitiveHighIntensitySet) {
                positiveHighIntensityFreqMap.put(r, Collections.frequency(positiveHighIntensityIds, r));
            }

            Set<Long> negativeHighIntensitySet = new HashSet<Long>(negativeHighIntensityIds);
            for (Long r : negativeHighIntensitySet) {
                negativeHighIntensityFreqMap.put(r, Collections.frequency(negativeHighIntensityIds, r));
            }

            Set<Long> possitiveLowIntensitySet = new HashSet<Long>(positiveLowIntensityIds);
            for (Long r : possitiveLowIntensitySet) {
                positiveLowIntensityFreqMap.put(r, Collections.frequency(positiveLowIntensityIds, r));
            }

            Set<Long> negativeLowIntensitySet = new HashSet<Long>(negativeLowIntensityIds);
            for (Long r : negativeLowIntensitySet) {
                negativeLowIntensityFreqMap.put(r, Collections.frequency(negativeLowIntensityIds, r));
            }

            List<Long> avgPositiveKeys = positiveFreqMap.entrySet().stream().sorted(Map.Entry.<Long, Integer>comparingByValue().reversed()).limit(3).map(Map.Entry::getKey).toList();
            List<Long> avgNegativeKeys = negativeFreqMap.entrySet().stream().sorted(Map.Entry.<Long, Integer>comparingByValue().reversed()).limit(3).map(Map.Entry::getKey).toList();
            List<Long> avgPositiveHighIntensityKeys = positiveHighIntensityFreqMap.entrySet().stream().sorted(Map.Entry.<Long, Integer>comparingByValue().reversed()).limit(3).map(Map.Entry::getKey).toList();
            List<Long> avgNegativeHighIntensityKeys = negativeHighIntensityFreqMap.entrySet().stream().sorted(Map.Entry.<Long, Integer>comparingByValue().reversed()).limit(3).map(Map.Entry::getKey).toList();
            List<Long> avgPositiveLowIntensityKeys = positiveLowIntensityFreqMap.entrySet().stream().sorted(Map.Entry.<Long, Integer>comparingByValue().reversed()).limit(3).map(Map.Entry::getKey).toList();
            List<Long> avgNegativeLowIntensityKeys = negativeLowIntensityFreqMap.entrySet().stream().sorted(Map.Entry.<Long, Integer>comparingByValue().reversed()).limit(3).map(Map.Entry::getKey).toList();



            UserMoodAverages userPosMoodAverages = new UserMoodAverages();
            userPosMoodAverages.setMoodType("AvgPositiveMoods");
            List<UserMoodAverageInfo> userPosMoodAverageInfoList = new ArrayList<>();
            for (Long positiveKey : avgPositiveKeys) {

                Optional<MtMoodInfo> optionalMtMoodInfo = moodInfoRepo.findById(positiveKey);
                if (optionalMtMoodInfo.isPresent()) {
                    UserMoodAverageInfo userMoodAverageInfo = new UserMoodAverageInfo();
                    userMoodAverageInfo.setMoodName(optionalMtMoodInfo.get().getName());
                    userMoodAverageInfo.setMoodCount(positiveFreqMap.get(positiveKey));
                    userPosMoodAverageInfoList.add(userMoodAverageInfo);
                }
            }
            userPosMoodAverages.setUserMoodAverageInfoList(userPosMoodAverageInfoList);
            userMoodAveragesList.add(userPosMoodAverages);


            UserMoodAverages userNegMoodAverages = new UserMoodAverages();
            userNegMoodAverages.setMoodType("AvgNegativeMoods");
            List<UserMoodAverageInfo> userNegAvgNegMoodInfoList = new ArrayList<>();
            for (Long negativeKey : avgNegativeKeys) {

                Optional<MtMoodInfo> optionalMtMoodInfo = moodInfoRepo.findById(negativeKey);
                if (optionalMtMoodInfo.isPresent()) {
                    UserMoodAverageInfo userMoodAverageInfo = new UserMoodAverageInfo();
                    userMoodAverageInfo.setMoodName(optionalMtMoodInfo.get().getName());
                    userMoodAverageInfo.setMoodCount(negativeFreqMap.get(negativeKey));
                    userNegAvgNegMoodInfoList.add(userMoodAverageInfo);
                }
            }
            userNegMoodAverages.setUserMoodAverageInfoList(userNegAvgNegMoodInfoList);
            userMoodAveragesList.add(userNegMoodAverages);

            UserMoodAverages userHighIntensityPosMoodAverages = new UserMoodAverages();
            userHighIntensityPosMoodAverages.setMoodType("AvgHighIntensityPositiveMoods");
            List<UserMoodAverageInfo> userPossitiveHighIntensityMoodInfoList = new ArrayList<>();
            for (Long posHighInKey : avgPositiveHighIntensityKeys) {
                Optional<MtMoodIntensity> optionalMtMoodIntensity = moodIntensityRepo.findById(posHighInKey);
                if(optionalMtMoodIntensity.isPresent()) {
                    UserMoodAverageInfo userMoodAverageInfo = new UserMoodAverageInfo();
                    userMoodAverageInfo.setMoodName(optionalMtMoodIntensity.get().getName());
                    userMoodAverageInfo.setMoodCount(positiveHighIntensityFreqMap.get(posHighInKey));
                    userPossitiveHighIntensityMoodInfoList.add(userMoodAverageInfo);
                }
            }
            userHighIntensityPosMoodAverages.setUserMoodAverageInfoList(userPossitiveHighIntensityMoodInfoList);
            userMoodAveragesList.add(userHighIntensityPosMoodAverages);

            UserMoodAverages userHighIntensityNegMoodAverages = new UserMoodAverages();
            userHighIntensityNegMoodAverages.setMoodType("AvgHighIntensityNegativeMoods");
            List<UserMoodAverageInfo> userNegativeHighIntensityMoodInfoList = new ArrayList<>();
            for (Long negHighInKey : avgNegativeHighIntensityKeys) {
                Optional<MtMoodIntensity> optionalMtMoodIntensity = moodIntensityRepo.findById(negHighInKey);
                if(optionalMtMoodIntensity.isPresent()) {
                    UserMoodAverageInfo userMoodAverageInfo = new UserMoodAverageInfo();
                    userMoodAverageInfo.setMoodName(optionalMtMoodIntensity.get().getName());
                    userMoodAverageInfo.setMoodCount(negativeHighIntensityFreqMap.get(negHighInKey));
                    userNegativeHighIntensityMoodInfoList.add(userMoodAverageInfo);
                }
            }
            userHighIntensityNegMoodAverages.setUserMoodAverageInfoList(userNegativeHighIntensityMoodInfoList);
            userMoodAveragesList.add(userHighIntensityNegMoodAverages);

            UserMoodAverages userLowIntensityPosMoodAverages = new UserMoodAverages();
            userLowIntensityPosMoodAverages.setMoodType("AvgLowIntensityPositiveMoods");
            List<UserMoodAverageInfo> userPossitiveLowIntensityMoodInfoList = new ArrayList<>();
            for (Long posLowInKey : avgPositiveLowIntensityKeys) {
                Optional<MtMoodIntensity> optionalMtMoodIntensity = moodIntensityRepo.findById(posLowInKey);
                if(optionalMtMoodIntensity.isPresent()) {
                    UserMoodAverageInfo userMoodAverageInfo = new UserMoodAverageInfo();
                    userMoodAverageInfo.setMoodName(optionalMtMoodIntensity.get().getName());
                    userMoodAverageInfo.setMoodCount(positiveLowIntensityFreqMap.get(posLowInKey));
                    userPossitiveLowIntensityMoodInfoList.add(userMoodAverageInfo);
                }
            }
            userLowIntensityPosMoodAverages.setUserMoodAverageInfoList(userPossitiveLowIntensityMoodInfoList);
            userMoodAveragesList.add(userLowIntensityPosMoodAverages);

            UserMoodAverages userLowIntensityNegMoodAverages = new UserMoodAverages();
            userLowIntensityNegMoodAverages.setMoodType("AvgLowIntensityNegativeMoods");
            List<UserMoodAverageInfo> userNegativeLowIntensityMoodInfoList = new ArrayList<>();
            for (Long negLowInKey : avgNegativeLowIntensityKeys) {
                Optional<MtMoodIntensity> optionalMtMoodIntensity = moodIntensityRepo.findById(negLowInKey);
                if(optionalMtMoodIntensity.isPresent()) {
                    UserMoodAverageInfo userMoodAverageInfo = new UserMoodAverageInfo();
                    userMoodAverageInfo.setMoodName(optionalMtMoodIntensity.get().getName());
                    userMoodAverageInfo.setMoodCount(negativeLowIntensityFreqMap.get(negLowInKey));
                    userNegativeLowIntensityMoodInfoList.add(userMoodAverageInfo);
                }
            }
            userLowIntensityNegMoodAverages.setUserMoodAverageInfoList(userNegativeLowIntensityMoodInfoList);
            userMoodAveragesList.add(userLowIntensityNegMoodAverages);

            userMoodCheckInResponse.setUserMoodAveragesList(userMoodAveragesList);

            return userMoodCheckInResponse;

        }

        return new UserMoodCheckInResponse();
    }

    @Override
    public List<UserMoodCheckIn> getAllMoodCheckIn() {
        List<UserMoodCheckIn> userMoodCheckInList = userMoodCheckInRepo.findAll();
        if (!userMoodCheckInList.isEmpty()) {
            return userMoodCheckInList.stream().filter(userMoodCheckIn -> userMoodCheckIn.getIsActive().equals(Boolean.TRUE)).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    @Override
    public String createUserMoodCheckIn(MoodSourceIntensityRequestDto request) {
        UserMoodCheckIn userMoodCheckIn = new UserMoodCheckIn();
        if (Validator.isValid(request.getIntensityId())) {
            Optional<MtMoodIntensity> optionalMtMoodIntensity = moodIntensityRepo.findByIdAndMtMoodInfoId(request.getIntensityId(), request.getMoodInfoId());
            optionalMtMoodIntensity.ifPresent(userMoodCheckIn::setMtMoodIntensity);
        }
        if (Validator.isValid(request.getLoginUserMail())) {
            Optional<AppUser> optionalAppUser = appUserRepo.findByEmail(request.getLoginUserMail());
            optionalAppUser.ifPresent(userMoodCheckIn::setAppUser);
        }

        if (Validator.isValid(request.getMoodInfoId())) {
            Optional<MtMoodInfo> optionalMtMoodInfo = moodInfoRepo.findById(request.getMoodInfoId());
            optionalMtMoodInfo.ifPresent(userMoodCheckIn::setMtMoodInfo);
        }
        userMoodCheckIn.setSearchKey(getUserMoodCheckedSearchKey(userMoodCheckIn));
        LocalDateTime myDateObj = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("MMM-yyyy");
        String formattedDate = myDateObj.format(myFormatObj);
        userMoodCheckIn.setCreatedDate(formattedDate);
        userMoodCheckInRepo.save(userMoodCheckIn);
        if (Validator.isValid(request.getMoodSourceIdList())) {
            List<MtMoodSource> moodSourceList = moodSourceRepo.findAllByIdIn(request.getMoodSourceIdList());
            if (!moodSourceList.isEmpty()) {
                for (MtMoodSource mtMoodSource : moodSourceList) {
                    UserMoodCheckInMoodSources userMoodCheckInMoodSources = new UserMoodCheckInMoodSources();
                    userMoodCheckInMoodSources.setMtMoodSource(mtMoodSource);
                    userMoodCheckInMoodSources.setUserMoodCheckIn(userMoodCheckIn);
                    userMoodCheckInMoodSourcesRepo.save(userMoodCheckInMoodSources);
                }
                return "create mood checking details";
            }
        }
        return "mood source list is empty";
    }


    public String getUserMoodCheckedSearchKey(UserMoodCheckIn userMoodCheckIn) {
        String searchKey = "";
        if (userMoodCheckIn.getIsActive() != null) {
            searchKey = searchKey + " " + userMoodCheckIn.getIsActive();
        }
        if (userMoodCheckIn.getAppUser() != null) {
            searchKey = searchKey + " " + userMoodCheckIn.getAppUser();
        }
        if (userMoodCheckIn.getMtMoodInfo() != null) {
            searchKey = searchKey + " " + userMoodCheckIn.getMtMoodInfo();
        }
        if (userMoodCheckIn.getMtMoodIntensity() != null) {
            searchKey = searchKey + " " + userMoodCheckIn.getMtMoodIntensity();
        }
        if (userMoodCheckIn.getIsActive() != null && userMoodCheckIn.getIsActive().equals(Boolean.FALSE)) {
            searchKey = searchKey + " " + "Inactive";
        } else {
            searchKey = searchKey + " " + "Active";
        }

        return searchKey;
    }

}