package net.thrymr.services.impl;

import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import net.thrymr.dto.*;
import net.thrymr.dto.request.MoodSourceIntensityRequestDto;
import net.thrymr.dto.slotRequest.TimeSlotDto;
import net.thrymr.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Part;
import java.io.IOException;
import java.text.ParseException;
import java.util.UUID;

@Slf4j
@Component
public class MutationResolver implements GraphQLMutationResolver {
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

    @MutationMapping(name = "createAppUser")
    public String createAppUser(AppUserDto request) throws Exception {
        return appUserService.createAppUser(request);
    }

    @MutationMapping(name = "updateAppUser")
    public String updateAppUser(AppUserDto request) throws Exception {
        return appUserService.updateAppUser(request);
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

    @MutationMapping(name = "createUserMoodSourceCheckIn")
    public String createUserMoodSourceCheckIn(MoodSourceIntensityRequestDto request) {
        return moodSourceService.createUserMoodSourceCheckIn(request);
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
    public String createTeam(TeamDto teamDto) {
        return siteTeamAndShiftTimingsService.createTeam(teamDto);
    }

    @MutationMapping(name = "updateTeam")
    public String updateTeam(TeamDto teamDto) {
        return siteTeamAndShiftTimingsService.updateTeam(teamDto);
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
        return siteTeamAndShiftTimingsService.saveSiftTimings(shiftTimingsDto);
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
    public String saveChapter(@Argument(name = "input") ChapterDto request) {
        return unitAndChapterServices.saveChapter(request);
    }

    @MutationMapping(name = "updateUnitById")
    public String updateUnitById(UnitDto unitDto) {
        return unitAndChapterServices.updateUnitById(unitDto);
    }

    @MutationMapping(name = "updateChaptersById")
    public String updateChaptersById(ChapterDto dto) {
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
    public String uploadRegionData(@RequestParam MultipartFile file) {
        return cityCountyAndRegionService.uploadRegionData(file);
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
    public String createCounsellorSlot(@Argument(name = "input") TimeSlotDto request) throws ParseException {
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

    @MutationMapping(name = "updateCounsellorById")
    public String updateCounsellorById(@Argument(name = "input") CounsellorDto counsellorDto) {
        return counsellorService.updateCounsellorById(counsellorDto);
    }

    @MutationMapping(name = "deleteCounsellorById")
    public String deleteCounsellorById(@Argument Long id) {
        return counsellorService.deleteCounsellorById(id);
    }

    @MutationMapping(name = "rescheduledCounsellorSlot")
    public String rescheduledCounsellorSlot(@Argument(name = "input") TimeSlotDto request) throws ParseException {
        return counsellorSlotService.rescheduledCounsellorSlot(request);
    }

    @MutationMapping(name = "cancelCounsellorSlot")
    public String cancelCounsellorSlot(@Argument Long id) {
        return counsellorSlotService.cancelCounsellorSlot(id);
    }

    @MutationMapping("createQuestion")
    public String createQuestion(@Argument(name = "input") QuestionDto request) {
        return questionAndOptionsService.createQuestion(request);
    }

    @MutationMapping("updateQuestionById")
    public String updateQuestionById(@Argument(name = "input") QuestionDto request) {
        return questionAndOptionsService.updateQuestionById(request);
    }

    @MutationMapping("deleteQuestionById")
    public String deleteQuestionById(@Argument Long id) {
        return questionAndOptionsService.deleteQuestionById(id);
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
    public String createAssessment(@Argument(name = "input") AssessmentDto request) {
        return assessmentService.createAssessment(request);
    }

    @MutationMapping("updateAssessmentId")
    public String updateAssessmentById(@Argument(name = "input") AssessmentDto request) {
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

}


