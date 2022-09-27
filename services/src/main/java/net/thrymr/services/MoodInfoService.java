package net.thrymr.services;

import net.thrymr.model.master.MtMoodInfo;
import net.thrymr.utils.ApiResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MoodInfoService {
    ApiResponse saveMoodInfo(MultipartFile file);
    ApiResponse getAllMoods();

    ApiResponse getMoodInfoById(Long id);

    ApiResponse deleteMoodInfoById(Long id);

    List<MtMoodInfo> getAllMoodInfo();
}
