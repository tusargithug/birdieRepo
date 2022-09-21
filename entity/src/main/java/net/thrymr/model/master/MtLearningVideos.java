package net.thrymr.model.master;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.thrymr.model.BaseEntity;
import org.hibernate.annotations.Cache;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Setter
@Getter
@Entity
@NoArgsConstructor
@Table(name = "mt_learning_videos")
public class MtLearningVideos extends BaseEntity {

    @Column(name = "video_reference",columnDefinition = "TEXT")
    private String videoReference;
}
