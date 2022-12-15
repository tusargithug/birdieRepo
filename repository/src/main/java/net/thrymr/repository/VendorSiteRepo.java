package net.thrymr.repository;

import net.thrymr.model.VendorSite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VendorSiteRepo extends JpaRepository<VendorSite,Long>, JpaSpecificationExecutor<VendorSite> {
    List<VendorSite> findAllByVendorId(Long id);

    boolean existsByVendorIdAndSiteId(Long id, Long newSiteIds);

    boolean existsBySiteId(Long id);

    Optional<VendorSite> findBySiteId(Long id);
}
