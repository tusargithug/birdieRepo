package net.thrymr.controller;

import net.thrymr.dto.slotRequest.TimeSlotDto;

import net.thrymr.model.CounsellorSlot;
import net.thrymr.services.CounsellorSlotService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;

import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
//@RequestMapping()
public class CounsellorSlotController {

    private final CounsellorSlotService counsellorSlotService;

    public CounsellorSlotController(CounsellorSlotService counsellorSlotService) {
        this.counsellorSlotService = counsellorSlotService;
    }

    @QueryMapping(name="getCounsellorSlot")
    public List<CounsellorSlot> getCounsellorSlot(@Argument String empId) {
        return counsellorSlotService.getCounsellorSlot(empId);
    }

    @MutationMapping(name = "createCounsellorSlot")
    public String createCounsellorSlot(@Argument(name = "input") TimeSlotDto request){
        return counsellorSlotService.createCounsellorSlot(request);
    }


}
