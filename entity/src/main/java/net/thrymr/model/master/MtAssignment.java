package net.thrymr.model.master;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import lombok.NoArgsConstructor;
import net.thrymr.model.BaseEntity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
@Table(name = "mt_assignment")
public class MtAssignment extends BaseEntity{
	
	private String name;
	
	@Column(columnDefinition = "TEXT")
	private String description;
	
	@OneToMany(mappedBy = "courseAssignment", cascade = CascadeType.ALL , fetch = FetchType.LAZY)
	private Set<MtQuestion> mtQuestions = new HashSet<>();
	
	@ManyToMany(mappedBy = "assignments")
	private Set<Course> courses = new HashSet<>();

	//private LocalDateTime duration;

	//private String instructions;
	//  TODO  need to create enum
	// private FrequencyType frequencyType;
}
