package net.thrymr.repository;
import net.thrymr.model.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VendorRepo extends JpaRepository<Vendor,Long>, JpaSpecificationExecutor<Vendor>  {
    boolean existsByVendorId(String vendorId);

    boolean existsByMobileNumber(String mobileNumber);

    boolean existsByEmail(String email);
}
