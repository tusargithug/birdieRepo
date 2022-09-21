package net.thrymr.repository;

import net.thrymr.model.UserMoodSourceCheckedIn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMoodSourceCheckInRepo extends JpaRepository<UserMoodSourceCheckedIn,Long> {
}
