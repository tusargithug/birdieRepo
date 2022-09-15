package net.thrymr.model;

import java.util.ArrayList;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;


import lombok.NoArgsConstructor;
import net.thrymr.model.master.MoodIntensity;
import net.thrymr.model.master.MoodSource;

import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class UserMoodCheckIn extends BaseEntity{
	
	@ManyToOne
	private AppUser appUser;


	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinTable(name = "mood_check_in_intensity", joinColumns = { @JoinColumn(name = "mood_info_id") }, inverseJoinColumns = {
			@JoinColumn(name = "mood_intensity_id") })
	private List<MoodIntensity> intensities = new ArrayList<>();
	

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinTable(name = "mood_check_in_source", joinColumns = { @JoinColumn(name = "mood_info_id") }, inverseJoinColumns = {
			@JoinColumn(name = "mood_source_id") })
	private List<MoodSource> sources = new ArrayList<>();
	
	@Column(name = "more_info",columnDefinition = "TEXT")
	private String moreInfo;
	

	
	
	
	

}
