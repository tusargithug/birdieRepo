package net.thrymr.repository;

import net.thrymr.model.CounsellorEmployee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CounsellorEmployeeRepo extends JpaRepository<CounsellorEmployee, Long>, JpaSpecificationExecutor<CounsellorEmployee> {
}
