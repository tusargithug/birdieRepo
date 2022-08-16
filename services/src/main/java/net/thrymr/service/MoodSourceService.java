package net.thrymr.service;

import net.thrymr.utils.ApiResponse;
import org.springframework.web.multipart.MultipartFile;

/*
 *@author Chanda Veeresh
 *@version 1.0
 *@since  11-08-2022
 */
public interface MoodSourceService {

    ApiResponse addMoodSourceByExcel(MultipartFile file);

    ApiResponse getAllMoodSources();
}
