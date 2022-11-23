package net.thrymr.repository;

import net.thrymr.model.master.MtWorksheet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorksheetRepo extends JpaRepository<MtWorksheet, Long> {
}
