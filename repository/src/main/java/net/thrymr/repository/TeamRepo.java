package net.thrymr.repository;

import net.thrymr.dto.TeamDto;
import net.thrymr.model.AppUser;
import net.thrymr.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface TeamRepo extends JpaRepository<Team,Long>, JpaSpecificationExecutor<Team> {

}
