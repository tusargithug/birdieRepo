package net.thrymr.model.master;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;

import net.thrymr.model.BaseEntity;
import net.thrymr.enums.Risk;

import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class RiskLevel extends BaseEntity{
	
	@ManyToOne
	private PsychometricTest psychometricTest;
	
	private Float rangeFrom;
	private Float rangeTo;
	
	@Enumerated
	private Risk risk;

}
