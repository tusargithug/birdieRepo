package net.thrymr.model;

import java.util.ArrayList;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.*;


import lombok.NoArgsConstructor;
import net.thrymr.model.master.MtMoodInfo;
import net.thrymr.model.master.MtMoodIntensity;

import lombok.Getter;
import lombok.Setter;
import net.thrymr.model.master.MtMoodSource;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class UserMoodCheckIn extends BaseEntity {

	private String createdDate;
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private AppUser appUser;
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private MtMoodInfo mtMoodInfo;
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private MtMoodIntensity mtMoodIntensity;
}
