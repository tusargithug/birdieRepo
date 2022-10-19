package net.thrymr.services.impl;
        import net.thrymr.dto.SiteDto;
        import net.thrymr.dto.TeamDto;
        import net.thrymr.dto.UnitDto;
        import net.thrymr.model.*;
        import net.thrymr.model.master.*;

        import net.thrymr.repository.CategoryRepo;
        import net.thrymr.repository.CourseRepo;
        import net.thrymr.services.*;

        import net.thrymr.utils.ApiResponse;
        import org.springframework.graphql.data.method.annotation.Argument;
        import org.springframework.graphql.data.method.annotation.QueryMapping;
        import org.springframework.http.ResponseEntity;
        import org.springframework.stereotype.Component;

        import graphql.kickstart.tools.GraphQLQueryResolver;
        import org.springframework.web.bind.annotation.GetMapping;
        import org.springframework.web.bind.annotation.PostMapping;
        import org.springframework.web.bind.annotation.RequestBody;
        import org.springframework.web.bind.annotation.RequestParam;

        import java.util.List;

@Component
public class Query implements GraphQLQueryResolver {


    private final MoodInfoService moodInfoService;

    private final MoodIntensityService moodIntensityService;

    private final AppUserService appUserService;

    private final RoleService roleService;

    private final CityCountyAndRegionService cityCountyAndRegionService;

    private final SiteTeamAndShiftTimingsService siteTeamAndShiftTimingsService;
    private final CategoryRepo categoryRepo;

    private final CounsellorSlotService counsellorSlotService;

    private final CourseRepo courseRepo;
    private final UnitAndChapterServices unitAndChapterServices;

    public Query(AppUserService appUserService, RoleService roleService, MoodInfoService moodInfoService, MoodIntensityService moodIntensityService, CityCountyAndRegionService cityCountyAndRegionService, SiteTeamAndShiftTimingsService siteTeamAndShiftTimingsService, CategoryRepo categoryRepo, CounsellorSlotService counsellorSlotService, CourseRepo courseRepo, UnitAndChapterServices unitAndChapterServices) {

        this.appUserService = appUserService;
        this.roleService = roleService;

        this.moodInfoService = moodInfoService;
        this.moodIntensityService = moodIntensityService;
        this.cityCountyAndRegionService = cityCountyAndRegionService;
        this.siteTeamAndShiftTimingsService = siteTeamAndShiftTimingsService;
        this.categoryRepo = categoryRepo;
        this.counsellorSlotService = counsellorSlotService;
        this.courseRepo = courseRepo;
        this.unitAndChapterServices = unitAndChapterServices;
    }

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
    public List<AppUser> getAllAppUsers() {
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

    //    @QueryMapping("getCounsellorSlot")
//    public List<CounsellorSlot> getCounsellorSlot() {
//        return counsellorSlotService.getCounsellorSlot();
//    }
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
    public List<Team> getAllTeam(){
        //List<Team> teamList= siteTeamAndShiftTimingsService.getAllTeam();
        return siteTeamAndShiftTimingsService.getAllTeam();
    }
//    @QueryMapping("getAllSiteWithPagination")
//    public List<Site>(SiteDto siteDto){
//        ApiResponse apiResponse= siteTeamAndShiftTimingsService.getAllSite(siteDto);
//        return new ApiResponse(apiResponse.getStatus(), apiResponse.getMessage());
//    }
//
     @QueryMapping("getAllSite")
    public List<Site>getAllSite(){

        return siteTeamAndShiftTimingsService.getAllSite();
    }
    @QueryMapping("getAllUnit")
    public List<Unit> getAllUnit(){
        List<Unit> unitList = unitAndChapterServices.getAllUnit();
        return unitList;
    }
    @QueryMapping("getAllChapters")
    public List<Chapter> getAllChapters(){
        List<Chapter> chapterList = unitAndChapterServices.getAllChapters();
        return chapterList;
    }
    @QueryMapping("getLearnPath")
    public List<Unit> getLearnPath(UnitDto unitDto){
        List<Unit> unitList = unitAndChapterServices.getLearnPath(unitDto);
        return unitList;
    }

    @QueryMapping(name="getAllTeamPagination")
    public List<Team> getAllTeamPagination(TeamDto teamdto){
        return siteTeamAndShiftTimingsService.getAllTeamPagination(teamdto);
    }
    @QueryMapping(name="getAllSitePagination")
    public List<Site> getAllSitePagination(SiteDto siteDto){
        return siteTeamAndShiftTimingsService.getAllSitePagination(siteDto);
    }


}