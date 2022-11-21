package net.thrymr.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.thrymr.enums.FileType;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "file_entity")
public class FileEntity extends BaseEntity {
    @Column(name = "file_id")
    private String fileId;

    @Column(name = "file_name")
    private String fileName;

    private FileType fileType;

    @Column(name = "file_size")
    private Long fileSize;

    @Column(name = "file_content_type")
    private String fileContentType;

}