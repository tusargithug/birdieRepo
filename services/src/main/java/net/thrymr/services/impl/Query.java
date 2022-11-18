package net.thrymr.services.impl;

import graphql.kickstart.tools.GraphQLQueryResolver;
import net.thrymr.FileDocument;
import net.thrymr.dto.CounsellorDto;
import net.thrymr.dto.SiteDto;
import net.thrymr.dto.TeamDto;
import net.thrymr.dto.UnitDto;
import net.thrymr.dto.VendorDto;
import net.thrymr.dto.*;
import net.thrymr.dto.response.UserAppointmentResponse;
import net.thrymr.enums.Roles;
import net.thrymr.model.*;
import net.thrymr.model.master.*;

import net.thrymr.repository.CategoryRepo;
import net.thrymr.repository.CourseRepo;
import net.thrymr.services.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
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

    /*@QueryMapping("getAllTeam")
    public List<Team> getAllTeam(TeamDto teamdto){
        List<Team> teamList= siteTeamAndShiftTimingsService.getAllTeam(teamdto);
        return teamList;
    }*/
    @QueryMapping("getAllTeam")
    public List<MtTeam> getAllTeam() {
        //List<Team> teamList= siteTeamAndShiftTimingsService.getAllTeam();
        return siteTeamAndShiftTimingsService.getAllTeam();
    }

    @QueryMapping("getAllSite")
    public List<MtSite> getAllSite() {

        return siteTeamAndShiftTimingsService.getAllSite();
    }

    @QueryMapping("getAllUnit")
    public List<MtUnit> getAllUnit() {
        List<MtUnit> unitList = unitAndChapterServices.getAllUnit();
        return unitList;
    }

    @QueryMapping("getAllChapters")
    public List<MtChapter> getAllChapters() {
        List<MtChapter> mtChapterList = unitAndChapterServices.getAllChapters();
        return mtChapterList;
    }

    @QueryMapping("getLearnPath")
    public List<MtUnit> getLearnPath(UnitDto unitDto) {
        List<MtUnit> unitList = unitAndChapterServices.getLearnPath(unitDto);
        return unitList;
    }

    @QueryMapping(name = "getAllTeamPagination")
    public List<MtTeam> getAllTeamPagination(TeamDto teamdto) {
        return siteTeamAndShiftTimingsService.getAllTeamPagination(teamdto);
    }

    @QueryMapping(name = "getAllSitePagination")
    public List<MtSite> getAllSitePagination(SiteDto siteDto) {
        return siteTeamAndShiftTimingsService.getAllSitePagination(siteDto);
    }

    /*@QueryMapping(name="getCounsellorSlot")
    public List<CounsellorSlot> getCounsellorSlot(String empId) {
        return counsellorSlotService.getCounsellorSlot(empId);
    }*/
    @QueryMapping("getAllVendor")
    public List<MtVendor> getAllVendor() {
        return vendorService.getAllVendor();
    }

    @QueryMapping
    public MtVendor getVendorById(@Argument Long id) {
        return vendorService.getVendorById(id);
    }

    @QueryMapping(name = "getAllVendorPagination")
    public List<MtVendor> getAllVendorPagination(@Argument(name = "input") VendorDto request) {
        return vendorService.getAllVendorPagination(request);
    }

    @QueryMapping(name = "getCounsellorSlot")
    public List<CounsellorSlot> getCounsellorSlot() {
        return counsellorSlotService.getCounsellorSlot();
    }

    @QueryMapping(name = "getAllShiftTimings")
    public List<MtShiftTimings> getAllShiftTimings() {
        return siteTeamAndShiftTimingsService.getAllShiftTimings();
    }

    @QueryMapping(name = "getCounsellorSlotById")
    public CounsellorSlot getCounsellorSlotById(@Argument Long id) {
        return counsellorSlotService.getCounsellorSlotById(id);
    }

    @QueryMapping(name = "getAllCounsellor")
    public List<MtCounsellor> getAllCounsellor(@Argument(name = "input") CounsellorDto response) {
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
    public MtCounsellor getCounsellorById(@Argument Long id) {
        return counsellorService.getCounsellorById(id);
    }


    @QueryMapping(name = "getAllEnumRoles")
    public List<Roles> getAllEnumRoles() {
        return appUserService.getAllEnumRoles();
    }

    @QueryMapping(name = "getAllAppUserByAlerts")
    public List<AppUser> getAllAppUserByAlerts(@Argument(name = "input") AppUserDto request) {
        return siteTeamAndShiftTimingsService.getAllAppUserByAlerts(request);
    }

    @QueryMapping(name = "getAllAppUserPagination")
    public List<AppUser> getAllAppUserPagination(@Argument(name = "input") AppUserDto request) {
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

    @QueryMapping(name = "downloads")
    public FileDocument downloads(@Argument String id) throws IOException {
        FileDocument loadFile = fileService.downloadFile(id);
        return loadFile;
    }

    @QueryMapping(name = "getAllGroupDetails")
    public List<GroupDetails> getAllGroupDetails() {
        return miniSessionService.getAllGroupDetails();
    }

    @QueryMapping(name="getGroupById")
    public Groups getGroupById(@Argument Long id) {
        return miniSessionService.getGroupById(id);
    }
}