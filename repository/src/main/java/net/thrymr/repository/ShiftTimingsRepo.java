package net.thrymr.repository;

import net.thrymr.model.master.MtShiftTimings;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShiftTimingsRepo extends JpaRepository<MtShiftTimings, Long> {

}
