package net.thrymr.model;

import java.util.ArrayList;

import java.util.Calendar;
import java.util.List;

import javax.persistence.*;


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
public class UserMoodCheckIn extends BaseEntity {
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private AppUser appUser;
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private MtMoodInfo mtMoodInfo;
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private MtMoodIntensity mtMoodIntensity;
}
