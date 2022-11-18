package net.thrymr.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "group_details")
public class GroupDetails extends BaseEntity {

    private String fileId;

    @Column(columnDefinition = "text")
    private String Text;

    private Boolean isImage;

    private Boolean isZif;

    private Boolean isPdf;

    private Boolean isVideo;

    private Boolean isAudio;

    private Boolean isEmoji;

    private Boolean isText;

    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private Groups groups;

}
