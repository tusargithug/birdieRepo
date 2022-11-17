package net.thrymr.services;

import net.thrymr.dto.GroupsDto;
import net.thrymr.dto.MiniSessionDto;
import net.thrymr.model.MiniSession;

import java.util.List;

public interface MiniSessionService {
    String saveMiniSession(MiniSessionDto request);

    String updateMiniSession(MiniSessionDto request);

    String deleteMiniSessionById(Long id);

    MiniSession getMiniSessionById(Long id);

    List<MiniSession> getAllMiniSession();

    String createGroup(GroupsDto request);

    String updateGroupById(GroupsDto request);
}
