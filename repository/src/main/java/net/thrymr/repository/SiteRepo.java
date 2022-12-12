package net.thrymr.repository;

import net.thrymr.model.Site;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SiteRepo extends JpaRepository<Site,Long>, JpaSpecificationExecutor<Site> {
    List<Site> findAllByIdInAndIsActiveAndIsDeleted(List<Long> siteIdList, Boolean aTrue, Boolean aFalse);
}
