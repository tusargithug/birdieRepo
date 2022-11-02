package net.thrymr.repository;

import net.thrymr.model.Counsellor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CounsellorRepo extends JpaRepository<Counsellor,Long>, JpaSpecificationExecutor<Counsellor> {

}
