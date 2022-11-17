package net.thrymr.repository;

import net.thrymr.model.TeamMembers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamMembersRepo extends JpaRepository<TeamMembers,Long> {
}
