package net.thrymr.services;

import net.thrymr.dto.CounsellorSlotDto;
import net.thrymr.dto.response.PaginationResponse;
import net.thrymr.dto.slotRequest.TimeSlotDto;
import net.thrymr.model.CounsellorSlot;

import java.text.ParseException;
import java.util.List;

public interface CounsellorSlotService {
    String createCounsellorSlot(CounsellorSlotDto request) throws ParseException;

    PaginationResponse getAllCounsellorSlotPagination(CounsellorSlotDto request);

    CounsellorSlot getCounsellorSlotById(Long id);

    String rescheduledCounsellorSlot(CounsellorSlotDto request) throws ParseException;

    String cancelCounsellorSlot(Long id);
}
