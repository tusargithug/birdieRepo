package net.thrymr.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import net.thrymr.model.BaseEntity;
import net.thrymr.model.master.Course;
import net.thrymr.enums.CourseStatus;

import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class UserCourse extends BaseEntity{
	
	@ManyToOne
	private AppUser user;
	
	@ManyToOne
	private Course course;
	
	@Enumerated
	private CourseStatus status = CourseStatus.NOT_STARTED;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "userCourseInfo")
	private Set<Answer> answers = new HashSet<>();
	
	private Date startedDate;
	
	private Date completedDate;

}
