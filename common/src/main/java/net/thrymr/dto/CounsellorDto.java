package net.thrymr.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CounsellorDto {
    private Long id;
    private List<String> educationalDetails;
    private List<String> languages;
    private Long siteId;
    private Long teamId;
    private String designation;
    private Long shiftTimingsId;
    private Long teamManagerId;
    private String bio;
    private String appUserName;
    private String employeeId;
    private String emailId;
    private String contactNumber;
    private Long appUserId;
    private Integer pageNumber;
    private Integer pageSize;
    private String addedOn;
    private ShiftTimingsDto shiftTimings;
    private SiteDto site;
    private TeamDto team;
    private String searchKey;
    private Boolean isActive;
}
