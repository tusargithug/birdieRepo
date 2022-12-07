package net.thrymr.repository;


import net.thrymr.enums.Alerts;
import net.thrymr.enums.Roles;
import net.thrymr.model.AppUser;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository
public interface AppUserRepo extends JpaRepository<AppUser, Long> , JpaSpecificationExecutor<AppUser> {
	Optional<AppUser> findByMobileAndIsActiveAndIsDeleted(String mobile, Boolean aTrue, Boolean aFalse);
	Optional<AppUser> findByEmail(String email);
}
