package net.thrymr.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class VendorDto {
    private Long id;
    private Long siteId;
    private String POC;
    private String name;
    private Integer pageNumber;
    private Integer pageSize;
    private String addedOn;
    private String sortBy;
    private String site;
    private String role;
    private String email;
    private String mobile;
    private String empId;
    private Boolean isActive;
    private String searchKey;
}
