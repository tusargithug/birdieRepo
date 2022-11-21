package net.thrymr.services;

import net.thrymr.dto.MoodSourceDto;
import net.thrymr.dto.request.MoodSourceIntensityRequestDto;
import net.thrymr.model.master.MtMoodSource;
import net.thrymr.utils.ApiResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


public interface MoodSourceService {

    ApiResponse addMoodSourceByExcel(MultipartFile file);

    List<MtMoodSource> getAllMoodSources();

    MtMoodSource getMoodSourceById(Long id);

    ApiResponse moodSourceSave(MoodSourceDto request);

    String updateMoodSourceById(MoodSourceDto request);

    String deleteMoodSourceById(Long id);

    // ApiResponse updateMoodSource(MoodSourceIntensityRequestDto request);

    String createUserMoodSourceCheckIn(MoodSourceIntensityRequestDto request);

    String deleteUserMoodSourceCheckInById(Long id);
}
