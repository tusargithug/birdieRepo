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


    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "mood_check_in_source", joinColumns = { @JoinColumn(name = "mood_info_id") }, inverseJoinColumns = {
            @JoinColumn(name = "mood_source_id") })
    private List<MtMoodSource> sources = new ArrayList<>();


    @Column(name = "description",columnDefinition = "TEXT")
    private String description;
}
