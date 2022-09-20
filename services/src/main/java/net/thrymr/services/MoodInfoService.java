package net.thrymr.services;

import net.thrymr.utils.ApiResponse;
import org.springframework.web.multipart.MultipartFile;

public interface MoodInfoService {
    ApiResponse saveMoodInfo(MultipartFile file);
    ApiResponse getAllMoods();

    ApiResponse getMoodInfoById(Long id);

    ApiResponse deleteMoodInfoById(Long id);
}
