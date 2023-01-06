package net.thrymr.controller;

import graphql.kickstart.servlet.context.DefaultGraphQLServletContext;
import graphql.schema.DataFetchingEnvironment;
import net.thrymr.dto.*;
import net.thrymr.model.Chapter;
import net.thrymr.model.Unit;
import net.thrymr.model.master.MtCity;
import net.thrymr.model.master.MtCountry;
import net.thrymr.model.master.MtRegion;
import net.thrymr.services.CityCountyAndRegionService;
import net.thrymr.utils.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.GraphQlRepository;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class CityCountyAndRegionController {

    @Autowired
    CityCountyAndRegionService cityCountyAndRegionService;
    final static Logger log = LoggerFactory.getLogger(CityCountyAndRegionController.class);

    @MutationMapping(name = "saveCountry")
    public String saveCountry(@Argument(name = "input") CountryDto countryDto) {
        return cityCountyAndRegionService.saveCountry(countryDto);

    }

    @MutationMapping(name = "updateCountryById")
    public String updateCountryById(@Argument(name = "input") CountryDto countryDto) {
        return cityCountyAndRegionService.updateCountryById(countryDto);
    }

    @MutationMapping(name = "deleteCountryById")
    public String deleteCountryById(@Argument Long id) {
        return cityCountyAndRegionService.deleteCountryById(id);

    }

    @MutationMapping(name = "saveCity")
    public String saveCity(@Argument(name = "input") CityDto cityDto) {
        return cityCountyAndRegionService.saveCity(cityDto);
    }

    @MutationMapping(name = "updateCityById")
    public String updateCityById(@Argument(name = "input") CityDto cityDto) {
        return cityCountyAndRegionService.updateCityById(cityDto);

    }

    @MutationMapping(name = "deleteCityById")
    public String deleteCityById(@Argument Long id) {
        return cityCountyAndRegionService.deleteCityById(id);
    }

    @MutationMapping(name = "saveRegion")
    public String saveRegion(@Argument(name = "input") RegionDto regionDto) {
        return cityCountyAndRegionService.saveRegion(regionDto);
    }

    @MutationMapping(name = "updateRegionById")
    public String updateRegionById(@Argument(name = "input") RegionDto regionDto) {
        return cityCountyAndRegionService.updateRegionById(regionDto);
    }

    @MutationMapping(name = "deleteRegionById")
    public String deleteRegionById(@Argument Long id) {
        return cityCountyAndRegionService.deleteRegionById(id);
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

    @QueryMapping("getRegionById")
    public MtRegion getRegionById(@Argument Long id) {
         return cityCountyAndRegionService.getRegionById(id);
    }

    @QueryMapping("getCountryById")
    public MtCountry getCountryById(@Argument Long id) {
        return cityCountyAndRegionService.getCountryById(id);
    }
    @QueryMapping("getCityById")
    public MtCity getCityById(@Argument Long id) {
        return cityCountyAndRegionService.getCityById(id);
    }

    @PostMapping("/upload-excel-region-data")
    public String uploadRegionData(@RequestBody MultipartFile file) {
      return  cityCountyAndRegionService.uploadRegionData(file);
    }

    @PostMapping("/upload-excel-city-data")
    public String uploadCityData(@RequestBody MultipartFile file) {
        return cityCountyAndRegionService.uploadCityData(file);
    }

     @PostMapping("/upload-excel-country-data")
    public String uploadCountryData(@RequestBody MultipartFile file) {
        return  cityCountyAndRegionService.uploadCountryData(file);
    }

    /*@MutationMapping(name="upload-excel-city-data")
    public String uploadCityData(@Argument(name="file") MultipartFile file) {
       return cityCountyAndRegionService.uploadCityData(file);

    }

    @MutationMapping(name="upload-excel-country-data")
    public String uploadCountryData(@Argument(name="file") MultipartFile file) {
       return cityCountyAndRegionService.uploadCountryData(file);
    }*/

    @MutationMapping(name = "uploadRegionData")
    public String uploadRegionData(@Argument(name = "file") MultipartFile file, DataFetchingEnvironment environment) {
        return cityCountyAndRegionService.uploadRegionData(environment.getArgument("file"));
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
}

