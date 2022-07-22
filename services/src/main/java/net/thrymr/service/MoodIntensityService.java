package net.thrymr.service;

import net.thrymr.dto.MoodIntensityDto;
import net.thrymr.utils.ApiResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/*
 *@author Chanda Veeresh
 *@version 1.0
 *@since  15-07-2022
 */
public interface MoodIntensityService {
    ApiResponse saveintensity(MultipartFile file);

    List<MoodIntensityDto> getMoodIntensityByMoodInfoId(Long id);
}
