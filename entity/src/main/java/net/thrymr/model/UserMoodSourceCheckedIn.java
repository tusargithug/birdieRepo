package net.thrymr.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.thrymr.model.master.MtMoodSource;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "user_mood_source_checked_in")
public class UserMoodSourceCheckedIn extends BaseEntity {

        @ManyToOne
        private AppUser appUser;

        @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
        private List<MtMoodSource> mtMoodSourceList = new ArrayList<>();

        @Column(name = "description", columnDefinition = "TEXT")
        private String description;

}
