package net.thrymr.model.master;

import java.util.ArrayList;

import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.RequiredArgsConstructor;
import net.thrymr.model.AppUser;
import net.thrymr.model.BaseEntity;
import net.thrymr.enums.MoodType;

import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@RequiredArgsConstructor
@Table(name = "mt_mood_info")
public class MtMoodInfo extends BaseEntity {

	private String name;

	@Column(columnDefinition = "TEXT")
	private String description;

	@Column(name = "emoji")
	private String emoji;

	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "mtMoodInfo")
	private List<MtMoodIntensity> intensities = new ArrayList<>();

	private int sequence;

	@Enumerated(EnumType.STRING)
	private MoodType moodType;

	@Column(name = "intensity_name")
	private String intensityName;

}