package net.thrymr.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Setter
@Getter
@Entity
@NoArgsConstructor
@Table(name = "user_learning_status")
public class UserLearningStatus extends BaseEntity {

    @Column(name = "userId", unique = true, nullable = false)
    private String userId;
    @Column(name = "user_name")
    private String userName;
    @Column(name = "unit_sequence")
    private Long unitSequence;
    @Column(name = "chapter_sequence")
    private Long chapterSequence;
    @Column(name = "video_sequence")
    private Long videoSequence;
    private Long totalUnits;
    private Long totalChaptersInUnit;
    private Long totalVideosInChapter;

}
