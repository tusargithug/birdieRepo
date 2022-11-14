package net.thrymr.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class GroupsDto {
    private Long id;
    private String groupName;
    private String text;
    private Long miniSessionId;
    private Boolean isActive;
}
