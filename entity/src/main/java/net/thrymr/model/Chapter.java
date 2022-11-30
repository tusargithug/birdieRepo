package net.thrymr.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.thrymr.model.master.MtQuestion;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Calendar;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Chapter extends  BaseEntity {

    @Column(name ="chapter_name" )
    private String chapterName;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private FileEntity profilePicture;

    @Column(name = "description",columnDefinition = "TEXT")
    private String description;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private FileEntity video;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private Unit unit;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY,mappedBy = "chapter")
    private List<MtQuestion> questionList=new ArrayList<>();
    }
