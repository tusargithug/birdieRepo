package net.thrymr.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import lombok.NoArgsConstructor;
import net.thrymr.model.master.Course;
import net.thrymr.enums.CourseStatus;

import lombok.Getter;
import lombok.Setter;
import net.thrymr.model.master.MtOptions;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class UserCourse extends BaseEntity {

    @ManyToOne
    private AppUser user;

    @ManyToOne
    private Course course;

    @Enumerated
    private CourseStatus status = CourseStatus.NOT_STARTED;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userCourse", fetch = FetchType.LAZY)
    private Set<MtOptions> mtOptions = new HashSet<>();

    @Column(name = "started_date")
    private Date startedDate;

    @Column(name = "completed_date")
    private Date completedDate;

}
