package net.thrymr.model.master;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.thrymr.model.BaseEntity;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class MtChapter extends BaseEntity {

    @Column(name = "chapter_name")
    private String chapterName;
    private String profilePicture;
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    private String video;//tutorial
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private MtUnit mtUnit;
}
