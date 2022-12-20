package net.thrymr.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
@NoArgsConstructor
public class VendorResponse {
    private Long id;
    private String POC;
    private String vendorName;
    private String vendorId;
    private String countryCode;
    private String mobileNumber;
    private String email;
    List<SiteResponse> siteResponseList = new ArrayList<>();
    private Long totalElement;
    private int totalPages;
}
