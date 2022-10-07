package net.thrymr.repository;

import net.thrymr.model.CounsellorSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CounsellorSlotRepo extends JpaRepository<CounsellorSlot,Long> {
}
