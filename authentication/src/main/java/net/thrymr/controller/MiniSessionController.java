package net.thrymr.controller;

import net.thrymr.dto.GroupsDto;
import net.thrymr.dto.MiniSessionDto;
import net.thrymr.model.GroupDetails;
import net.thrymr.model.Groups;
import net.thrymr.model.MiniSession;
import net.thrymr.services.MiniSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MiniSessionController {
    @Autowired
    MiniSessionService miniSessionService;

    @MutationMapping(name = "createMiniSession")
    public String createMiniSession(@Argument(name = "input") MiniSessionDto request) {
        return miniSessionService.createMiniSession(request);
    }

    @MutationMapping(name = "updateMiniSession")
    public String updateMiniSession(@Argument(name = "input") MiniSessionDto request) {
        return miniSessionService.updateMiniSession(request);
    }

    @MutationMapping(name = "deleteMiniSessionById")
    public String deleteMiniSessionById(@Argument Long id) {
        return miniSessionService.deleteMiniSessionById(id);
    }

    @QueryMapping(name = "getMiniSessionById")
    public MiniSession getMiniSessionById(@Argument Long id) {
        return miniSessionService.getMiniSessionById(id);
    }

    @QueryMapping(name = "getAllMiniSession")
    public List<MiniSession> getAllMiniSession() {
        return miniSessionService.getAllMiniSession();
    }

    @MutationMapping(name = "createGroup")
    public String createGroup(@Argument(name = "input") GroupsDto request) {
        return miniSessionService.createGroup(request);
    }

    @QueryMapping(name="getGroupById")
    public Groups getGroupById(@Argument Long id) {
        return miniSessionService.getGroupById(id);
    }

    @MutationMapping(name = "saveGroupDetails")
    public String saveGroupDetails(@Argument(name = "input") GroupsDto request) {
        return miniSessionService.saveGroupDetails(request);
    }

    @QueryMapping(name = "getAllGroupDetails")
    public List<GroupDetails> getAllGroupDetails() {
        return miniSessionService.getAllGroupDetails();
    }

    @MutationMapping(name = "updateGroupById")
    public String updateGroupById(@Argument(name = "input") GroupsDto request) {
        return miniSessionService.updateGroupById(request);
    }
}
