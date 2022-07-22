package net.thrymr.model.master;

import java.util.ArrayList;

import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.RequiredArgsConstructor;
import net.thrymr.model.BaseEntity;
import net.thrymr.enums.MoodType;

import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@RequiredArgsConstructor
public class MoodInfo extends BaseEntity {
	
	private String name;
	
	@Column(columnDefinition = "TEXT")
	private String description;
	
	@ManyToOne
	private FileEntity icon;

	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL,mappedBy = "moodInfo")
	private List<MoodIntensity> intensities=new ArrayList<>();
	
	private int sequence;

	@Enumerated(EnumType.STRING)
	private MoodType moodType;

	private String intensityName;

}
