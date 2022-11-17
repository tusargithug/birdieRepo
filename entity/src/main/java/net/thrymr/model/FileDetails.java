package net.thrymr.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "file_details")
public class FileDetails extends BaseEntity {
    @Column(name = "file_id")
    private String fileId;

    @Column(name = "file_name")
    private String fileName;

    private String fileType;

    @Column(name = "file_size")
    private Long fileSize;

    @Column(name = "file_content_type")
    private String fileContentType;

    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private Groups groups;

}
