package net.thrymr.services;

import net.thrymr.dto.PsychoEducationDto;
import net.thrymr.model.master.MtPsychoEducation;

import java.util.List;

public interface PsychoEducationService {

    List<MtPsychoEducation> getAllPsychoEducation();

    MtPsychoEducation getPsychoEducationById(Long id);

    String createPsychoEducation(PsychoEducationDto request);

    String updatePsychoEducationById(PsychoEducationDto request);

    String deletePsychoEducationById(Long id);
}
