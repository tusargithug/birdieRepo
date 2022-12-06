package net.thrymr.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CounsellorEmployeeDto {

    private Long id;
    private Long appUserId;
    private Long counsellorId;
}
