package net.thrymr.repository;

import net.thrymr.model.master.MtTeam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamRepo extends JpaRepository<MtTeam, Long>, JpaSpecificationExecutor<MtTeam> {

}
