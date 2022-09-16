package net.thrymr.services;


import net.thrymr.utils.ApiResponse;
import org.springframework.web.multipart.MultipartFile;




public interface MoodIntensityService {
    ApiResponse saveintensity(MultipartFile file);

    ApiResponse getMoodIntensityByMoodInfoId(Long id);
}
