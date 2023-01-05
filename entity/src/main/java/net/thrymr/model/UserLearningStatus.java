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
    @Column(name = "unit_number")
    private Long unitNumber;
    @Column(name = "chapter_number")
    private Long chapterNumber;
    private Long sequence;

}
