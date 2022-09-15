package net.thrymr.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Setter
@Getter
@NoArgsConstructor
@ToString
public class Roles extends BaseEntity{

	private String name;

	@JsonIgnore
	@OneToMany(mappedBy = "roles",cascade = CascadeType.ALL)
	private List<AppUser> users = new ArrayList<>();

	public  Roles(String  name){
		this.name=name;
	}

}
