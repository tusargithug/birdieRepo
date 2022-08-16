package net.thrymr.model.master;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.*;

import net.thrymr.enums.Category;
import net.thrymr.model.BaseEntity;
import net.thrymr.model.UserMoodCheckIn;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class MoodSource extends BaseEntity{

	private String name;
	
	@Column(columnDefinition = "TEXT")
	private String description;
	
	@ManyToOne
	private FileEntity icon;
	
	private int sequence;

	@Enumerated(EnumType.STRING)
	private Category category;
	
	@ManyToMany(mappedBy = "sources")
	private List<UserMoodCheckIn> userMoodCheckIns = new LinkedList<UserMoodCheckIn>();
}
