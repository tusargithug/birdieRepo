package net.thrymr.services;

import net.thrymr.dto.CityDto;
import net.thrymr.dto.CountryDto;
import net.thrymr.dto.RegionDto;
import net.thrymr.model.master.MtCity;
import net.thrymr.model.master.MtCountry;
import net.thrymr.model.master.MtRegion;
import net.thrymr.utils.ApiResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


public interface CityCountyAndRegionService {

    String saveCountry(CountryDto countryDto);

    String updateCountryById(Long id,CountryDto countryDto);

    List<MtCountry> getAllCountry();

    String saveCity(CityDto cityDto);

    String updateCityById(Long id,CityDto cityDto);

    List<MtCity> getAllCities();

    String saveRegion(RegionDto regionDto);

    String updateRegionById(Long id,RegionDto regionDto);

    List<MtRegion> getAllRegions();

    String deleteCountryById(Long id);

    String deleteCityById(Long id);

    String deleteRegionById(Long id);

    ApiResponse uploadCountryData(MultipartFile file);

    ApiResponse uploadCityData(MultipartFile file);

    ApiResponse uploadRegionData(MultipartFile file);
}
