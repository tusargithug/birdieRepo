package net.thrymr.repository;

import net.thrymr.model.Site;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SiteRepo extends JpaRepository<Site,Long>, JpaSpecificationExecutor<Site> {

}
