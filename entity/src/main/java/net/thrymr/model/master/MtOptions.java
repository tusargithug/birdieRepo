package net.thrymr.model.master;

import javax.persistence.*;

import lombok.NoArgsConstructor;
import net.thrymr.model.BaseEntity;
import net.thrymr.model.UserCourse;


import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
@Table(name = "mt_options")
public class MtOptions extends BaseEntity {
	
	@Column(columnDefinition = "TEXT")
	private String  textAnswer;
	
	@ManyToOne(fetch = FetchType.LAZY,cascade =CascadeType.ALL)
	private MtQuestion question;

	@ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
	private UserCourse userCourse;
}
