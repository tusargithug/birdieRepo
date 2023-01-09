package net.thrymr.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.thrymr.model.master.MtMoodSource;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class UserMoodCheckInMoodSources extends  BaseEntity {
    @ManyToOne
    private UserMoodCheckIn userMoodCheckIn;
    @ManyToOne
    private MtMoodSource mtMoodSource;
}
