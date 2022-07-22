package net.thrymr.service;

import net.thrymr.utils.ApiResponse;
import org.springframework.web.multipart.MultipartFile;

public interface MoodInfoService {
    ApiResponse saveMoodInfo(MultipartFile file);
    ApiResponse getAllMoods();

}
