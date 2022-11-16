package net.thrymr.repository;

import net.thrymr.model.master.MtSite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SiteRepo extends JpaRepository<MtSite, Long>, JpaSpecificationExecutor<MtSite> {

}
