package net.thrymr.model.master;

import java.util.ArrayList;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import net.thrymr.model.BaseEntity;
import net.thrymr.enums.MoodType;

import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class MoodInfo extends BaseEntity {
	
	private String name;
	
	@Column(columnDefinition = "TEXT")
	private String description;
	
	@ManyToOne
	private FileEntity icon;
	
	@OneToMany(mappedBy = "moodInfo", fetch = FetchType.LAZY)
	private List<MoodIntensity> intensities = new ArrayList<MoodIntensity>();
	
	private int sequence;

	@Enumerated
	private MoodType moodType;

	private String intensityName;

}
