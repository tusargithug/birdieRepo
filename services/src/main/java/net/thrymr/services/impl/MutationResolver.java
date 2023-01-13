package net.thrymr.services.impl;

import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import net.thrymr.dto.*;
import net.thrymr.dto.request.MoodSourceIntensityRequestDto;
import net.thrymr.dto.slotRequest.TimeSlotDto;
import net.thrymr.model.Chapter;
import net.thrymr.model.Team;
import net.thrymr.model.master.MtQuestion;
import net.thrymr.services.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Part;
import java.security.Principal;
import java.text.ParseException;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
public class MutationResolver implements GraphQLMutationResolver {

    private static final Logger logger = LoggerFactory.getLogger(MutationResolver.class);

    @Autowired
    MoodSourceService moodSourceService;
    @Autowired
    RoleService roleService;
    @Autowired
    AppUserService appUserService;
    @Autowired
    MoodIntensityService moodIntensityService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    CourseService courseService;
    @Autowired
    CityCountyAndRegionService cityCountyAndRegionService;
    @Autowired
    SiteTeamAndShiftTimingsService siteTeamAndShiftTimingsService;
    @Autowired
    UnitAndChapterServices unitAndChapterServices;
    @Autowired
    CounsellorSlotService counsellorSlotService;
    @Autowired
    VendorService vendorService;
    @Autowired
    CounsellorService counsellorService;
    @Autowired
    QuestionAndOptionsService questionAndOptionsService;
    @Autowired
    AssessmentService assessmentService;
    @Autowired
    AppointmentService appointmentService;
    @Autowired
    MiniSessionService miniSessionService;
    @Autowired
    FileService fileService;
    @Autowired
    MoodInfoService moodInfoService;
    @Autowired
    WorksheetService worksheetService;
    @Autowired
    PsychoEducationService psychoEducationService;
    @Autowired
    MeditationService meditationService;
    @Autowired
    TeamMembersService teamMembersService;

    @Autowired
    UserLearningStatusService userLearningStatusService;

    @MutationMapping(name = "createAppUser")
    public String createAppUser(AppUserDto request) throws Exception {
        return appUserService.createAppUser(request);
    }

    @MutationMapping(name = "updateAppUser")
    public String updateAppUser(AppUserDto request) throws Exception {
        return appUserService.updateAppUser(request);
    }
    @MutationMapping(name = "updateAppUsers")
    public String updateAppUsers(List<AppUserDto> request) throws Exception {
        return appUserService.updateAppUsers(request);
    }


    //delete App user by id
    @MutationMapping(name = "deleteAppUserById")
    public String deleteAppUserById(Long id) {
        return appUserService.deleteAppUserById(id);
    }


    @MutationMapping(name = "createUserMoodCheckIn")
    public String createUserMoodCheckIn(MoodSourceIntensityRequestDto request) {
        return moodIntensityService.createUserMoodCheckIn(request);

    }

    @MutationMapping(name = "deleteUserMoodCheckInById")
    public String deleteUserMoodCheckInById(Long id) {
        return moodIntensityService.deleteUserMoodCheckInById(id);


    }

    @MutationMapping(name = "deleteUserMoodSourceCheckInById")
    public String deleteUserMoodSourceCheckInById(Long id) {
        return moodSourceService.deleteUserMoodSourceCheckInById(id);
    }


    @MutationMapping(name = "createUserCourse")
    public String createUserCourse(UserCourseDto request) throws ParseException {
        return appUserService.createUserCourse(request);


    }

    @MutationMapping(name = "updateCategory")
    public String updateCategory(CategoryDto request) {
        return categoryService.updateCategory(request);

    }

    @MutationMapping(name = "deleteCategoryById")
    public String deleteCategoryById(@Argument Long id) {
        return categoryService.deleteCategoryById(id);

    }

    @MutationMapping(name = "createCategory")
    public String createCategory(CategoryDto request) {
        return categoryService.createCategory(request);

    }


    @MutationMapping(name = "createCourse")
    public String createCourse(CourseDto request) {
        return courseService.createCourse(request);

    }

    @MutationMapping(name = "updateCourse")
    public String updateCourse(CourseDto request) {
        return courseService.updateCourse(request);

    }

    @MutationMapping(name = "deleteCourseById")
    public String deleteCourseById(Long id) {

        return categoryService.deleteCourseById(id);

    }

    @MutationMapping(name = "createRole")
    public String createRole(RolesDto request) {
        return roleService.createRole(request);
    }

    @MutationMapping("updateRole")
    public String updateRole(RolesDto request) {
        return roleService.updateRole(request);
    }

    @MutationMapping(name = "deleteRoleById")
    public String deleteRoleById(Long id) {
        return roleService.deleteRoleById(id);
    }

    @MutationMapping(name = "saveCountry")
    public String saveCountry(CountryDto countryDto) {
        return cityCountyAndRegionService.saveCountry(countryDto);

    }


    @MutationMapping(name = "updateCountryById")
    public String updateCountryById(CountryDto countryDto) {
        return cityCountyAndRegionService.updateCountryById(countryDto);
    }

    @MutationMapping(name = "deleteCountryById")
    public String deleteCountryById(@Argument Long id) {
        return cityCountyAndRegionService.deleteCountryById(id);

    }

    @MutationMapping(name = "saveCity")
    public String saveCity(CityDto cityDto) {
        return cityCountyAndRegionService.saveCity(cityDto);
    }

   /* @MutationMapping(name="upload-excel-city-data")
    public ResponseEntity<String> uploadCityData(MultipartFile file) {
        cityCountyAndRegionService.uploadCityData(file);
        return new ResponseEntity<>(String, String.getStatus());
    }*/

    @MutationMapping(name = "updateCityById")
    public String updateCityById(CityDto cityDto) {
        return cityCountyAndRegionService.updateCityById(cityDto);
    }

    @MutationMapping(name = "deleteCityById")
    public String deleteCityById(@Argument Long id) {
        return cityCountyAndRegionService.deleteCityById(id);
    }

    @MutationMapping(name = "saveRegion")
    public String saveRegion(RegionDto regionDto) {
        return cityCountyAndRegionService.saveRegion(regionDto);
    }

    /*@MutationMapping(name="upload-excel-region-data")
    public ResponseEntity<String> uploadRegionData(MultipartFile file) {
        cityCountyAndRegionService.uploadRegionData(file);
        return new ResponseEntity<>(String, String.getStatus());
    }*/

    @MutationMapping(name = "updateRegionById")
    public String updateRegionById(RegionDto regionDto) {
        return cityCountyAndRegionService.updateRegionById(regionDto);
    }

    @MutationMapping(name = "deleteRegionById")
    public String deleteRegionById(@Argument Long id) {
        return cityCountyAndRegionService.deleteRegionById(id);
    }

    @MutationMapping(name = "createTeam")
    public Team createTeam(TeamDto teamDto) {
        return siteTeamAndShiftTimingsService.createTeam(teamDto);
    }

    @MutationMapping(name = "updateTeam")
    public Team updateTeam(TeamDto teamDto) {
        return siteTeamAndShiftTimingsService.updateTeam(teamDto);
    }
    @MutationMapping(name = "updateTeams")
    public String updateTeams(List<TeamDto> teamDtoList) {
        return siteTeamAndShiftTimingsService.updateTeams(teamDtoList);
    }

    @MutationMapping(name = "deleteTeamById")
    public String deleteTeamById(@Argument Long id) {
        return siteTeamAndShiftTimingsService.deleteTeamById(id);
    }

    @MutationMapping(name = "saveSite")
    public String saveSite(SiteDto siteDto) {
        return siteTeamAndShiftTimingsService.saveSite(siteDto);
    }

    @MutationMapping(name = "updateSite")
    public String updateSite(SiteDto siteDto) {
        return siteTeamAndShiftTimingsService.updateSite(siteDto);
    }

    @MutationMapping(name = "deleteSiteById")
    public String deleteSiteById(@Argument Long id) {
        return siteTeamAndShiftTimingsService.deleteSiteById(id);
    }

    @MutationMapping(name = "saveSiftTimings")
    public String saveSiftTimings(ShiftTimingsDto shiftTimingsDto) {
        return siteTeamAndShiftTimingsService.saveShiftTimings(shiftTimingsDto);
    }

    @MutationMapping(name = "updateSiftTimings")
    public String updateSiftTimings(ShiftTimingsDto shiftTimingsDto) {
        return siteTeamAndShiftTimingsService.updateSiftTimings(shiftTimingsDto);
    }

    @MutationMapping(name = "deleteSiftTimingsById")
    public String deleteSiftTimingsById(@Argument Long id) {
        return siteTeamAndShiftTimingsService.deleteSiftTimingsById(id);
    }

    @MutationMapping(name = "saveUnit")
    public String saveUnit(UnitDto request) {
        return unitAndChapterServices.saveUnit(request);
    }

    @MutationMapping(name = "saveChapter")
    public Chapter saveChapter(@Argument(name = "input") ChapterDto request) {
        return unitAndChapterServices.saveChapter(request);
    }

    @MutationMapping(name = "updateUnitById")
    public String updateUnitById(UnitDto unitDto) {
        return unitAndChapterServices.updateUnitById(unitDto);
    }

    @MutationMapping(name = "updateChaptersById")
    public Chapter updateChaptersById(ChapterDto dto) {
        return unitAndChapterServices.updateChaptersById(dto);
    }

    @MutationMapping(name = "deleteUnitById")
    public String deleteUnitById(@Argument Long id) {
        return unitAndChapterServices.deleteUnitById(id);

    }

    @MutationMapping(name = "deleteChapterById")
    public String deleteChapterById(@Argument Long id) {
        return unitAndChapterServices.deleteChapterById(id);
    }

    @MutationMapping(name = "uploadRegionData")
    public String uploadRegionData(@Argument(name = "file") MultipartFile file, DataFetchingEnvironment environment) {
        return cityCountyAndRegionService.uploadRegionData(environment.getArgument("file"));
    }

    /*@MutationMapping(name="upload-excel-city-data")
    public String uploadCityData(@Argument MultipartFile file) {
        return cityCountyAndRegionService.uploadCityData(file);

    }*/

    /*@MutationMapping(name="upload-excel-country-data")
    public String uploadCountryData(@Argument MultipartFile file) {
        return  cityCountyAndRegionService.uploadCountryData(file);
    }*/

    @MutationMapping(name = "createCounsellorSlot")
    public String createCounsellorSlot(@Argument(name = "input") CounsellorSlotDto request) throws ParseException {
        return counsellorSlotService.createCounsellorSlot(request);
    }

    @MutationMapping(name = "saveVendor")
    public String saveVendor(@Argument(name = "input") VendorDto request) {
        return vendorService.saveVendor(request);
    }

    @MutationMapping(name = "deleteVendorById")
    public String deleteVendorById(@Argument Long id) {
        return vendorService.deleteVendorById(id);
    }

    @MutationMapping(name = "updateVendor")
    public String updateVendor(@Argument(name = "input") VendorDto request) {
        return vendorService.updateVendor(request);
    }

    @MutationMapping(name = "createCounsellor")
    public String createCounsellor(@Argument(name = "input") CounsellorDto counsellorDto) {
        return counsellorService.createCounsellor(counsellorDto);
    }
    @MutationMapping(name = "updateCounsellors")
    public String updateCounsellors(@Argument(name = "input") List<CounsellorDto> counsellorDtoList) {
        return counsellorService.updateCounsellors(counsellorDtoList);
    }
    @MutationMapping(name = "updateCounsellorById")
    public String updateCounsellorById(@Argument(name = "input") CounsellorDto counsellorDto) {
        return counsellorService.updateCounsellorById(counsellorDto);
    }

    @MutationMapping(name = "deleteCounsellorById")
    public String deleteCounsellorById(@Argument Long id) {
        return counsellorService.deleteCounsellorById(id);
    }

    @MutationMapping(name = "updateCounsellorSlot")
    public String updateCounsellorSlot(@Argument(name = "input") CounsellorSlotDto request) throws ParseException {
        return counsellorSlotService.updateCounsellorSlot(request);
    }

    @MutationMapping(name = "removeCounsellorSlotsById")
    public String removeCounsellorSlotsById(@Argument(name = "input") CounsellorSlotDto request) throws ParseException {
        return counsellorSlotService.removeCounsellorSlotsById(request);
    }

    @MutationMapping("createQuestion")
    public List<MtQuestion> createQuestion(@Argument(name = "input") List<QuestionDto> request) {
        return questionAndOptionsService.createQuestion(request);
    }

    @MutationMapping("updateQuestionById")
    public String updateQuestionById(@Argument(name = "input") List<QuestionDto> request) {
        return questionAndOptionsService.updateQuestionById(request);
    }

    @MutationMapping("deleteQuestionById")
    public String deleteQuestionById(@Argument(name = "input") QuestionDto request) {
        return questionAndOptionsService.deleteQuestionById(request);
    }

    @MutationMapping("createOptions")
    public String createOptions(@Argument(name = "input") OptionsDto request) {
        return questionAndOptionsService.createOptions(request);
    }

    @MutationMapping("updateOptionById")
    public String updateOptionById(@Argument(name = "input") OptionsDto request) {
        return questionAndOptionsService.updateOptionById(request);
    }

    @MutationMapping("deleteOptionById")
    public String deleteOptionById(@Argument Long id) {
        return questionAndOptionsService.deleteOptionById(id);
    }

    @MutationMapping("createAssessment")
    public String createAssessment(@Argument(name = "input") AssessmentDto request) throws ParseException {
        return assessmentService.createAssessment(request);
    }

    @MutationMapping("updateAssessmentId")
    public String updateAssessmentById(@Argument(name = "input") AssessmentDto request) throws ParseException {
        return assessmentService.updateAssessmentById(request);
    }

    @MutationMapping("deleteAssessmentId")
    public String deleteAssessmentId(@Argument Long id) {
        return assessmentService.deleteAssessmentId(id);
    }

    @MutationMapping("createAppointment")
    public String createAppointment(@Argument(name = "input") TimeSlotDto request) {
        return appointmentService.createAppointment(request);
    }

    @MutationMapping("rescheduledUserAppointment")
    public String rescheduledUserAppointment(@Argument(name = "input") TimeSlotDto request) throws ParseException {
        return appointmentService.rescheduledUserAppointment(request);
    }

    @MutationMapping(name = "createGroup")
    public String createGroup(@Argument(name = "input") GroupsDto request) {
        return miniSessionService.createGroup(request);
    }

    @MutationMapping(name = "saveGroupDetails")
    public String saveGroupDetails(@Argument(name = "input") GroupsDto request) {
        return miniSessionService.saveGroupDetails(request);
    }

    @MutationMapping(name = "saveFileDetails")
    public String saveFileDetails(@Argument(name = "input") FileDetailsDto request) {
        return miniSessionService.saveFileDetails(request);
    }

    @MutationMapping(name = "updateGroupById")
    public String updateGroupById(@Argument(name = "input") GroupsDto request) {
        return miniSessionService.updateGroupById(request);
    }

    @MutationMapping(name = "createMiniSession")
    public String createMiniSession(@Argument(name = "input") MiniSessionDto request) {
        return miniSessionService.createMiniSession(request);
    }

    @MutationMapping(name = "updateMiniSession")
    public String updateMiniSession(@Argument(name = "input") MiniSessionDto request) {
        return miniSessionService.updateMiniSession(request);
    }

    @MutationMapping(name = "deleteMiniSessionById")
    public String deleteMiniSessionById(@Argument Long id) {
        return miniSessionService.deleteMiniSessionById(id);
    }


    @MutationMapping("deleteFiles")
    public String deleteFiles(@Argument String id) {
        return fileService.deleteFile(id);
    }

    @MutationMapping(name = "updateMoodInfoById")
    public String updateMoodInfoById(@Argument(name = "input") MoodInfoDto request) {
        return moodInfoService.updateMoodInfoById(request);
    }

    @MutationMapping(name = "deleteMoodInfoById")
    public String deleteMoodInfoById(@Argument Long id) {
        return moodInfoService.deleteMoodInfoById(id);
    }

//    @MutationMapping("uploadFile")
//    public String uploadFile(@Argument("file") MultipartFile file) throws IOException {
//        return fileService.addFile(file);
//    }


    @MutationMapping(name = "updateMoodSourceById")
    public String updateMoodSourceById(@Argument(name = "input") MoodSourceDto request) {
        return moodSourceService.updateMoodSourceById(request);
    }


    @MutationMapping(name = "deleteMoodSourceById")
    public String deleteMoodSourceById(@Argument Long id) {
        return moodSourceService.deleteMoodSourceById(id);
    }

    public String uploadFile(Part file, DataFetchingEnvironment env) {
        Part actualFile = env.getArgument("file");
        return actualFile.getSubmittedFileName();
    }

    @MutationMapping("createWorksheet")
    public String createWorksheet(@Argument(name = "input") WorksheetDto request) {
        return worksheetService.createWorksheet(request);
    }

    @MutationMapping("updateWorksheetById")
    public String updateWorksheetById(@Argument(name = "input") WorksheetDto request) {
        return worksheetService.updateWorksheetById(request);
    }

    @MutationMapping("deleteWorksheetById")
    public String deleteWorksheetById(@Argument Long id) {
        return worksheetService.deleteWorksheetById(id);
    }


    @MutationMapping("createPsychoEducation")
    public String createPsychoEducation(@Argument(name = "input") PsychoEducationDto request) {
        return psychoEducationService.createPsychoEducation(request);
    }

    @MutationMapping("updatePsychoEducationById")
    public String updatePsychoEducationById(@Argument(name = "input") PsychoEducationDto request) {
        return psychoEducationService.updatePsychoEducationById(request);
    }

    @MutationMapping("deletePsychoEducationById")
    public String deletePsychoEducationById(@Argument Long id) {
        return psychoEducationService.deletePsychoEducationById(id);
    }


    @MutationMapping("createMeditation")
    public String createMeditation(@Argument(name = "input") MeditationDto request) {
        return meditationService.createMeditation(request);
    }

    @MutationMapping("updateMeditationById")
    public String updateMeditationById(@Argument(name = "input") MeditationDto request) {
        return meditationService.updateMeditationById(request);
    }

    @MutationMapping("deleteMeditationById")
    public String deleteMeditationById(@Argument Long id) {
        return meditationService.deleteMeditationById(id);
    }

    @MutationMapping(name = "saveCounsellorEmployeeInfo")
    public String saveCounsellorEmployeeInfo(@Argument(name = "input") CounsellorEmployeeDto request) {
        return appUserService.saveCounsellorEmployeeInfo(request);
    }

    @MutationMapping(name = "addEmployeeToTeam")
    public String addEmployeeToTeam(@Argument(name = "input") TeamMembersDto request) {
        return teamMembersService.addEmployeeToTeam(request);
    }

    @MutationMapping(name = "updateTeamMemberById")
    public String updateTeamMemberById(@Argument(name = "input") TeamMembersDto request) {
        return teamMembersService.updateTeamMemberById(request);
    }

    @MutationMapping(name = "addNewLanguage")
    public String addNewLanguage(@Argument(name = "input") LanguageDto request) {
        return counsellorService.addNewLanguage(request);
    }

    @MutationMapping(name = "addNewEducation")
    public String addNewEducation(@Argument(name = "input") EducationDto request) {
        return counsellorService.addNewEducation(request);
    }

    @MutationMapping(name = "updateEducationDetailsById")
    public String updateEducationDetailsById(@Argument(name = "input") EducationDto request) {
        return counsellorService.updateEducationDetailsById(request);
    }

    @MutationMapping(name = "updateLanguageDetailsById")
    public String updateLanguageDetailsById(@Argument(name = "input") LanguageDto request) {
        return counsellorService.updateLanguageDetailsById(request);
    }

    @MutationMapping(name = "deleteTeamMember")
    public String deleteTeamMember(@Argument(name = "input") TeamMembersDto request) {
        return teamMembersService.deleteTeamMember(request);
    }

    @MutationMapping(name = "deleteAllCounsellorSlot")
    public String deleteAllCounsellorSlot() {
        return counsellorSlotService.deleteAllCounsellorSlots();
    }

    @MutationMapping(name = "deleteAllCountry")
    public String deleteAllCountry() {
        return cityCountyAndRegionService.deleteAllCountry();
    }

    @MutationMapping(name = "deleteAllRegion")
    public String deleteAllRegion() {
        return cityCountyAndRegionService.deleteAllRegion();
    }

    @MutationMapping(name = "deleteAllCities")
    public String deleteAllCities() {
        return cityCountyAndRegionService.deleteAllCities();
    }

    @MutationMapping(name="pauseCounsellorSlotsById")
    public String pauseCounsellorSlotsById(@Argument (name = "input") CounsellorSlotDto request) throws ParseException {
        return counsellorSlotService.pauseCounsellorSlotsById(request);
    }
    @MutationMapping(name = "uploadFileTesting")
    public FileUploadResult uploadFileTesting(@Argument MultipartFile file, DataFetchingEnvironment environment) {
        logger.info("Upload file: name={}", file.getOriginalFilename());
        environment.getArgument(file.getContentType());
        return new FileUploadResult(UUID.randomUUID());
    }

    @MutationMapping(name = "testSingleFileUpload")
    public String testSingleFileUpload(Part part, DataFetchingEnvironment env) {
       return "uploaded";
    }




    class FileUploadResult {
        UUID id;

        public FileUploadResult(UUID id) {
            this.id = id;
        }

        public UUID getId() {
            return id;
        }

        public void setId(UUID id) {
            this.id = id;
        }
    }

    @MutationMapping(name = "createUserLearningStatus")
    public String createUserLearningStatus(UserLearningStatusDto userLearningStatusDto) {
        return userLearningStatusService.createUserLearningStatus(userLearningStatusDto);
    }
    @MutationMapping(name = "updateUserLearningStatus")
    public String updateUserLearningStatus(UserLearningStatusDto userLearningStatusDto) {
        return userLearningStatusService.updateUserLearningStatus(userLearningStatusDto);
    }
    @MutationMapping(name = "deleteUserLearningStatusById")
    public String deleteUserLearningStatusById(String userId) {
        return userLearningStatusService.deleteUserLearningStatusById(userId);
    }
    @MutationMapping(name = "deleteAllEducation")
    public String deleteAllEducation(){
        return counsellorService.deleteAllEducation();
    }
}



