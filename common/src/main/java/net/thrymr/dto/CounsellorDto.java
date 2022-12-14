package net.thrymr.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
public class CounsellorDto {
    private Long id;
    private List<String> educationalDetails;
    private List<String> languages;
    private Long siteId;
    private String designation;
    private String bio;
    private String counsellorName;
    private Boolean sortCounsellorName;
    private String empId;
    private String emailId;
    private String mobileNumber;
    private Integer pageNumber;
    private Integer  pageSize;
    private Boolean addedOn;
    private String searchKey;
    private Boolean isActive;
    private String shiftStartAt;
    private String shiftEndAt;
    private List<String> shiftTimings;
    private String countryCode;
    private String gender;
    private Boolean isCreatedOn;
    private Long vendorId;
}
