package net.thrymr.services;

import net.thrymr.dto.VendorDto;
import net.thrymr.model.master.MtVendor;

import java.util.List;

public interface VendorService {
    String saveVendor(VendorDto request);

    List<MtVendor> getAllVendor();

    String deleteVendorById(Long id);

    MtVendor getVendorById(Long id);

    String updateVendor(VendorDto request);

    List<MtVendor> getAllVendorPagination(VendorDto vendorDto);

}
