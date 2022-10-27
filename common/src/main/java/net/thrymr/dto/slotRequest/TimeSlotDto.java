package net.thrymr.dto.slotRequest;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.thrymr.model.AppUser;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class TimeSlotDto {


    private Long counsellorId;
    private String slotShift;
    private List<SlotDetailsDto> slots = new ArrayList<>();
}
