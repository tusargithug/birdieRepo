package net.thrymr.model.master;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import net.thrymr.model.BaseEntity;

import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class Course extends BaseEntity {

	private String code;

	private String name;

	private int sequence;

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinTable(name = "course_content", joinColumns = { @JoinColumn(name = "course_id") }, inverseJoinColumns = {
			@JoinColumn(name = "content_id") })
	Set<FileEntity> content = new HashSet<FileEntity>();

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinTable(name = "course_assignment", joinColumns = { @JoinColumn(name = "course_id") }, inverseJoinColumns = {
			@JoinColumn(name = "assignment_id") })
	Set<Assignment> assignments = new HashSet<Assignment>();

	@Column(columnDefinition = "TEXT")
	private String decription;

	@ManyToOne(fetch = FetchType.LAZY)
	private Category category;

}
