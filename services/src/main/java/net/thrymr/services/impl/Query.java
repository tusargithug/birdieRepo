package net.thrymr.services.impl;
        import graphql.kickstart.tools.GraphQLQueryResolver;
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
        import org.springframework.graphql.data.method.annotation.QueryMapping;
        import org.springframework.stereotype.Component;
        import java.text.ParseException;
        import java.util.List;

@Component
public class Query implements GraphQLQueryResolver {


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

    @QueryMapping
    public MtMoodInfo moodInfoById(Long id) {
        return moodInfoService.moodInfoById(id);

    }

    @QueryMapping
    public List<MtMoodInfo> getAllMoodInfo() {
        return moodInfoService.getAllMoodInfo();

    }

    @QueryMapping("getAllMoodIntensity")
    public List<MtMoodIntensity> getAllMoodIntensity() {
        return moodIntensityService.getAllMoodIntensity();
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
    public List<Team> getAllTeam() {
        //List<Team> teamList= siteTeamAndShiftTimingsService.getAllTeam();
        return siteTeamAndShiftTimingsService.getAllTeam();
    }

    @QueryMapping("getAllSite")
    public List<Site> getAllSite() {

        return siteTeamAndShiftTimingsService.getAllSite();
    }

    @QueryMapping("getAllUnit")
    public List<Unit> getAllUnit() {
        List<Unit> unitList = unitAndChapterServices.getAllUnit();
        return unitList;
    }

    @QueryMapping("getAllChapters")
    public List<Chapter> getAllChapters() {
        List<Chapter> chapterList = unitAndChapterServices.getAllChapters();
        return chapterList;
    }

    @QueryMapping("getLearnPath")
    public List<Unit> getLearnPath(UnitDto unitDto) {
        List<Unit> unitList = unitAndChapterServices.getLearnPath(unitDto);
        return unitList;
    }

    @QueryMapping(name = "getAllTeamPagination")
    public List<Team> getAllTeamPagination(TeamDto teamdto) {
        return siteTeamAndShiftTimingsService.getAllTeamPagination(teamdto);
    }

    @QueryMapping(name = "getAllSitePagination")
    public List<Site> getAllSitePagination(SiteDto siteDto) {
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
    public List<Vendor> getAllVendorPagination(@Argument(name = "input") VendorDto request) {
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

    @QueryMapping(name = "getCounsellorSlotById")
    public CounsellorSlot getCounsellorSlotById(@Argument Long id) {
        return counsellorSlotService.getCounsellorSlotById(id);
    }

    @QueryMapping(name = "getAllCounsellor")
    public List<Counsellor> getAllCounsellor(@Argument(name = "input") CounsellorDto response) {
        return counsellorService.getAllCounsellor(response);
    }

    @QueryMapping("getAllAssessment")
    public List<MtAssessment> getAllAssessment() {
        return  assessmentService.getAllAssessment();
    }
    @QueryMapping("getAssessmentById")
    public MtAssessment getAssessmentById(@Argument Long id) {
        return  assessmentService.getAssessmentById(id);
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
    public UserAppointment getAppointmentById(@Argument Long id){
        return appointmentService.getAppointmentById(id);
    }

    @QueryMapping(name="getUserAppointmentCountById")
    public UserAppointmentResponse getUserAppointmentCountById(@Argument Long id){
        return appUserService.getUserAppointmentCountById(id);
    }
    @QueryMapping("getAllAppointment")
    public List<UserAppointment> getAllAppointment(){
        return appointmentService.getAllAppointment();
    }

    @QueryMapping(name = "getCounsellorById")
    public Counsellor getCounsellorById(@Argument Long id){
        return counsellorService.getCounsellorById(id);
    }
    @QueryMapping(name = "getAllEnumRoles")
    public List<Roles> getAllEnumRoles(){
        return appUserService.getAllEnumRoles();
    }

    @QueryMapping(name="getAllAppUserByAlerts")
    public List<AppUser> getAllAppUserByAlerts(@Argument(name = "input") AppUserDto request)  {
        return siteTeamAndShiftTimingsService.getAllAppUserByAlerts(request);
    }
}