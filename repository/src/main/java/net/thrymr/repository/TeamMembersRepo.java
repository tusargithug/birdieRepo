package net.thrymr.repository;

import net.thrymr.model.TeamMembers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamMembersRepo extends JpaRepository<TeamMembers,Long> {


    List<TeamMembers> findAllByTeamId(Long id);
    boolean existsByAppUserId(Long id);
   // List<TeamMembers> findByAppUserId(Long id);
    List<TeamMembers> findAllByAppUserIdIn(List<Long> appUserIdList);

    List<TeamMembers> findByAppUserIdAndTeamId(Long id, Long teamId);
}
