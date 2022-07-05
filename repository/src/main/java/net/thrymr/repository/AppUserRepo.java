package net.thrymr.repository;

import net.thrymr.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AppUserRepo extends JpaRepository<AppUser, Long> {
	Optional<AppUser> findByMobileAndIsActiveAndIsDeleted(String mobile, Boolean aTrue, Boolean aFalse);

	Optional<AppUser> findByMobile(String contactNumber);

	Optional<AppUser> findByEmail(String email);
}
