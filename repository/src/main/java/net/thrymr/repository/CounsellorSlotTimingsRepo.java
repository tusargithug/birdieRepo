package net.thrymr.repository;

import net.thrymr.model.CounsellorSlotTimings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

@Repository
public interface CounsellorSlotTimingsRepo extends JpaRepository<CounsellorSlotTimings,Long>, JpaSpecificationExecutor<CounsellorSlotTimings> {
    List<CounsellorSlotTimings> findAllByCounsellorId(Long counsellorId);

    boolean existsBySlotTimingAndCounsellorId(LocalTime slotTime, Long counsellorId);

    List<CounsellorSlotTimings> findBySlotTimingAndCounsellorId(LocalTime slotTime, Long counsellorId);

    boolean existsBySlotTimingAndCounsellorIdAndSlotDateAndSlotDay(LocalTime slotTime, Long counsellorId, Date date, DayOfWeek of);

    List<CounsellorSlotTimings> findAllBySlotTimingAndCounsellorIdAndSlotDateAndSlotDay(LocalTime slotTime, Long counsellorId, Date date, DayOfWeek of);
}
