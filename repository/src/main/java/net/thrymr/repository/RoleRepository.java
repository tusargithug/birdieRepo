package net.thrymr.repository;

import net.thrymr.model.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/*
 *@author Chanda Veeresh
 *@version 1.0
 *@since  08-07-2022
 */

@Repository
public interface RoleRepository extends JpaRepository<Roles, Long> {
}
