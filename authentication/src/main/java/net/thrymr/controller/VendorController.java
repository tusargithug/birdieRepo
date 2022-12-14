package net.thrymr.controller;
import net.thrymr.dto.VendorDto;
import net.thrymr.dto.response.PaginationResponse;
import net.thrymr.model.Vendor;
import net.thrymr.model.VendorSite;
import net.thrymr.services.VendorService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class VendorController {
    private final VendorService vendorService;

    public VendorController(VendorService vendorService) {
        this.vendorService = vendorService;
    }

    @MutationMapping(name = "saveVendor")
    public String saveVendor(@Argument(name = "input") VendorDto request) {
        return vendorService.saveVendor(request);
    }

    @QueryMapping(name = "getAllVendor")
    public List<Vendor> getAllVendor() {
        return vendorService.getAllVendor();
    }

    @MutationMapping(name = "deleteVendorById")
    public String deleteVendorById(@Argument Long id) {
        return vendorService.deleteVendorById(id);
    }

    @QueryMapping
    public Vendor getVendorById(@Argument Long id) {return vendorService.getVendorById(id);}

    @MutationMapping(name = "updateVendor")
    public String updateVendor(@Argument(name = "input")VendorDto request) {
        return vendorService.updateVendor(request);
    }

    @QueryMapping(name = "getAllVendorPagination")
    public PaginationResponse getAllVendorPagination(@Argument(name="input") VendorDto request) {
        return vendorService.getAllVendorPagination(request);
    }

    @QueryMapping(name = "getVendorSiteById")
    public VendorSite getVendorSiteById(@Argument Long id) {return vendorService.getVendorSiteById(id);}
}
