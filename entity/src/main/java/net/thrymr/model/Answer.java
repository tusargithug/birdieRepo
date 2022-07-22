package net.thrymr.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import net.thrymr.model.master.Question;

import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class Answer extends BaseEntity{
	
	@Column(columnDefinition = "TEXT")
	private String  textAnswer;
	
	@ManyToOne
	private Question question;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private UserCourse userCourseInfo;
	

}
