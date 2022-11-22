package net.thrymr.repository;

import net.thrymr.model.MiniSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface MiniSessionRepo extends JpaRepository<MiniSession,Long> , JpaSpecificationExecutor<MiniSession> {

}
