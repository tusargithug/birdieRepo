package net.thrymr.services;

import net.thrymr.dto.CounsellorSlotDto;
import net.thrymr.dto.response.CounsellorSlotResponse;
import net.thrymr.dto.response.PaginationResponse;
import net.thrymr.model.CounsellorSlot;

import java.text.ParseException;
import java.util.List;

public interface CounsellorSlotService {
    String createCounsellorSlot(CounsellorSlotDto request) throws ParseException;

    PaginationResponse getAllCounsellorSlotPagination(CounsellorSlotDto request);

    List<CounsellorSlotResponse> getCounsellorSlotById(Long counsellorId);

    String updateCounsellorSlot(CounsellorSlotDto request) throws ParseException;

    String removeCounsellorSlotsById(CounsellorSlotDto request) throws ParseException;

    String deleteAllCounsellorSlots();

    String pauseCounsellorSlotsById(CounsellorSlotDto request) throws ParseException;
}
