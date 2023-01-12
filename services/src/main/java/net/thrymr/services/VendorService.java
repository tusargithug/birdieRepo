package net.thrymr.services;

import net.thrymr.dto.VendorDto;
import net.thrymr.model.Vendor;
import net.thrymr.dto.response.VendorResponse;
import net.thrymr.model.VendorSite;

import java.util.List;

public interface VendorService {
    String saveVendor(VendorDto request);

    List<Vendor> getAllVendor();

    String deleteVendorById(Long id);

    Vendor getVendorById(Long id);

    String updateVendor(VendorDto request);

    List<VendorResponse> getAllVendorPagination(VendorDto vendorDto);

    List<VendorSite> getVendorSiteById(Long id);

    Long getTotalVendorsCount();

}
