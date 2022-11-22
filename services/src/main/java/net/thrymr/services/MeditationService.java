package net.thrymr.services;


import net.thrymr.dto.MeditationDto;
import net.thrymr.model.master.MtMeditation;

import java.util.List;

public interface MeditationService {

    List<MtMeditation> getAllMeditation();

    MtMeditation getMeditationById(Long id);

    String createMeditation(MeditationDto request);

    String updateMeditationById(MeditationDto request);

    String deleteMeditationById(Long id);
}
