package net.thrymr.repository;

import net.thrymr.model.Groups;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepo extends JpaRepository<Groups,Long> {
}
