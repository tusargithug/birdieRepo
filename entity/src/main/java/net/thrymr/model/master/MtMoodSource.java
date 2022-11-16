package net.thrymr.model.master;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import lombok.NoArgsConstructor;
import net.thrymr.enums.Category;
import net.thrymr.model.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import net.thrymr.model.UserMoodSourceCheckedIn;

@Entity
@Setter
@Getter
@NoArgsConstructor
@Table(name = "mt_mood_source")
public class MtMoodSource extends BaseEntity {

    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "emoji")
    private String emoji;

    private int sequence;

    @Enumerated(EnumType.STRING)
    private Category category;

    @ManyToMany(mappedBy = "sources")
    private List<UserMoodSourceCheckedIn> userMoodSourceCheckedIns = new ArrayList<>();
}
