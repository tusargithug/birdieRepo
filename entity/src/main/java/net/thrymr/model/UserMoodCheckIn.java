package net.thrymr.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import net.thrymr.model.BaseEntity;
import net.thrymr.model.master.MoodInfo;
import net.thrymr.model.master.MoodIntensity;
import net.thrymr.model.master.MoodSource;

import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class UserMoodCheckIn extends BaseEntity{
	
	@ManyToOne
	private AppUser appUser;
	
	@ManyToOne
	private MoodInfo moodInfo;
	

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinTable(name = "mood_check_in_intensity", joinColumns = { @JoinColumn(name = "mood_info_id") }, inverseJoinColumns = {
			@JoinColumn(name = "mood_intensity_id") })
	private List<MoodIntensity> intensities = new ArrayList<MoodIntensity>();
	

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinTable(name = "mood_check_in_source", joinColumns = { @JoinColumn(name = "mood_info_id") }, inverseJoinColumns = {
			@JoinColumn(name = "mood_source_id") })
	private List<MoodSource> sources = new ArrayList<MoodSource>();
	
	@Column(columnDefinition = "TEXT")
	private String moreInfo;
	
	private Date moodCheckInDate;
	
	
	
	

}
