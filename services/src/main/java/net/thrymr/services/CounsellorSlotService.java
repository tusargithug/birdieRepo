package net.thrymr.services;

import net.thrymr.dto.slotRequest.TimeSlotDto;
import net.thrymr.model.CounsellorSlot;

import java.text.ParseException;
import java.util.List;

public interface CounsellorSlotService {
    String createCounsellorSlot(TimeSlotDto request) throws ParseException;

    List<CounsellorSlot> getCounsellorSlot();

    CounsellorSlot getCounsellorSlotById(Long id);
}
