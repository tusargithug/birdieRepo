package net.thrymr.services;

import net.thrymr.dto.CounsellorSlotResponseDto;
import net.thrymr.dto.slotRequest.SlotDetailsDto;
import net.thrymr.dto.slotRequest.TimeSlotDto;
import net.thrymr.model.CounsellorSlot;

import java.text.ParseException;
import java.util.List;
import java.util.Optional;

public interface CounsellorSlotService {
    String createCounsellorSlot(TimeSlotDto request) throws ParseException;

    List<CounsellorSlot> getCounsellorSlot();

    CounsellorSlot getCounsellorSlotById(Long id);
}
