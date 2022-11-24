package net.thrymr.services.impl;

import net.thrymr.dto.MeditationDto;
import net.thrymr.model.FileDetails;
import net.thrymr.model.FileEntity;
import net.thrymr.model.master.MtMeditation;
import net.thrymr.repository.FileRepo;
import net.thrymr.repository.MeditationRepo;
import net.thrymr.services.MeditationService;
import net.thrymr.utils.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MeditationServiceImpl implements MeditationService {

    @Autowired
    private MeditationRepo meditationRepo;
    @Autowired
    FileRepo fileRepo;

    @Override
    public List<MtMeditation> getAllMeditation() {

        List<MtMeditation> meditationList = meditationRepo.findAll();

        if (!meditationList.isEmpty()) {
            return meditationList.stream().filter(obj -> obj.getIsActive().equals(Boolean.TRUE)).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    @Override
    public MtMeditation getMeditationById(Long id) {

        if (Validator.isValid(id)) {
            Optional<MtMeditation> optionalMtMeditation = meditationRepo.findById(id);
            if (optionalMtMeditation.isPresent() && optionalMtMeditation.get().getIsActive().equals(Boolean.TRUE)) {
                return optionalMtMeditation.get();
            }
        }
        return new MtMeditation();
    }

    @Override
    public String createMeditation(MeditationDto request) {
        meditationRepo.save(dtoToMeditationEntity(request));
        return "create Meditation successfully";
    }

    @Override
    public String updateMeditationById(MeditationDto request) {

        MtMeditation mtMeditation = null;
        if (Validator.isValid(request.getId())) {
            Optional<MtMeditation> optionalMtMeditation = meditationRepo.findById(request.getId());
            if (optionalMtMeditation.isPresent()) {
                mtMeditation = optionalMtMeditation.get();
                if (Validator.isValid(request.getName())) {
                    mtMeditation.setName(request.getName());
                }
                if (Validator.isValid(request.getMeditationVideoLink())) {
                    mtMeditation.setMeditationVideoLink(request.getMeditationVideoLink());
                }
                if (request.getIsActive() != null) {
                    mtMeditation.setIsActive(request.getIsActive());
                }
                if(Validator.isValid(request.getFileId())){
                    Optional<FileEntity> fileEntity=fileRepo.findByFileId(request.getFileId());
                    if(fileEntity.isPresent()){
                        mtMeditation.setFile(fileEntity.get());
                    }
                }
                meditationRepo.save(mtMeditation);
                return "Meditation updated successfully";
            }
        }

        return "Meditation id not found";
    }

    @Override
    public String deleteMeditationById(Long id) {
        MtMeditation mtMeditation = null;
        if (Validator.isValid(id)) {
            Optional<MtMeditation> optionalMtMeditation = meditationRepo.findById(id);
            if(optionalMtMeditation.isPresent()) {
                mtMeditation = optionalMtMeditation.get();
                mtMeditation.setIsActive(Boolean.FALSE);
                mtMeditation.setIsDeleted(Boolean.TRUE);
                meditationRepo.save(mtMeditation);
                return "Meditation deleted successfully";

            }
        }
        return "Meditation id not found";
    }

    private MtMeditation dtoToMeditationEntity(MeditationDto request) {
        MtMeditation mtMeditation = new MtMeditation();
        mtMeditation.setName(request.getName());
        mtMeditation.setMeditationVideoLink(request.getMeditationVideoLink());
        mtMeditation.setIsActive(request.getIsActive());
        if(Validator.isValid(request.getFileId())){
            Optional<FileEntity> fileEntity=fileRepo.findByFileId(request.getFileId());
            if(fileEntity.isPresent()){
                mtMeditation.setFile(fileEntity.get());
            }
        }
        return mtMeditation;
    }
}