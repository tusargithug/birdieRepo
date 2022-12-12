package net.thrymr.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.thrymr.enums.Alerts;
import java.util.List;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AppUserDto {

    private Long id;

    private String email;

    private String userName;

    private String mobile;
    
    private String password;

    private String searchKey;

    private Boolean isActive;

    private String empId;

    private String roles;

    private String educationDetails;

    private String languages;

    private Long siteId;

    private Long shiftTimingsId;

    private Long CounsellorSlotId;

    private Long teamId;

    private String dateOfJoining;

    private List<Alerts> alerts;

    private Integer pageSize;

    private Integer pageNumber;

    private String addedOn;

    private Long counsellorId;

    private String countryCode;

    private String gender;

    private Boolean isTeamLeader;

    private String shiftStartAt;
    private String shiftEndAt;
    private String shiftTimings;

    private Boolean isTeamManager;
    private List<Long> appUserIdList;
    private Boolean sortUserName;

    private String pictureId;

    private List<Alerts> alertList;
    private List<String> shiftTimingsList;
}
