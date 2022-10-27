package net.thrymr.services.impl;
        import graphql.kickstart.tools.GraphQLMutationResolver;
        import net.thrymr.dto.*;
        import net.thrymr.dto.request.MoodSourceIntensityRequestDto;
        import net.thrymr.dto.slotRequest.TimeSlotDto;
        import net.thrymr.model.master.Category;
        import net.thrymr.model.master.Course;
        import net.thrymr.services.*;
        import org.springframework.graphql.data.method.annotation.Argument;
        import org.springframework.graphql.data.method.annotation.MutationMapping;
        import org.springframework.stereotype.Component;
        import org.springframework.web.multipart.MultipartFile;

        import java.text.ParseException;

@Component
public class MutationResolver implements GraphQLMutationResolver {
    private final MoodSourceService moodSourceService;

    private final RoleService roleService;

    private final AppUserService appUserService;

    private final MoodIntensityService moodIntensityService;

    private final CategoryService categoryService;

    private final CourseService courseService;

    private final CityCountyAndRegionService cityCountyAndRegionService;

    private final SiteTeamAndShiftTimingsService siteTeamAndShiftTimingsService;
    private final UnitAndChapterServices unitAndChapterServices;
    private final CounsellorSlotService counsellorSlotService;

    private final CounsellorService counsellorService;

    public MutationResolver(MoodSourceService moodSourceService, RoleService roleService, AppUserService appUserService, MoodIntensityService moodIntensityService, CategoryService categoryService, CourseService courseService, CounsellorSlotService counsellorSlotService, UnitAndChapterServices unitAndChapterServices, CityCountyAndRegionService cityCountyAndRegionService, SiteTeamAndShiftTimingsService siteTeamAndShiftTimingsService, UnitAndChapterServices unitAndChapterServices1, CounsellorSlotService counsellorSlotService1, CounsellorService counsellorService) {
        this.moodSourceService = moodSourceService;
        this.roleService = roleService;
        this.appUserService = appUserService;
        this.moodIntensityService = moodIntensityService;
        this.categoryService = categoryService;
        this.courseService = courseService;
        this.cityCountyAndRegionService = cityCountyAndRegionService;
        this.siteTeamAndShiftTimingsService = siteTeamAndShiftTimingsService;
        this.unitAndChapterServices = unitAndChapterServices;
        this.counsellorSlotService = counsellorSlotService;
        this.counsellorService = counsellorService;
    }

    @MutationMapping(name = "createAppUser")
    public String createAppUser(AppUserDto request){
        return  appUserService.createAppUser(request);
    }

    @MutationMapping(name = "updateAppUser")
    public String  updateAppUser(AppUserDto request){
        return appUserService.updateAppUser(request);
    }



    //delete App user by id
    @MutationMapping(name="deleteAppUserById")
    public String deleteAppUserById(Long id){
        return appUserService.deleteAppUserById(id);
    }


    @MutationMapping(name="createUserMoodCheckIn")
    public String createUserMoodCheckIn(MoodSourceIntensityRequestDto request){
        return moodIntensityService.createUserMoodCheckIn(request);

    }
    @MutationMapping(name="deleteUserMoodCheckInById")
    public String deleteUserMoodCheckInById( Long id){
        return moodIntensityService.deleteUserMoodCheckInById(id);


    }

    @MutationMapping(name="createUserMoodSourceCheckIn")
    public String createUserMoodSourceCheckIn(MoodSourceIntensityRequestDto request) {
        return   moodSourceService.createUserMoodSourceCheckIn(request);
    }

    @MutationMapping(name="deleteUserMoodSourceCheckInById")
    public String deleteUserMoodSourceCheckInById( Long id){
        return   moodSourceService.deleteUserMoodSourceCheckInById( id);
    }

    @MutationMapping(name="createUserCourse")
    public String createUserCourse( UserCourseDto request) throws ParseException {
        return appUserService.createUserCourse(request);


    }
    @MutationMapping(name="updateCategory")
    public Category updateCategory( CategoryDto request){
        return categoryService.updateCategory(request);

    }

    @MutationMapping(name="deleteCategoryById")
    public String deleteCategoryById(@Argument Long id){
    return categoryService.deleteCategoryById(id);

    }
    @MutationMapping(name="createCategory")
    public String createCategory(CategoryDto request) {
        return categoryService.createCategory(request);

    }



    @MutationMapping(name="createCourse")
    public String createCourse( CourseDto request) {
      return courseService.createCourse(request);

    }

    @MutationMapping(name="updateCourse")
    public Course updateCourse(CourseDto request){
        return courseService. updateCourse(request);

    }

    @MutationMapping(name="deleteCourseById")
    public String deleteCourseById( Long id){

        return categoryService.deleteCourseById(id);

    }

    @MutationMapping(name="createRole")
    public String createRole( RolesDto request){
        return roleService.createRole(request);
    }

    @MutationMapping( "updateRole")
    public String updateRole( RolesDto request){
        return roleService.updateRole(request);
    }

    @MutationMapping(name="deleteRoleById")
    public String deleteRoleById( Long id){
        return roleService.deleteRoleById(id);
    }
    @MutationMapping(name="save-country")
    public String saveCountry(CountryDto countryDto) {
        return cityCountyAndRegionService.saveCountry(countryDto);

    }


    @MutationMapping(name="updateCountryById")
    public String updateCountryById(Long id,CountryDto countryDto) {
        return cityCountyAndRegionService.updateCountryById(id,countryDto);
    }

    @MutationMapping(name="deleteCountryById")
    public String deleteCountryById(@Argument Long id){
        return cityCountyAndRegionService.deleteCountryById(id);

    }

    @MutationMapping(name="saveCity")
    public String saveCity(CityDto cityDto) {
        return cityCountyAndRegionService.saveCity(cityDto);
    }

   /* @MutationMapping(name="upload-excel-city-data")
    public ResponseEntity<String> uploadCityData(MultipartFile file) {
        cityCountyAndRegionService.uploadCityData(file);
        return new ResponseEntity<>(String, String.getStatus());
    }*/

    @MutationMapping(name="updateCityById")
    public String updateCityById(Long id,CityDto cityDto) {
        return cityCountyAndRegionService.updateCityById(id,cityDto);
        
    }

    @MutationMapping(name="deleteCityById")
    public String deleteCityById(@Argument Long id){
        return cityCountyAndRegionService.deleteCityById(id);
    }

    @MutationMapping(name="saveRegion")
    public String saveRegion(RegionDto regionDto) {
        return cityCountyAndRegionService.saveRegion(regionDto);
    }

    /*@MutationMapping(name="upload-excel-region-data")
    public ResponseEntity<String> uploadRegionData(MultipartFile file) {
        cityCountyAndRegionService.uploadRegionData(file);
        return new ResponseEntity<>(String, String.getStatus());
    }*/

    @MutationMapping(name="updateRegionById")
    public String updateRegionById(Long id,RegionDto regionDto) {
        return cityCountyAndRegionService.updateRegionById(id,regionDto);
    }

    @MutationMapping(name="deleteRegionById")
    public String deleteRegionById(@Argument Long id){
        return cityCountyAndRegionService.deleteRegionById(id);
    }
    @MutationMapping(name="createTeam")
    public String createTeam(TeamDto teamDto){
        return siteTeamAndShiftTimingsService.createTeam(teamDto);
    }
    @MutationMapping(name="updateTeam")
    public String updateTeam(Long id,TeamDto teamDto){
        return siteTeamAndShiftTimingsService.updateTeam(id,teamDto);
    }

    @MutationMapping(name="deleteTeamById")
    public String deleteTeamById(@Argument Long id){
        return siteTeamAndShiftTimingsService.deleteTeamById(id);
    }

    @MutationMapping(name="saveSite")
    public String saveSite(SiteDto siteDto){
        return siteTeamAndShiftTimingsService.saveSite(siteDto);
    }
    @MutationMapping(name="updateSite")
    public String updateSite(Long id,SiteDto siteDto){
        return siteTeamAndShiftTimingsService.updateSite(id,siteDto);
    }
    @MutationMapping(name="deleteSiteById")
    public String deleteSiteById(@Argument Long id){
        return siteTeamAndShiftTimingsService.deleteSiteById(id);
    }

    @MutationMapping(name="saveSiftTimings")
    public String saveSiftTimings(ShiftTimingsDto shiftTimingsDto){
        return siteTeamAndShiftTimingsService.saveSiftTimings(shiftTimingsDto);
    }

    @MutationMapping(name="updateSiftTimings")
    public String updateSiftTimings(Long id,ShiftTimingsDto shiftTimingsDto){
        return siteTeamAndShiftTimingsService.updateSiftTimings(id,shiftTimingsDto);
    }
    @MutationMapping(name="deleteSiftTimingsById")
    public String deleteSiftTimingsById(@Argument Long id){
        return siteTeamAndShiftTimingsService.deleteSiftTimingsById(id);
    }
    @MutationMapping(name="saveUnit")
    public String saveUnit( UnitDto request) {
        return unitAndChapterServices.saveUnit(request);
    }

    @MutationMapping(name = "saveChapter")
    public String  saveChapter(@Argument(name = "input") ChapterDto request) {
        return unitAndChapterServices.saveChapter(request);
    }

    @MutationMapping(name="updateUnitById")
    public String updateUnitById(Long id,UnitDto unitDto) {
        return unitAndChapterServices.updateUnitById(id,unitDto);
    }
    @MutationMapping(name="updateChaptersById")
    public String updateChaptersById(Long id,ChapterDto dto) {
        return unitAndChapterServices.updateChaptersById(id,dto);
    }
    @MutationMapping(name="deleteUnitById")
    public String deleteUnitById(@Argument Long id){
        return unitAndChapterServices.deleteUnitById(id);
        
    }
    @MutationMapping(name="deleteChapterById")
    public String deleteChapterById(@Argument Long id){
        return unitAndChapterServices.deleteChapterById(id);
    }

    @MutationMapping(name="upload-excel-region-data")
    public String uploadRegionData(@Argument (name = "file") MultipartFile file) {
        return cityCountyAndRegionService.uploadRegionData(file);
    }

    /*@MutationMapping(name="upload-excel-city-data")
    public String uploadCityData(@Argument MultipartFile file) {
        return cityCountyAndRegionService.uploadCityData(file);

    }

    @MutationMapping(name="upload-excel-country-data")
    public String uploadCountryData(@Argument MultipartFile file) {
        return  cityCountyAndRegionService.uploadCountryData(file);
    }*/

    @MutationMapping(name = "createCounsellorSlot")
    public String createCounsellorSlot(@Argument(name = "input") TimeSlotDto request) throws ParseException {
        return counsellorSlotService.createCounsellorSlot(request);
    }

    @MutationMapping(name ="createCounsellor")
    public String createCounsellor(@Argument(name = "input") CounsellorDto counsellorDto){
        return counsellorService.createCounsellor(counsellorDto);
    }
    @MutationMapping(name ="updateCounsellorById")
    public String updateCounsellorById(@Argument Long id,@Argument(name = "input") CounsellorDto counsellorDto){
        return counsellorService.updateCounsellorById(id,counsellorDto);
    }
    @MutationMapping(name ="deleteCounsellorById")
    public String deleteCounsellorById(@Argument Long id){
        return counsellorService.deleteCounsellorById(id);
    }
}


