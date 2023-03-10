package net.thrymr.controller;

import net.thrymr.dto.slotRequest.TimeSlotDto;

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

    @QueryMapping(name = "getCounsellorSlot")
    public List<CounsellorSlot> getCounsellorSlot() {
        return counsellorSlotService.getCounsellorSlot();
    }

    @QueryMapping(name = "getCounsellorSlotById")
    public CounsellorSlot getCounsellorSlotById(@Argument Long id) {
        return counsellorSlotService.getCounsellorSlotById(id);
    }

    @MutationMapping(name = "createCounsellorSlot")
    public String createCounsellorSlot(@Argument(name = "input") TimeSlotDto request) throws ParseException {
        return counsellorSlotService.createCounsellorSlot(request);
    }

    @MutationMapping(name = "rescheduledCounsellorSlot")
    public String rescheduledCounsellorSlot(@Argument(name = "input") TimeSlotDto request) throws ParseException {
        return counsellorSlotService.rescheduledCounsellorSlot(request);
    }

    @MutationMapping(name = "cancelCounsellorSlot")
    public String cancelCounsellorSlot(@Argument Long id) {
        return counsellorSlotService.cancelCounsellorSlot(id);
    }

}
