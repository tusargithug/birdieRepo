package net.thrymr.services;

import net.thrymr.dto.MoodInfoDto;
import net.thrymr.model.master.MtMoodInfo;
import net.thrymr.utils.ApiResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MoodInfoService {
    ApiResponse saveMoodInfo(MultipartFile file);

    List<MtMoodInfo> getAllMoodInfo(String searchKey);

    MtMoodInfo getMoodInfoById(Long id);

    String updateMoodInfoById(MoodInfoDto request);

    String deleteMoodInfoById(Long id);
}
