package net.thrymr.services.impl;

import graphql.kickstart.tools.GraphQLQueryResolver;
import net.thrymr.FileDocument;
import net.thrymr.dto.*;
import net.thrymr.dto.response.PaginationResponse;
import net.thrymr.dto.response.UserAppointmentResponse;
import net.thrymr.enums.Roles;
import net.thrymr.enums.TagType;
import net.thrymr.model.*;
import net.thrymr.model.master.*;
import net.thrymr.repository.CategoryRepo;
import net.thrymr.repository.CourseRepo;
import net.thrymr.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

@Component
public class Query implements GraphQLQueryResolver {


    @Autowired
    MoodSourceService moodSourceService;
    @Autowired
    MoodInfoService moodInfoService;

    @Autowired
    MoodIntensityService moodIntensityService;

    @Autowired
    AppUserService appUserService;

    @Autowired
    RoleService roleService;

    @Autowired
    CityCountyAndRegionService cityCountyAndRegionService;

    @Autowired
    SiteTeamAndShiftTimingsService siteTeamAndShiftTimingsService;
    @Autowired
    CategoryRepo categoryRepo;

    @Autowired
    CounsellorSlotService counsellorSlotService;

    @Autowired
    CourseRepo courseRepo;
    @Autowired
    UnitAndChapterServices unitAndChapterServices;
    @Autowired
    CounsellorService counsellorService;
    @Autowired
    VendorService vendorService;
    @Autowired
    AssessmentService assessmentService;
    @Autowired
    QuestionAndOptionsService questionAndOptionsService;
    @Autowired
    AppointmentService appointmentService;

    @Autowired
    CourseService courseService;

    @Autowired
    FileService fileService;

    @Autowired
    MiniSessionService miniSessionService;

    @Autowired
    WorksheetService worksheetService;

    @Autowired
    PsychoEducationService psychoEducationService;

    @Autowired
    MeditationService meditationService;


    @QueryMapping(name = "getMoodSourceById")
    public MtMoodSource getMoodSourceById(@Argument Long id) {
        return moodSourceService.getMoodSourceById(id);
    }

    @QueryMapping(name = "getAllMoodSources")
    public List<MtMoodSource> getAllMoodSources() {
        return moodSourceService.getAllMoodSources();
    }

    @QueryMapping(name = "getMoodInfoById")
    public MtMoodInfo getMoodInfoById(@Argument Long id) {
        return moodInfoService.getMoodInfoById(id);
    }

    @QueryMapping(name = "getAllMoodInfo")
    public List<MtMoodInfo> getAllMoodInfo() {
        return moodInfoService.getAllMoodInfo();
    }

    @QueryMapping(name = "getAllMoodCheckIn")
    public List<UserMoodCheckIn> getAllMoodCheckIn() {
        return moodIntensityService.getAllMoodCheckIn();
    }

    @QueryMapping("getAllMoodIntensities")
    public List<MtMoodIntensity> getAllMoodIntensities() {
        return moodIntensityService.getAllMoodIntensities();
    }

    //get user by id
    @QueryMapping
    public AppUser getAppUserById(@Argument Long id) {
        return appUserService.getAppUserById(id);

    }

    // get all Users
    @QueryMapping("getAllAppUsers")
    public List<AppUser> getAllAppUsers() throws ParseException {
        return appUserService.getAllAppUsers();

    }


    @QueryMapping("getAllMtRoles")
    public List<MtRoles> getAllMtRoles() {
        return roleService.getAllMtRoles();

    }

    @QueryMapping("mtRoleById")
    public MtRoles mtRoleById(Long id) {
        return roleService.mtRoleById(id);
    }

    @QueryMapping("getAllCategory")
    public List<Category> getAllCategory() {
        return categoryRepo.findAll();
    }

    @QueryMapping("getAllCourse")
    public List<Course> getAllCourse() {
        return courseRepo.findAll();
    }

    @QueryMapping("getAllCountry")
    public List<MtCountry> getAllCountry() {
        List<MtCountry> countryList = cityCountyAndRegionService.getAllCountry();
        return countryList;
    }

    @QueryMapping("getAllCities")
    public List<MtCity> getAllCities() {
        List<MtCity> cityList = cityCountyAndRegionService.getAllCities();
        return cityList;
    }

    @QueryMapping("getAllRegions")
    public List<MtRegion> getAllRegions() {
        List<MtRegion> regionList = cityCountyAndRegionService.getAllRegions();
        return regionList;
    }

    @QueryMapping("getAllUnit")
    public List<Unit> getAllUnit() {
        List<Unit> unitList = unitAndChapterServices.getAllUnit();
        return unitList;
    }

    @QueryMapping("getAllChapters")
    public List<Chapter> getAllChapters() {
        List<Chapter> mtChapterList = unitAndChapterServices.getAllChapters();
        return mtChapterList;
    }

    @QueryMapping("getLearnPath")
    public PaginationResponse getLearnPath(UnitDto unitDto) {
        return unitAndChapterServices.getLearnPath(unitDto);
    }

    @QueryMapping(name = "getAllTeamPagination")
    public PaginationResponse getAllTeamPagination(TeamDto teamdto) {
        return siteTeamAndShiftTimingsService.getAllTeamPagination(teamdto);
    }

    @QueryMapping(name = "getAllSitePagination")
    public PaginationResponse getAllSitePagination(SiteDto siteDto) {
        return siteTeamAndShiftTimingsService.getAllSitePagination(siteDto);
    }

    /*@QueryMapping(name="getCounsellorSlot")
    public List<CounsellorSlot> getCounsellorSlot(String empId) {
        return counsellorSlotService.getCounsellorSlot(empId);
    }*/
    @QueryMapping("getAllVendor")
    public List<Vendor> getAllVendor() {
        return vendorService.getAllVendor();
    }

    @QueryMapping
    public Vendor getVendorById(@Argument Long id) {
        return vendorService.getVendorById(id);
    }

    @QueryMapping(name = "getAllVendorPagination")
    public PaginationResponse getAllVendorPagination(@Argument(name = "input") VendorDto request) {
        return vendorService.getAllVendorPagination(request);
    }

    @QueryMapping(name = "getCounsellorSlot")
    public List<CounsellorSlot> getCounsellorSlot() {
        return counsellorSlotService.getCounsellorSlot();
    }

    @QueryMapping(name = "getAllShiftTimings")
    public List<ShiftTimings> getAllShiftTimings() {
        return siteTeamAndShiftTimingsService.getAllShiftTimings();
    }

    @QueryMapping(name = "getSiteById")
    public Site getSiteById(@Argument Long id){
        return siteTeamAndShiftTimingsService.getSiteById(id);
    }

    @QueryMapping(name = "getCounsellorSlotById")
    public CounsellorSlot getCounsellorSlotById(@Argument Long id) {
        return counsellorSlotService.getCounsellorSlotById(id);
    }

    @QueryMapping(name = "getAllCounsellor")
    public Page<Counsellor> getAllCounsellor(@Argument(name = "input") CounsellorDto response) {
        return counsellorService.getAllCounsellor(response);
    }

    @QueryMapping("getAllAssessment")
    public List<MtAssessment> getAllAssessment() {
        return assessmentService.getAllAssessment();
    }


    @QueryMapping("getAssessmentById")
    public MtAssessment getAssessmentById(@Argument Long id) {
        return assessmentService.getAssessmentById(id);
    }


    @QueryMapping("getQuestionById")
    public MtQuestion getQuestionById(@Argument Long id) {
        return questionAndOptionsService.getQuestionById(id);
    }

    @QueryMapping("getAnswersByQuestionId")
    public List<MtOptions> getAnswersByQuestionId(@Argument Long id) {
        return questionAndOptionsService.getAnswersByQuestionId(id);
    }

    @QueryMapping("getAllQuestions")
    public List<MtQuestion> getAllQuestions() {
        return questionAndOptionsService.getAllQuestions();
    }

    @QueryMapping("getOptionById")
    public MtOptions getOptionById(@Argument Long id) {
        return questionAndOptionsService.getOptionById(id);
    }

    @QueryMapping("getAllOption")
    public List<MtOptions> getAllOption() {
        return questionAndOptionsService.getAllOption();
    }

    @QueryMapping("getAppointmentById")
    public UserAppointment getAppointmentById(@Argument Long id) {
        return appointmentService.getAppointmentById(id);
    }

    @QueryMapping(name = "getUserAppointmentCountById")
    public UserAppointmentResponse getUserAppointmentCountById(@Argument Long id) {
        return appUserService.getUserAppointmentCountById(id);
    }


    @QueryMapping("getAllAppointment")
    public List<UserAppointment> getAllAppointment() {
        return appointmentService.getAllAppointment();
    }

    @QueryMapping(name = "getCounsellorById")
    public Counsellor getCounsellorById(@Argument Long id) {
        return counsellorService.getCounsellorById(id);
    }


    @QueryMapping(name = "getAllEnumRoles")
    public List<Roles> getAllEnumRoles() {
        return appUserService.getAllEnumRoles();
    }

    @QueryMapping(name = "getAllAppUserByRoles")
    public List<AppUser> getAllAppUserByRoles(@Argument(name = "input") AppUserDto request) {
        return siteTeamAndShiftTimingsService.getAllAppUserByRoles(request);
    }

    @QueryMapping(name = "getAllAppUserPagination")
    public PaginationResponse getAllAppUserPagination(@Argument(name = "input") AppUserDto request) {
        return appUserService.getAllAppUserPagination(request);
    }

    @QueryMapping(name = "getMiniSessionById")
    public MiniSession getMiniSessionById(@Argument Long id) {
        return miniSessionService.getMiniSessionById(id);
    }

    @QueryMapping(name = "getAllMiniSession")
    public List<MiniSession> getAllMiniSession() {
        return miniSessionService.getAllMiniSession();
    }

    @QueryMapping(name = "getAllEnumTags")
    public List<TagType> getAllEnumTags() {
        return miniSessionService.getAllEnumTags();
    }

    @QueryMapping(name = "getAllGroups")
    public List<Groups> getAllGroups() {
        return miniSessionService.getAllGroups();
    }

    @QueryMapping(name = "getAllMiniSessionPagination")
    public PaginationResponse getAllMiniSessionPagination(@Argument(name = "input") MiniSessionDto request) {
        return miniSessionService.getAllMiniSessionPagination(request);
    }

    @QueryMapping(name = "downloads")
    public FileDocument downloads(@Argument String id) throws IOException {
        FileDocument loadFile = fileService.downloadFile(id);
        return loadFile;
    }

    @QueryMapping(name = "getAllGroupDetails")
    public List<GroupDetails> getAllGroupDetails() {
        return miniSessionService.getAllGroupDetails();
    }

    @QueryMapping(name = "getGroupById")
    public Groups getGroupById(@Argument Long id) {
        return miniSessionService.getGroupById(id);
    }

    @QueryMapping("getAllWorksheet")
    public List<MtWorksheet> getAllWorksheet() {
        return worksheetService.getAllWorksheet();
    }

    @QueryMapping("getAllWorkSheetPagination")
    public PaginationResponse getAllWorkSheetPagination(@Argument(name = "input") WorksheetDto request) {
        return worksheetService.getAllWorkSheetPagination(request);
    }

    @QueryMapping("getWorksheetById")
    public MtWorksheet getWorksheetById(@Argument Long id) {
        return worksheetService.getWorksheetById(id);
    }

    @QueryMapping("getAllPsychoEducation")
    public List<MtPsychoEducation> getAllPsychoEducation() {
        return psychoEducationService.getAllPsychoEducation();
    }

    @QueryMapping("getPsychoEducationById")
    public MtPsychoEducation getPsychoEducationById(@Argument Long id) {
        return psychoEducationService.getPsychoEducationById(id);
    }

    @QueryMapping(name = "getPaginationPsychoEducation")
    public PaginationResponse getPsychoEducationPagination(@Argument(name = "input") PsychoEducationDto request) {
        return psychoEducationService.getPsychoEducationPagination(request);
    }

    @QueryMapping("getAllMeditation")
    public List<MtMeditation> getAllMeditation() {
        return meditationService.getAllMeditation();
    }
    @QueryMapping("getAllMeditationPagination")
    public PaginationResponse getAllMeditationPagination(@Argument (name = "input") MeditationDto response) {
        return meditationService.getAllMeditationPagination(response);
    }

    @QueryMapping("getMeditationById")
    public MtMeditation getMeditationById(@Argument Long id) {
        return meditationService.getMeditationById(id);
    }

    @QueryMapping("getAllChapterPagination")
    public PaginationResponse getAllChapterPagination(@Argument(name = "input") ChapterDto response){
        return unitAndChapterServices.getAllChapterPagination(response);
    }

    @QueryMapping(name="previewAlertNotification")
    public RoleWiseCountResponse previewAlertNotification(@Argument(name = "input") AppUserDto request)  {
        return siteTeamAndShiftTimingsService.previewAlertNotification(request);
    }

    @QueryMapping("getTeamById")
    public Team getTeamById(@Argument Long id) {
        return siteTeamAndShiftTimingsService.getTeamById(id);
    }
}
