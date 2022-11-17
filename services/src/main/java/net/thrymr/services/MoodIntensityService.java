package net.thrymr.services;
import net.thrymr.dto.request.MoodSourceIntensityRequestDto;
import net.thrymr.model.UserMoodCheckIn;
import net.thrymr.model.master.MtMoodIntensity;
import net.thrymr.utils.ApiResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


public interface MoodIntensityService {
    ApiResponse saveintensity(MultipartFile file);
    MtMoodIntensity getMoodIntensitiesById(Long id);

    String deleteMoodIntensitiesById(Long id);


    List<MtMoodIntensity> getAllMoodIntensities();

    List<MtMoodIntensity> getAllMoodIntensitiesByMoodInfoId(Long id);

    String updateMoodIntensity(MoodSourceIntensityRequestDto request);

    List<UserMoodCheckIn> getAllMoodCheckIn();

    String createUserMoodCheckIn(MoodSourceIntensityRequestDto request);

    String deleteUserMoodCheckInById(Long id);

}
