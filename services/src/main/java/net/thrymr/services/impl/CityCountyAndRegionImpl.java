package net.thrymr.services.impl;

import net.thrymr.dto.CityDto;
import net.thrymr.dto.CountryDto;
import net.thrymr.dto.RegionDto;
import net.thrymr.dto.UnitDto;
import net.thrymr.model.Site;
import net.thrymr.model.Unit;
import net.thrymr.model.master.MtCity;
import net.thrymr.model.master.MtCountry;
import net.thrymr.model.master.MtRegion;
import net.thrymr.repository.CityRepo;
import net.thrymr.repository.CountryRepo;
import net.thrymr.repository.RegionRepo;
import net.thrymr.repository.SiteRepo;
import net.thrymr.services.CityCountyAndRegionService;
import net.thrymr.utils.ApiResponse;
import net.thrymr.utils.Validator;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.plaf.synth.Region;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static net.thrymr.utils.DateUtils.dateToString;

@Service
public class CityCountyAndRegionImpl implements CityCountyAndRegionService {
    @Autowired
    CountryRepo countryRepo;
    @Autowired
    CityRepo cityRepo;
    @Autowired
    RegionRepo regionRepo;

    @Autowired
    SiteRepo siteRepo;
    @Override
    public String saveCountry(CountryDto countryDto) {
        MtCountry mtCountry=new MtCountry();
        mtCountry.setCountryName(countryDto.getCountryName());
        mtCountry.setCountryCode(countryDto.getCountryCode());
        Optional<MtRegion> mtRegionId=regionRepo.findById(countryDto.getRegionId());
        if(mtRegionId.isPresent()) {
            mtCountry.setRegion(mtRegionId.get());
        }

       countryRepo.save(mtCountry);
       return "Country saved successfully";
    }

    @Override
    public String updateCountryById(Long id,CountryDto countryDto) {
        Optional<MtCountry> mtCountryId=countryRepo.findById(id);
        MtCountry mtCountry;
        if(mtCountryId.isPresent()){
            mtCountry=mtCountryId.get();
            mtCountry.setCountryName(countryDto.getCountryName());
            mtCountry.setCountryCode(countryDto.getCountryCode());
            countryRepo.save(mtCountry);
            return "Country update successfully";
        }
        return "this id not in database";

    }

    @Override
    public List<MtCountry> getAllCountry() {
        List<MtCountry> mtCountries=countryRepo.findAll();
        return mtCountries;
    }
    @Override
    public String deleteCountryById(Long id) {
        Optional<MtCountry> mtCountryId=countryRepo.findById(id);
        MtCountry mtCountry;
        if(mtCountryId.isPresent()){
            mtCountry=mtCountryId.get();
            mtCountry.setIsActive(Boolean.FALSE);
            mtCountry.setIsDeleted(Boolean.TRUE);
            countryRepo.save(mtCountry);
            return "delete record successfully";
        }
        return "this id not in database";
    }


    @Override
    public String saveCity(CityDto cityDto) {
        MtCity mtCity=new MtCity();
        mtCity.setCityName(cityDto.getCityName());
        Optional<MtCountry> mtCountryId=countryRepo.findById(cityDto.getMtCountryId());
        if(mtCountryId.isPresent()) {
            mtCity.setCountry(mtCountryId.get());
        }
        cityRepo.save(mtCity);
        return "City saved successfully";
    }

    @Override
    public String updateCityById(Long id, CityDto cityDto) {
        Optional<MtCity> mtCityId=cityRepo.findById(id);
        MtCity mtCity;
        if(mtCityId.isPresent()){
            mtCity=mtCityId.get();
            mtCity.setCityName(mtCity.getCityName());
            cityRepo.save(mtCity);
           return  "City update successfully";
        }
        return "this id not in data base";
    }

    @Override
    public List<MtCity> getAllCities() {
        List<MtCity> mtCity=cityRepo.findAll();
        return mtCity;
    }

    @Override
    public String deleteCityById(Long id) {
        Optional<MtCity> mtCityId=cityRepo.findById(id);
        MtCity mtCity;
        if(mtCityId.isPresent()){
            mtCity=mtCityId.get();
            mtCity.setIsActive(Boolean.FALSE);
            mtCity.setIsDeleted(Boolean.TRUE);
            cityRepo.save(mtCity);
            return "delete record successfully";
        }
        return "this id not in data base";
    }


    @Override
    public String saveRegion(RegionDto regionDto) {
        MtRegion mtRegion=new MtRegion();
        mtRegion.setRegionName(regionDto.getRegionName());
        regionRepo.save(mtRegion);
        return "Region saved successfully";
    }

    @Override
    public String updateRegionById(Long id ,RegionDto regionDto) {
        Optional<MtRegion> mtRegionId=regionRepo.findById(id);
        MtRegion mtRegion;
        if(mtRegionId.isPresent()){
            mtRegion=mtRegionId.get();
            mtRegion.setRegionName(regionDto.getRegionName());
            regionRepo.save(mtRegion);
            return "region update successfully";
        }
        return "this id not in data base";
    }

    @Override
    public List<MtRegion> getAllRegions() {
        List<MtRegion> mtRegions=regionRepo.findAll();
        return mtRegions;
    }
    @Override
    public String deleteRegionById(Long id) {
        Optional<MtRegion> mtRegionId=regionRepo.findById(id);
        MtRegion mtRegion;
        if(mtRegionId.isPresent()){
            mtRegion=mtRegionId.get();
            mtRegion.setIsActive(Boolean.FALSE);
            mtRegion.setIsDeleted(Boolean.TRUE);
            regionRepo.save(mtRegion);
            return "delete record successfully";
        }
        return "this id not in data base";
    }

    @Override
    public ApiResponse uploadCountryData(MultipartFile file) {
        List<MtCountry> countryList=new ArrayList<>();
        XSSFWorkbook workbook;
        try {
            workbook = new XSSFWorkbook(file.getInputStream());
        } catch (IOException e) {
            return new ApiResponse(HttpStatus.BAD_REQUEST, "COUNTRY.IMPORT.FORMAT.FAILED");
        }
        XSSFSheet worksheet = workbook.getSheetAt(0);
        if (worksheet.getLastRowNum() < 1) {
            return new ApiResponse(HttpStatus.BAD_REQUEST, "COUNTRY.IMPORT.FORMAT.INVALID.DATA");
        }
        for (int index = 1; index <= worksheet.getLastRowNum(); index++) {
            if (index > 0) {
                try {
                    XSSFRow row = worksheet.getRow(index);
                    MtCountry country=new MtCountry();

                    if(row.getCell(9)!=null){
                        country.setCountryCode(getCellValue(row.getCell(9)));
                    }
                    if(row.getCell(10)!=null){
                        country.setCountryName(getCellValue(row.getCell(10)));
                    }
                    if(row.getCell(11)!=null){
                        Optional<MtRegion> mtRegion = regionRepo.findById(Long.valueOf(getCellValue(row.getCell(11))));
                        if(mtRegion.isPresent()){
                            country.setRegion(mtRegion.get());
                        }
                    }
                    setCountrySearchKey(country);
                    countryList.add(country);
                }catch (Exception e) {
                    return new ApiResponse(HttpStatus.BAD_REQUEST, "COUNTRY.IMPORT.FORMAT.FAILED");
                }
            }

        }
        if (Validator.isObjectValid(countryList)) {
            countryRepo.saveAll(countryList);
            return new ApiResponse(HttpStatus.OK, "COUNTRY.IMPORT.SUCCESS");

        }
        return new ApiResponse(HttpStatus.BAD_REQUEST, "COUNTRY.IMPORT.FAILED");

    }

    @Override
    public ApiResponse uploadCityData(MultipartFile file) {
        List<MtCity> mtCityList = new ArrayList<>();
        XSSFWorkbook workbook;
        try {
            workbook = new XSSFWorkbook(file.getInputStream());
        } catch (IOException e) {
            return new ApiResponse(HttpStatus.BAD_REQUEST, "CITY.IMPORT.FORMAT.FAILED");
        }
        XSSFSheet worksheet = workbook.getSheetAt(0);
        if (worksheet.getLastRowNum() < 1) {
            return new ApiResponse(HttpStatus.BAD_REQUEST, "CITY.IMPORT.FORMAT.INVALID.DATA");
        }
        XSSFSheet workSheet= workbook.getSheetAt(0);
        if (workSheet.getLastRowNum() < 1) {
            return new ApiResponse(HttpStatus.BAD_REQUEST, "CITY.IMPORT.FORMAT.INVALID.DATA");
        }
        for (int index = 1; index <= workSheet.getLastRowNum(); index++) {
            if (index > 0) {
                try {
                    XSSFRow row = workSheet.getRow(index);
                    MtCity mtCity=new MtCity();

                    if(row.getCell(9)!=null){
                        mtCity.setCityName(getCellValue(row.getCell(9)));
                    }
                    if(row.getCell(10)!=null){
                        Optional<MtCountry> mtCountry = countryRepo.findById(Long.valueOf(getCellValue(row.getCell(10))));
                        if(mtCountry.isPresent()){
                            mtCity.setCountry(mtCountry.get());
                        }
                    }
                    setCitySearchKey(mtCity);
                    mtCityList.add(mtCity);
                }catch (Exception e) {
                    return new ApiResponse(HttpStatus.BAD_REQUEST, "CITY.IMPORT.FORMAT.FAILED");
                }
            }

        }
        if (Validator.isObjectValid(mtCityList)) {
            cityRepo.saveAll(mtCityList);
            return new ApiResponse(HttpStatus.OK, "CITY.IMPORT.SUCCESS");
        }
        return new ApiResponse(HttpStatus.BAD_REQUEST, "CITY.IMPORT.FAILED");
    }

    @Override
    public ApiResponse uploadRegionData(MultipartFile file) {
        List<MtRegion> MtRegionList = new ArrayList<>();
        XSSFWorkbook workbook;
        try {
            workbook = new XSSFWorkbook(file.getInputStream());
        } catch (IOException e) {
            return new ApiResponse(HttpStatus.BAD_REQUEST, "REGION.IMPORT.FORMAT.FAILED");
        }
        XSSFSheet worksheet = workbook.getSheetAt(0);
        if (worksheet.getLastRowNum() < 1) {
            return new ApiResponse(HttpStatus.BAD_REQUEST, "REGION.IMPORT.FORMAT.INVALID.DATA");
        }
        XSSFSheet workSheet= workbook.getSheetAt(0);
        if (workSheet.getLastRowNum() < 1) {
            return new ApiResponse(HttpStatus.BAD_REQUEST, "REGION.IMPORT.FORMAT.INVALID.DATA");
        }
        for (int index = 1; index <= workSheet.getLastRowNum(); index++) {
            if (index > 0) {
                try {
                    XSSFRow row = workSheet.getRow(index);
                    MtRegion mtRegion=new MtRegion();

                    if(row.getCell(9)!=null){
                        mtRegion.setRegionName(getCellValue(row.getCell(9)));
                    }
                    setRegionSearchKey(mtRegion);
                    MtRegionList.add(mtRegion);
                }catch (Exception e) {
                    return new ApiResponse(HttpStatus.BAD_REQUEST, "REGION.IMPORT.FORMAT.FAILED");
                }
            }

        }
        if (Validator.isObjectValid(MtRegionList)) {
            regionRepo.saveAll(MtRegionList);
            return new ApiResponse(HttpStatus.OK, "REGION.IMPORT.SUCCESS");
        }
        return new ApiResponse(HttpStatus.BAD_REQUEST, "REGION.IMPORT.FAILED");
    }

    private String getCellValue(XSSFCell cell) {
        String value;
        if (cell.getCellType().equals(CellType.NUMERIC)) {
            value = NumberToTextConverter.toText(cell.getNumericCellValue());
        } else if (cell.getCellType().equals(CellType.STRING)) {
            value = cell.getStringCellValue();
        } else if (cell.getCellType().equals(CellType.BOOLEAN)) {
            value = String.valueOf(cell.getBooleanCellValue());
        }else if (cell.getCellType().equals(CellType.BLANK) || cell.getCellType().equals(CellType.ERROR)) {
            value = "";
        } else {
            value = cell.getRawValue();
        }
        return value;
    }
    private void setCountrySearchKey(MtCountry country) {
        String searchKey = "";
        if (Validator.isValid(country.getCountryName())) {
            searchKey = searchKey + country.getCountryName();
        }
        country.setSearchKey(searchKey);
    }
    private void setCitySearchKey(MtCity mtCity) {
        String searchKey = "";
        if (Validator.isValid(mtCity.getCityName())) {
            searchKey = searchKey + mtCity.getCityName();
        }
        mtCity.setSearchKey(searchKey);
    }
    private void setRegionSearchKey(MtRegion mtRegion) {
        String searchKey = "";
        if (Validator.isValid(mtRegion.getRegionName())) {
            searchKey = searchKey + mtRegion.getRegionName();
        }
        mtRegion.setSearchKey(searchKey);
    }

    public CountryDto entityToDtoForGetAllCountry(MtCountry mtCountry) {
        CountryDto countryDto = new CountryDto();
        countryDto.setCountryName(mtCountry.getCountryName());
        countryDto.setCountryCode(mtCountry.getCountryCode());
        return countryDto;
    }

    public CityDto entityToDtoForGetAllCity(MtCity mtCity){
       CityDto cityDto=new CityDto();
       cityDto.setCityName(mtCity.getCityName());
       return cityDto;
    }

    public RegionDto entityToDtoForGetAllRegion(MtRegion mtRegion){
        RegionDto regionDto=new RegionDto();
        regionDto.setRegionName(mtRegion.getRegionName());
        return regionDto;
    }

}
