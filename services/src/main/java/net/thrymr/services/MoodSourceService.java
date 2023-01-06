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

    String updateMoodSourceById(MoodSourceDto request);

    String deleteMoodSourceById(Long id);

    String deleteUserMoodSourceCheckInById(Long id);

    List<MtMoodSource> getAllMoodSourceById(MoodSourceIntensityRequestDto request);
}
