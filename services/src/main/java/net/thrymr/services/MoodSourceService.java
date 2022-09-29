package net.thrymr.services;

import net.thrymr.dto.MoodSourceDto;
import net.thrymr.dto.request.MoodSourceIntensityRequestDto;
import net.thrymr.utils.ApiResponse;
import org.springframework.web.multipart.MultipartFile;


public interface MoodSourceService {

    ApiResponse addMoodSourceByExcel(MultipartFile file);

    ApiResponse getAllMoodSources();

    ApiResponse moodSourceSave(MoodSourceDto request);

    ApiResponse updateMoodSource(MoodSourceIntensityRequestDto request);

    String createUserMoodSourceCheckIn(MoodSourceIntensityRequestDto request);

    String deleteUserMoodSourceCheckInById(Long id);
}
