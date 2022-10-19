package net.thrymr.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.thrymr.model.master.FileEntity;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Chapter extends  BaseEntity{

    @Column(name ="chapter_name" )
    private String chapterName;
    @OneToOne
    private FileEntity profilePicture;
    @Column(name = "description",columnDefinition = "TEXT")
    private String description;
    @OneToOne
    private FileEntity video;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private Unit unit;
    }
