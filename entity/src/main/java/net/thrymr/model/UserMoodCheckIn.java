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
import net.thrymr.model.master.MtMoodInfo;
import net.thrymr.model.master.MtMoodIntensity;

import lombok.Getter;
import lombok.Setter;
import net.thrymr.model.master.MtMoodSource;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class UserMoodCheckIn extends BaseEntity{

	@ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
	private AppUser appUser;
	@ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
	private MtMoodInfo mtMoodInfo;
	@ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
	private MtMoodSource mtMoodSource;
	@ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
	private MtMoodIntensity mtMoodIntensity;
	@Column(name = "description",columnDefinition = "TEXT")
	private String description;
}
