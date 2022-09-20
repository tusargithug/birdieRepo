package net.thrymr.services;


import net.thrymr.dto.MoodIntensityDto;
import net.thrymr.dto.request.MoodSourceIntensityRequestDto;
import net.thrymr.utils.ApiResponse;
import org.springframework.web.multipart.MultipartFile;




public interface MoodIntensityService {
    ApiResponse saveintensity(MultipartFile file);

    ApiResponse getMoodIntensityByMoodInfoId(Long id);

    ApiResponse moodIntensitySave(MoodIntensityDto request);

    ApiResponse getMoodIntensitiesById(Long id);

    ApiResponse deleteMoodIntensitiesById(Long id);


    ApiResponse getAllMoodIntensities();

    ApiResponse getAllMoodIntensitiesByMoodInfoId(Long id);

    ApiResponse updateMoodIntensity(MoodSourceIntensityRequestDto request);
}
