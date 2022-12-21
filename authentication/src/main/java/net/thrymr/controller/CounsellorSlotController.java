package net.thrymr.controller;
import net.thrymr.dto.CounsellorSlotDto;
import net.thrymr.dto.response.PaginationResponse;

import net.thrymr.model.CounsellorSlot;
import net.thrymr.services.CounsellorSlotService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;

import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.List;


@RestController
//@RequestMapping()
public class CounsellorSlotController {

    private final CounsellorSlotService counsellorSlotService;

    public CounsellorSlotController(CounsellorSlotService counsellorSlotService) {
        this.counsellorSlotService = counsellorSlotService;
    }
    @QueryMapping(name="getAllCounsellorSlotPagination")
    public PaginationResponse getAllCounsellorSlotPagination(@Argument(name = "input") CounsellorSlotDto request) {
        return counsellorSlotService.getAllCounsellorSlotPagination(request);
    }
    @QueryMapping(name="getCounsellorSlotById")
    public List<CounsellorSlot> getCounsellorSlotById(@Argument Long counsellorId) {
        return counsellorSlotService.getCounsellorSlotById(counsellorId);
    }

    @MutationMapping(name = "createCounsellorSlot")
    public String createCounsellorSlot(@Argument(name = "input") CounsellorSlotDto request) throws ParseException {
        return counsellorSlotService.createCounsellorSlot(request);
    }

    @MutationMapping(name="updateCounsellorSlot")
    public String updateCounsellorSlot(@Argument(name = "input") CounsellorSlotDto request) throws ParseException {
        return counsellorSlotService.updateCounsellorSlot(request);
    }

    @MutationMapping(name="removeCounsellorSlotsById")
    public String removeCounsellorSlotsById(@Argument (name = "input") CounsellorSlotDto request) {
        return counsellorSlotService.removeCounsellorSlotsById(request);
    }

    @MutationMapping(name="deleteAllCounsellorSlot")
    public String deleteAllCounsellorSlot() {
        return counsellorSlotService.deleteAllCounsellorSlots();
    }

}
