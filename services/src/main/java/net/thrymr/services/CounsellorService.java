package net.thrymr.services;

import net.thrymr.dto.CounsellorDto;
import net.thrymr.dto.EducationDto;
import net.thrymr.dto.LanguageDto;
import net.thrymr.dto.response.PaginationResponse;
import net.thrymr.model.Counsellor;
import net.thrymr.model.master.EducationDetails;
import net.thrymr.model.master.LanguageDetails;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CounsellorService {
    String createCounsellor(CounsellorDto counsellorDto);

    String updateCounsellorById(CounsellorDto counsellorDto);

    String deleteCounsellorById(Long id);

    PaginationResponse getAllCounsellorPagination(CounsellorDto response);

    Counsellor getCounsellorById(Long id);

    String updateCounsellors(List<CounsellorDto> counsellorDtoList);

    List<EducationDetails> getAllEducationalDetails();

    List<LanguageDetails> getAllLanguagesDetails();

    String addNewLanguage(LanguageDto request);

    String addNewEducation(EducationDto request);

    String updateEducationDetailsById(EducationDto request);

    String updateLanguageDetailsById(LanguageDto request);

    EducationDetails getEducationalDetailsById(Long id);

    LanguageDetails getLanguageDetailsById(Long id);


    String deleteAllEducation();
}
