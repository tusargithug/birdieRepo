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
public class FileDetails extends BaseEntity {
    private String fileId;

    @Column(name = "file_name")
    private String fileName;

    @Enumerated(EnumType.STRING)
    private FileType fileContentType;

    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private Groups groups;
}
