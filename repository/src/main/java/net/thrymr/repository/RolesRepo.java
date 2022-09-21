package net.thrymr.repository;

import net.thrymr.model.master.MtRoles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolesRepo extends JpaRepository<MtRoles,Long> {
}
