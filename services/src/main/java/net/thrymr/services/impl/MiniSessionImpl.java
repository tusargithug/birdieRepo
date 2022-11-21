package net.thrymr.services.impl;

import net.thrymr.FileDocument;
import net.thrymr.FileDocumentRepo;
import net.thrymr.dto.FileDetailsDto;

import net.thrymr.dto.GroupsDto;
import net.thrymr.dto.MiniSessionDto;
import net.thrymr.enums.FileType;


import net.thrymr.model.FileDetails;
import net.thrymr.model.GroupDetails;
import net.thrymr.model.Groups;
import net.thrymr.model.MiniSession;
import net.thrymr.repository.FileRepo;
import net.thrymr.repository.GroupRepo;
import net.thrymr.repository.MiniSessionRepo;
import net.thrymr.services.MiniSessionService;
import net.thrymr.utils.Validator;
import org.apache.poi.sl.draw.geom.GuideIf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MiniSessionImpl implements MiniSessionService {
    @Autowired
    MiniSessionRepo miniSessionRepo;
    @Autowired
    FileDocumentRepo fileDocumentRepo;
    @Autowired
    GroupRepo groupRepo;
    @Autowired
    FileRepo fileRepo;
    @Autowired
    GroupDetailsRepo groupDetailsRepo;

    @Autowired
    FileDetailsRepo fileDetailsRepo;

    @Override
    public String createMiniSession(MiniSessionDto request) {
        MiniSession miniSession = new MiniSession();
        miniSession.setMiniSessionName(request.getMiniSessionName());
        miniSession.setTags(request.getTags());
        miniSessionRepo.save(miniSession);
        return "Mini session save successfully";
    }

    @Override
    public String updateMiniSession(MiniSessionDto request) {
        MiniSession miniSession = null;
        if (Validator.isValid(request.getId())) {
            Optional<MiniSession> optionalMiniSession = miniSessionRepo.findById(request.getId());
            if (optionalMiniSession.isPresent()) {
                miniSession = optionalMiniSession.get();
                if(Validator.isValid(request.getMiniSessionName())) {
                    miniSession.setMiniSessionName(request.getMiniSessionName());
                }else if(Validator.isValid(request.getTags())) {
                    miniSession.setTags(request.getTags());
                }else {
                    return "invalid request";
                }
                miniSessionRepo.save(miniSession);
                return "Mini session updated successfully";
            }
        }
        return "This mini session id not present in database";
    }

    @Override
    public String deleteMiniSessionById(Long id) {
        MiniSession miniSession = null;
        if (Validator.isValid(id)) {
            Optional<MiniSession> optionalMiniSession = miniSessionRepo.findById(id);
            if (optionalMiniSession.isPresent()) {
                miniSession = optionalMiniSession.get();
                miniSession.setIsActive(Boolean.FALSE);
                miniSession.setIsDeleted(Boolean.TRUE);
                miniSessionRepo.save(miniSession);
            }
        }
        return "This not present in database";
    }

    @Override
    public MiniSession getMiniSessionById(Long id) {
        if (Validator.isValid(id)) {
            Optional<MiniSession> optionalMiniSession = miniSessionRepo.findById(id);
            if (optionalMiniSession.isPresent() && optionalMiniSession.get().getIsActive().equals(Boolean.TRUE)) {
                return optionalMiniSession.get();
            }
        }
        return new MiniSession();
    }

    @Override
    public List<MiniSession> getAllMiniSession() {
        List<MiniSession> miniSessionList = miniSessionRepo.findAll();
        if (!miniSessionList.isEmpty()) {
            return miniSessionList.stream().filter(obj -> obj.getIsActive().equals(Boolean.TRUE)).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    @Override
    public String createGroup(GroupsDto request) {
        Groups groups = new Groups();
        groups.setGroupName(request.getGroupName());
        if (request.getIsActive() != null && request.getIsActive().equals(Boolean.TRUE)) {
            groups.setIsActive(request.getIsActive());
        }
        if (Validator.isValid(request.getMiniSessionId())) {
            Optional<MiniSession> optionalMiniSession = miniSessionRepo.findById(request.getMiniSessionId());
            groups.setMiniSession(optionalMiniSession.get());
        }
        groupRepo.save(groups);
        return "group saved successfully";
    }

    @Override
    public String updateGroupById(GroupsDto request) {
        Groups groups = null;
        if (Validator.isValid(request.getId())) {
            Optional<Groups> optionalGroups = groupRepo.findById(request.getId());
            if (optionalGroups.isPresent()) {
                groups = optionalGroups.get();
                if (Validator.isValid(request.getGroupName())) {
                    groups.setGroupName(request.getGroupName());
                }
                if (request.getIsActive() != null && request.getIsActive().equals(Boolean.TRUE) || request.getIsActive().equals(Boolean.FALSE)) {
                    groups.setIsActive(request.getIsActive());
                }
                if (Validator.isValid(request.getMiniSessionId())) {
                    Optional<MiniSession> optionalMiniSession = miniSessionRepo.findById(request.getMiniSessionId());
                    groups.setMiniSession(optionalMiniSession.get());
                }
                groupRepo.save(groups);
                return "Group update successfully";
            }
        }
        return "This id not present in database";
    }

    @Override
    public String saveGroupDetails(GroupsDto request) {
        GroupDetails groupDetails = new GroupDetails();
        if (Validator.isValid(request.getGroupId())) {
            Optional<Groups> optionalGroups = groupRepo.findById(request.getGroupId());
            if (optionalGroups.isPresent()) {
                groupDetails.setGroups(optionalGroups.get());
            }
        }
        Optional<FileDetails> optionalFileDetails = fileDetailsRepo.findByFileId(request.getFileId());
        if (optionalFileDetails.isPresent()) {
            groupDetails.setFileId(request.getFileId());
        }else {
            return "please add file_id";
        }

        if (request.getIsImage() != null && request.getIsImage().equals(Boolean.TRUE)) {
            groupDetails.setIsImage(request.getIsImage());
        }
        if (request.getIsAudio() != null && request.getIsAudio().equals(Boolean.TRUE)) {
            groupDetails.setIsAudio(request.getIsAudio());
        }
        if (request.getIsVideo() != null && request.getIsVideo().equals(Boolean.TRUE)) {
            groupDetails.setIsVideo(request.getIsVideo());
        }
        if (request.getIsZif() != null && request.getIsZif().equals(Boolean.TRUE)) {
            groupDetails.setIsZif(request.getIsZif());
        }
        if (request.getIsPdf() != null && request.getIsPdf().equals(Boolean.TRUE)) {
            groupDetails.setIsPdf(request.getIsPdf());
        }
        if (request.getIsEmoji() != null && request.getIsEmoji().equals(Boolean.TRUE)) {
            groupDetails.setIsEmoji(request.getIsEmoji());
        }
        if (request.getIsText() != null && request.getIsText().equals(Boolean.TRUE)) {
            groupDetails.setIsText(request.getIsText());
            groupDetails.setText(request.getText());
        }
        groupDetailsRepo.save(groupDetails);
        return "Group details saved successfully";
    }

    @Override
    public List<GroupDetails> getAllGroupDetails() {
        List<GroupDetails> groupDetailsList = groupDetailsRepo.findAll();
        if (!groupDetailsList.isEmpty()) {
            return groupDetailsList.stream().filter(groupDetails -> groupDetails.getIsActive().equals(Boolean.FALSE)).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    @Override
    public Groups getGroupById(Long id) {
        if (Validator.isValid(id)) {
            Optional<Groups> optionalGroups = groupRepo.findById(id);
            if (optionalGroups.isPresent()) {
                return optionalGroups.get();
            }
        }
        return new Groups();
    }

    @Override
    public String saveFileDetails(FileDetailsDto request) {
        FileDetails fileDetails = new FileDetails();
        if (Validator.isValid(request.getContentType()) && request.getContentType().equals("IMAGE") || request.getContentType().equals("EMOJI")) {
            Optional<Groups> optionalGroups = groupRepo.findById(1l);
            saveFileDetails(request, fileDetails, optionalGroups);
        }
        if (Validator.isValid(request.getContentType()) && request.getContentType().equals("PDF") || request.getContentType().equals("ZIP") || request.getContentType().equals("DOCUMENT")) {
            Optional<Groups> optionalGroups = groupRepo.findById(2l);
            saveFileDetails(request, fileDetails, optionalGroups);
        }
        if (Validator.isValid(request.getContentType()) && request.getContentType().equals("VIDEO") || request.getContentType().equals("AUDIO")) {
            Optional<Groups> optionalGroups = groupRepo.findById(3l);
            saveFileDetails(request, fileDetails, optionalGroups);
        }
        return "File details saved successfully";
    }

    private FileDetails saveFileDetails(FileDetailsDto request, FileDetails fileDetails, Optional<Groups> optionalGroups) {
        if (optionalGroups.isPresent()) {
            fileDetails.setGroups(optionalGroups.get());
            fileDetails.setFileId(request.getFileId());
            fileDetails.setFileName(request.getFileName());
            fileDetails.setFileContentType(FileType.valueOf(request.getContentType()));
            fileDetailsRepo.save(fileDetails);
        }
        return fileDetails;
    }
}
