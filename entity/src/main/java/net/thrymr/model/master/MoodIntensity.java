package net.thrymr.model.master;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import net.thrymr.model.BaseEntity;
import net.thrymr.model.UserMoodCheckIn;

import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class MoodIntensity extends BaseEntity{
	
	private String name;
	
	@Column(columnDefinition = "TEXT")
	private String description;
	
	@ManyToOne
	private FileEntity icon;

	@ManyToOne
	private MoodInfo moodInfo;
	
	private int sequence;
	
	private Float score;
	
	@ManyToMany(mappedBy = "intensities")
	private List<UserMoodCheckIn> userMoodCheckIns = new LinkedList<UserMoodCheckIn>();

}
