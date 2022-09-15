package net.thrymr.model.master;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import lombok.NoArgsConstructor;
import net.thrymr.model.BaseEntity;

import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class Category extends BaseEntity{
	
	private String name;
	
	@Column(columnDefinition = "TEXT")
	private String description;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "category")
	private Set<Course> courses = new HashSet<>();
	
	private int sequence;
	

}
