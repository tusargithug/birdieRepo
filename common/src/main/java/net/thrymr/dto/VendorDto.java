package net.thrymr.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class VendorDto {
    private Long id;
    private List<Long> siteIdList;
    private List<Long> vendorIdList;
    private String POC;
    private String vendorName;
    private Boolean sortVendorName;
    private Integer pageNumber;
    private Integer pageSize;
    private String addedOn;
    private String sortBy;
    private String site;
    private String designation;
    private String email;
    private String mobileNumber;
    private String vendorId;
    private String searchKey;
    private String countryCode;
}
