package net.thrymr.model.master;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import lombok.NoArgsConstructor;
import net.thrymr.model.BaseEntity;
import net.thrymr.model.UserMoodCheckIn;

import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
@Table(name = "mt_mood_intensity")
public class MtMoodIntensity extends BaseEntity{
	
	private String name;
	
	@Column(columnDefinition = "TEXT")
	private String description;

	@Column(name = "emoji")
	private String emoji;

	@ManyToOne
	private MtMoodInfo mtMoodInfo;
	
	private int sequence;
	
	private Float score;
	
	@ManyToMany
	private List<UserMoodCheckIn> userMoodCheckIns = new ArrayList<>();

}
