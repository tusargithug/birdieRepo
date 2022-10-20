package net.thrymr.repository;

import net.thrymr.model.CounsellorSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CounsellorSlotRepo extends JpaRepository<CounsellorSlot,Long> {
    List<CounsellorSlot> findByAppUser(String empId);
}
