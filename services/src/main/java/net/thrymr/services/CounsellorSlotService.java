package net.thrymr.services;

import net.thrymr.dto.slotRequest.TimeSlotDto;
import net.thrymr.model.CounsellorSlot;

import java.util.List;

public interface CounsellorSlotService {
    String createCounsellorSlot(TimeSlotDto request);

    List<CounsellorSlot> getCounsellorSlot( String empId);
}
