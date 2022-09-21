package net.thrymr.services;

import net.thrymr.dto.LearningVideoDto;
import net.thrymr.utils.ApiResponse;

public interface LearningVideoService {
    ApiResponse saveLearningVideo(LearningVideoDto request);
}
