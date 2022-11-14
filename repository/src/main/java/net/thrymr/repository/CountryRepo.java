package net.thrymr.repository;

import net.thrymr.model.master.MtCountry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryRepo extends JpaRepository<MtCountry,Long> {

}
