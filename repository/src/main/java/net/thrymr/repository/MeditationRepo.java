package net.thrymr.repository;

import net.thrymr.model.master.MtMeditation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface MeditationRepo extends JpaRepository<MtMeditation, Long>, JpaSpecificationExecutor<MtMeditation> {
}
