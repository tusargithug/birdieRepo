package net.thrymr.repository;

import net.thrymr.model.UserMoodCheckIn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMoodCheckInRepo extends JpaRepository<UserMoodCheckIn, Long> {
}
