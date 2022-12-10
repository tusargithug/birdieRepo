package net.thrymr.repository;

import net.thrymr.model.master.LanguageDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LanguageRepo extends JpaRepository<LanguageDetails, Long> {
}
