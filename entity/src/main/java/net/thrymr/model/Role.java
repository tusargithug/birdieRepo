package net.thrymr.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import net.thrymr.model.BaseEntity;

import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class Role extends BaseEntity{
	
	private String name;
	
	@OneToMany(mappedBy = "", fetch = FetchType.LAZY)
	private List<AppUser> users = new ArrayList<>();

}
