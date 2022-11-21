package net.thrymr.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class MiniSessionDto {
    private Long id;

    private String tags;

    private String miniSessionName;

    private Boolean isActive;
}
