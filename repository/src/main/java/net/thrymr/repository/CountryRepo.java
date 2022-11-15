package net.thrymr.repository;

import net.thrymr.model.master.MtCountry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryRepo extends JpaRepository<MtCountry, Long> {

}
