package net.thrymr.services.impl;

import net.thrymr.FileDocument;
import net.thrymr.FileDocumentRepo;
import net.thrymr.dto.FileDetailsDto;

import net.thrymr.dto.GroupsDto;
import net.thrymr.dto.MiniSessionDto;
import net.thrymr.enums.FileType;


import net.thrymr.model.FileDetails;
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

    @Override
    public String saveMiniSession(MiniSessionDto request) {
        MiniSession miniSession = new MiniSession();
        if(request.getIsActive().equals(Boolean.TRUE)) {
            miniSession.setIsActive(request.getIsActive());
        }
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
                if (request.getIsActive().equals(Boolean.TRUE) || request.getIsActive().equals(Boolean.FALSE)) {
                    miniSession.setIsActive(request.getIsActive());
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
        if(request.getIsActive()!=null && request.getIsActive().equals(Boolean.TRUE)) {
            groups.setIsActive(request.getIsActive());
        }
        groups.setText(request.getText());
        groups.setTags(request.getTags());
        if (Validator.isValid(request.getMiniSessionId())) {
            Optional<MiniSession> optionalMiniSession = miniSessionRepo.findById(request.getMiniSessionId());
            groups.setMiniSession(optionalMiniSession.get());
        }
        groupRepo.save(groups);
        return "group saved successfully";
    }

    @Override
    public String updateGroupById(GroupsDto request) {
        Groups groups=null;
        if(Validator.isValid(request.getId())){
            Optional<Groups> optionalGroups=groupRepo.findById(request.getId());
            if(optionalGroups.isPresent()){
                groups=optionalGroups.get();
                groups.setGroupName(request.getGroupName());
                if(request.getIsActive()!=null && request.getIsActive().equals(Boolean.TRUE) || request.getIsActive().equals(Boolean.FALSE)) {
                    groups.setIsActive(request.getIsActive());
                }
                groups.setText(request.getText());
                groups.setTags(request.getTags());
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


   /* private FileDetails dtoToEntity(FileDetailsDto dataDto) {
        FileDetails data = new FileDetails();
        if (Validator.isValid(dataDto.getId())) {
            data.setId(dataDto.getId());
        }
        data.setFileId(dataDto.getFileId());
        data.setFileName(dataDto.getFileName());
        data.setFileSize(dataDto.getFileSize());
        data.setFileContentType(dataDto.getContentType());
        data.setFileType(dataDto.getFileType());
        data = fileRepo.save(data);
        return data;
    }*/

}
