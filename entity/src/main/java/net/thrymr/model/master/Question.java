package net.thrymr.model.master;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import lombok.NoArgsConstructor;
import net.thrymr.model.BaseEntity;
import net.thrymr.model.Answer;
import net.thrymr.enums.QuestionCalType;

import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class Question extends BaseEntity{
	
	@Column(columnDefinition = "TEXT")
	private String questionText;
	
	@ManyToOne
	private Assignment courseAssignment;
	
	@OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
	private Set<Answer> answers = new HashSet<Answer>();
	
	@ManyToOne
	private PsychometricTest psychometricTest;
	
	@Enumerated
	private QuestionCalType questionCalType;
	
	private int sequence;

}
