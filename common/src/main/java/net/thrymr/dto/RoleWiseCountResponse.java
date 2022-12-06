package net.thrymr.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleWiseCountResponse {

    private Integer teamLeaderCount;
    private Integer teamManagerCount;
    private Integer directorCount;
    private Integer generalManagerCount;
    private Integer seniorManagerCount;
    private Integer accountManagerCount;

}
