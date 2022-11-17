package net.thrymr.repository;

import net.thrymr.dto.CityDto;
import net.thrymr.model.master.MtCity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CityRepo extends JpaRepository<MtCity,Long> {
}
