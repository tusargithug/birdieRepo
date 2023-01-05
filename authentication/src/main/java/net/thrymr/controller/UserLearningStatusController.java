package net.thrymr.controller;

import net.thrymr.dto.UserLearningStatusDto;
import net.thrymr.dto.response.PaginationResponse;
import net.thrymr.services.UserLearningStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserLearningStatusController {

    @Autowired
    private UserLearningStatusService userLearningStatusService;

    @MutationMapping(name = "createUserLearningStatus")
    public String createUserLearningStatus(@Argument(name = "input") UserLearningStatusDto request) {
        return userLearningStatusService.createUserLearningStatus(request);
    }

    @MutationMapping(name = "updateUserLearningStatus")
    public String updateUserLearningStatus(@Argument(name = "input") UserLearningStatusDto request) {
        return userLearningStatusService.updateUserLearningStatus(request);
    }

    @QueryMapping(name = "getAllPaginationUserLearningStatus")
    public PaginationResponse getAllPaginationUserLearningStatus(@Argument(name = "input") UserLearningStatusDto request) {
        return userLearningStatusService.getAllPaginationUserLearningStatus(request);
    }

    @MutationMapping(name = "deleteUserLearningStatusById")
    public String deleteUserLearningStatusById(@Argument String userId) {
        return userLearningStatusService.deleteUserLearningStatusById(userId);
    }



}
