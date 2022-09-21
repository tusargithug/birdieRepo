package net.thrymr.repository;

import net.thrymr.model.master.MtRoles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/*
 *@author Chanda Veeresh
 *@version 1.0
 *@since  08-07-2022
 */

@Repository
public interface RoleRepo extends JpaRepository<MtRoles, Long> {
    Optional<MtRoles> findByName(String name);
}
