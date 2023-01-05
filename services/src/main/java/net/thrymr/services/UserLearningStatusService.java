package net.thrymr.services;

import net.thrymr.dto.UserLearningStatusDto;
import net.thrymr.dto.response.PaginationResponse;

public interface UserLearningStatusService {

    String createUserLearningStatus(UserLearningStatusDto request);
    PaginationResponse getAllPaginationUserLearningStatus(UserLearningStatusDto request);
    String updateUserLearningStatus(UserLearningStatusDto request);
    String deleteUserLearningStatusById(String userId);

}
