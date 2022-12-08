package net.thrymr.repository;

import net.thrymr.model.TeamMembers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamMembersRepo extends JpaRepository<TeamMembers,Long> {


    List<TeamMembers> findAllByTeamId(Long id);
}
