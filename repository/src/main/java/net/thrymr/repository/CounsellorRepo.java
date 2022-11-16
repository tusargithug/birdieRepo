package net.thrymr.repository;

import net.thrymr.model.master.MtCounsellor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CounsellorRepo extends JpaRepository<MtCounsellor, Long>, JpaSpecificationExecutor<MtCounsellor> {

}
