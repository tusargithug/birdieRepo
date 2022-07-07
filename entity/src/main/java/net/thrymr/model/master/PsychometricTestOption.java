package net.thrymr.model.master;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import net.thrymr.model.BaseEntity;

import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class PsychometricTestOption extends BaseEntity{
	
	private String option;
	
	private int score;
	
	@ManyToOne
	private PsychometricTest psychometricTest;
	
	

}
