package net.thrymr.repository;

import net.thrymr.model.master.MoodInfo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;




@Repository
public interface MoodInfoRepo extends JpaRepository<MoodInfo, Long> {

}
