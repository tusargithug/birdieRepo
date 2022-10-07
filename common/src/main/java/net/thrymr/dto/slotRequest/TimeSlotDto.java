package net.thrymr.dto.slotRequest;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class TimeSlotDto {


    private Long counsellorId;

    private List<SlotDetailsDto> slots = new ArrayList<>();
}
