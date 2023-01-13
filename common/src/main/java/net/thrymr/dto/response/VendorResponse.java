package net.thrymr.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.thrymr.model.Counsellor;

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
    private List<Counsellor> counsellorList;
    private int noOfCounsellors=0;
}
