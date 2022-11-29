package net.thrymr.services;

import net.thrymr.dto.FileDetailsDto;
import net.thrymr.dto.GroupsDto;
import net.thrymr.dto.MiniSessionDto;
import net.thrymr.dto.PaginationResponse;
import net.thrymr.enums.TagType;
import net.thrymr.model.GroupDetails;
import net.thrymr.model.Groups;
import net.thrymr.model.MiniSession;
import org.springframework.data.domain.Page;

import java.util.List;

public interface MiniSessionService {
    String createMiniSession(MiniSessionDto request);

    String updateMiniSession(MiniSessionDto request);

    String deleteMiniSessionById(Long id);

    MiniSession getMiniSessionById(Long id);

    List<MiniSession> getAllMiniSession();

    String createGroup(GroupsDto request);

    String updateGroupById(GroupsDto request);

    String saveGroupDetails(GroupsDto request);

    List<GroupDetails> getAllGroupDetails();

    Groups getGroupById(Long id);

    String saveFileDetails(FileDetailsDto request);

    PaginationResponse getAllMiniSessionPagination(MiniSessionDto request);

    List<TagType> getAllEnumTags();

    List<Groups> getAllGroups();
}
