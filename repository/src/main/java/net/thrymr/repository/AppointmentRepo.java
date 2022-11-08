package net.thrymr.repository;

import net.thrymr.enums.SlotStatus;
import net.thrymr.model.UserAppointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppointmentRepo extends JpaRepository<UserAppointment, Long> {
    Long countByAppUserIdAndSlotStatusAndIsActiveAndIsDeleted(Long id, SlotStatus booked, Boolean aTrue, Boolean aFalse);
}
