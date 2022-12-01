package net.thrymr.services.impl;

import net.thrymr.dto.CityDto;
import net.thrymr.dto.CountryDto;
import net.thrymr.dto.RegionDto;
import net.thrymr.model.master.MtCity;
import net.thrymr.model.master.MtCountry;
import net.thrymr.model.master.MtRegion;
import net.thrymr.repository.CityRepo;
import net.thrymr.repository.CountryRepo;
import net.thrymr.repository.RegionRepo;
import net.thrymr.repository.SiteRepo;
import net.thrymr.services.CityCountyAndRegionService;
import net.thrymr.utils.Validator;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        mtCountry.setSearchKey(getCountrySearchKey(mtCountry));
        countryRepo.save(mtCountry);
        return "Country saved successfully";
    }

    @Override
    public String updateCountryById(CountryDto countryDto) {
        Optional<MtCountry> mtCountryId=countryRepo.findById(countryDto.getId());
        MtCountry mtCountry;
        if(mtCountryId.isPresent()) {
            mtCountry = mtCountryId.get();
            if (Validator.isValid(countryDto.getCountryName())) {
                mtCountry.setCountryName(countryDto.getCountryName());
            }
            if(Validator.isValid(countryDto.getCountryCode())) {
                mtCountry.setCountryCode(countryDto.getCountryCode());
            }
            mtCountry.setSearchKey(getCountrySearchKey(mtCountry));
            countryRepo.save(mtCountry);
            return "Country update successfully";
        }
        return "this id not in database";

    }


    public String getAppUserSearchKey(MtCity mtCity) {
        String searchKey = "";
        if (mtCity.getCityName() != null) {
            searchKey = searchKey + " " + mtCity.getCityName();
        }
        if (mtCity.getCountry() != null) {
            searchKey = searchKey + " " + mtCity.getCountry();
        }
        if (mtCity.getSites() != null) {
            searchKey = searchKey + " " + mtCity.getSites();
        }
        return searchKey;
    }

    @Override
    public List<MtCountry> getAllCountry() {
        List<MtCountry> mtCountries=countryRepo.findAll();
        if(!mtCountries.isEmpty()) {
            return mtCountries.stream().filter(obj-> obj.getIsActive().equals(Boolean.TRUE)).collect(Collectors.toList());
        }
        return new ArrayList<>();
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

    public String getCountrySearchKey(MtCountry country) {
        String searchKey = "";
        if (country.getCountryName() != null) {
            searchKey = searchKey + " " + country.getCountryName();
        }
        if (country.getCountryCode() != null) {
            searchKey = searchKey + " " + country.getCountryCode();
        }
        if (country.getRegion() != null) {
            searchKey = searchKey + " " + country.getRegion();
        }
        if (country.getCities() != null) {
            searchKey = searchKey + " " + country.getCities();
        }
        return searchKey;
    }

    @Override
    public String saveCity(CityDto cityDto) {
        MtCity mtCity=new MtCity();
        mtCity.setCityName(cityDto.getCityName());
        Optional<MtCountry> mtCountryId=countryRepo.findById(cityDto.getMtCountryId());
        if(mtCountryId.isPresent()) {
            mtCity.setCountry(mtCountryId.get());
        }
        mtCity.setSearchKey(getAppUserSearchKey(mtCity));
        cityRepo.save(mtCity);
        return "City saved successfully";
    }

    public String getRegionSearchKey(MtRegion region) {
        String searchKey = "";
        if (region.getRegionName() != null) {
            searchKey = searchKey + " " + region.getRegionName();
        }
        if (region.getMtCountry() != null) {
            searchKey = searchKey + " " + region.getMtCountry();
        }
        if (region.getSite() != null) {
            searchKey = searchKey + " " + region.getSite();
        }
         return searchKey;
    }
    @Override
    public String updateCityById(CityDto cityDto) {
        Optional<MtCity> mtCityId=cityRepo.findById(cityDto.getId());
        MtCity mtCity;
        if(mtCityId.isPresent()){
            mtCity=mtCityId.get();
            if(Validator.isValid(cityDto.getCityName())) {
                mtCity.setCityName(cityDto.getCityName());
                cityRepo.save(mtCity);
            }
            mtCity.setSearchKey(getAppUserSearchKey(mtCity));
            return "City update successfully";
        }
        return "this id not in data base";
    }

    @Override
    public List<MtCity> getAllCities() {
        List<MtCity> mtCity=cityRepo.findAll();
        if(!mtCity.isEmpty()) {
            return mtCity.stream().filter(obj-> obj.getIsActive().equals(Boolean.TRUE)).collect(Collectors.toList());
        }
        return new ArrayList<>();
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
        mtRegion.setSearchKey(getRegionSearchKey(mtRegion));
        regionRepo.save(mtRegion);
        return "Region saved successfully";
    }

    @Override
    public String updateRegionById(RegionDto regionDto) {
        Optional<MtRegion> mtRegionId=regionRepo.findById(regionDto.getId());
        MtRegion mtRegion;
        if(mtRegionId.isPresent()){
            mtRegion=mtRegionId.get();
            if(Validator.isValid(regionDto.getId())) {
                mtRegion.setRegionName(regionDto.getRegionName());
            }
            mtRegion.setSearchKey(getRegionSearchKey(mtRegion));
            regionRepo.save(mtRegion);
            return "region update successfully";
        }
        return "this id not in data base";
    }

    @Override
    public List<MtRegion> getAllRegions() {
        List<MtRegion> mtRegions=regionRepo.findAll();
        if(!mtRegions.isEmpty()) {
            return mtRegions.stream().filter(obj-> obj.getIsActive().equals(Boolean.TRUE)).collect(Collectors.toList());
        }
        return new ArrayList<>();
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
    public String uploadCountryData(MultipartFile file) {
        List<MtCountry> countryList=new ArrayList<>();
        XSSFWorkbook workbook;
        try {
            workbook = new XSSFWorkbook(file.getInputStream());
        } catch (IOException e) {
            return "COUNTRY.IMPORT.FORMAT.FAILED";
        }
        XSSFSheet worksheet = workbook.getSheetAt(0);
        if (worksheet.getLastRowNum() < 1) {
            return "COUNTRY.IMPORT.FORMAT.INVALID.DATA";
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
                        country.setInternationalDialing(getCellValue(row.getCell(11)));
                    }
                    if(row.getCell(12)!=null){
                        Optional<MtRegion> mtRegion = regionRepo.findById(Long.valueOf(getCellValue(row.getCell(12))));
                        if(mtRegion.isPresent()){
                            country.setRegion(mtRegion.get());
                        }
                    }
                    setCountrySearchKey(country);
                    countryList.add(country);
                }catch (Exception e) {
                    return "COUNTRY.IMPORT.FORMAT.FAILED";
                }
            }

        }
        if (Validator.isObjectValid(countryList)) {
            countryRepo.saveAll(countryList);
            return "COUNTRY.IMPORT.SUCCESS";

        }
        return "COUNTRY.IMPORT.FAILED";

    }

    @Override
    public String uploadCityData(MultipartFile file) {
        List<MtCity> mtCityList = new ArrayList<>();
        XSSFWorkbook workbook;
        try {
            workbook = new XSSFWorkbook(file.getInputStream());
        } catch (IOException e) {
            return "CITY.IMPORT.FORMAT.FAILED";
        }
        XSSFSheet worksheet = workbook.getSheetAt(0);
        if (worksheet.getLastRowNum() < 1) {
            return "CITY.IMPORT.FORMAT.INVALID.DATA";
        }
        XSSFSheet workSheet= workbook.getSheetAt(0);
        if (workSheet.getLastRowNum() < 1) {
            return "CITY.IMPORT.FORMAT.INVALID.DATA";
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
                    return "CITY.IMPORT.FORMAT.FAILED";
                }
            }

        }
        if (Validator.isObjectValid(mtCityList)) {
            cityRepo.saveAll(mtCityList);
            return "CITY.IMPORT.SUCCESS";
        }
        return "CITY.IMPORT.FAILED";
    }

    @Override
    public String uploadRegionData(MultipartFile file) {
        List<MtRegion> MtRegionList = new ArrayList<>();
        XSSFWorkbook workbook;
        try {
            workbook = new XSSFWorkbook(file.getInputStream());
        } catch (IOException e) {
            return "REGION.IMPORT.FORMAT.FAILED";
        }
        XSSFSheet worksheet = workbook.getSheetAt(0);
        if (worksheet.getLastRowNum() < 1) {
            return "REGION.IMPORT.FORMAT.INVALID.DATA";
        }
        XSSFSheet workSheet= workbook.getSheetAt(0);
        if (workSheet.getLastRowNum() < 1) {
            return  "REGION.IMPORT.FORMAT.INVALID.DATA";
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
                    return  "REGION.IMPORT.FORMAT.FAILED";
                }
            }

        }
        if (Validator.isObjectValid(MtRegionList)) {
            regionRepo.saveAll(MtRegionList);
            return "REGION.IMPORT.SUCCESS";
        }
        return  "REGION.IMPORT.FAILED";
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
}
