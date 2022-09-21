package net.thrymr.model.master;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import lombok.NoArgsConstructor;
import lombok.ToString;
import net.thrymr.model.BaseEntity;
import net.thrymr.enums.QuestionCalType;

import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
@Table(name = "mt_question")
public class MtQuestion extends BaseEntity{
	
	@Column(columnDefinition = "TEXT")
	private String question;
	
	@ManyToOne
	private MtAssignment courseAssignment;
	
	@OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
	private Set<MtOptions> mtOptions = new HashSet<>();

	
	@ManyToOne
	private PsychometricTest psychometricTest;
	
	@Enumerated
	private QuestionCalType questionCalType;
	
	private int sequence;

}
