package net.thrymr.repository;

import net.thrymr.model.master.MtRegion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegionRepo extends JpaRepository<MtRegion,Long> {

}
