package net.thrymr.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SiteDto {
    private Long id;
    private String siteId;
    private String siteName;
    private CityDto city;
    private CountryDto country;
    private RegionDto region;
    private AppUserDto siteManager;
    private Long cityId;
    private Long countryId;
    private Long regionId;
    private Long siteManagerId;
    private Boolean status;
    private Integer pageSize;
    private Integer PageNumber;
    private String searchKey;
    private Boolean isCreatedOn;
    private Boolean sortSiteName;
    private Long vendorId;
}
