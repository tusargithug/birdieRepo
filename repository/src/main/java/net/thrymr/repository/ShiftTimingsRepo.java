package net.thrymr.repository;

import net.thrymr.model.ShiftTimings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Repository
public interface ShiftTimingsRepo extends JpaRepository<ShiftTimings,Long> {
}
