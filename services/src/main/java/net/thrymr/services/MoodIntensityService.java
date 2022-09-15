package net.thrymr.services;

import net.thrymr.dto.MoodIntensityDto;
import net.thrymr.utils.ApiResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


public interface MoodIntensityService {
    ApiResponse saveintensity(MultipartFile file);

    ApiResponse getMoodIntensityByMoodInfoId(Long id);
}
