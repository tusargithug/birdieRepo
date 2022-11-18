package net.thrymr.repository;

import net.thrymr.model.master.MtShiftTimings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Repository
public interface ShiftTimingsRepo extends JpaRepository<MtShiftTimings, Long> {

}
