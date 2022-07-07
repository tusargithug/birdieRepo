package net.thrymr.model;

import java.util.Date;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;

import net.thrymr.model.BaseEntity;
import net.thrymr.model.master.PsychometricTest;
import net.thrymr.model.master.Question;
import net.thrymr.model.master.PsychometricTestOption;
import net.thrymr.enums.Risk;
import net.thrymr.model.master.RiskLevel;

import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class UserPsychometricTest extends BaseEntity{
	
	@ManyToOne
	private AppUser appUser;
	
	@ManyToOne
	private PsychometricTest psychometricTest;
	
	@Embedded
	private Map<Question, PsychometricTestOption> selectedOptions = new HashMap<Question, PsychometricTestOption>();
	
	private Date submittedOn;
	
	private Long totalScore;
	
	@Enumerated
	private Risk riskIdentified;
	
	@ManyToOne
	private RiskLevel riskLevel;
	

}
