package net.thrymr.model.master;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import net.thrymr.model.BaseEntity;
import net.thrymr.enums.FileType;

import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class FileEntity extends BaseEntity{

	@Column(name = "file_id")
	private String fileId;

	@Column(name = "content_type")
	private String contentType;
	
	private String name;
	
	@Enumerated
	private FileType fileType = FileType.NONE;
	
	@ManyToMany(mappedBy = "content", fetch = FetchType.LAZY)
    private Set<Course> employees = new HashSet<>();
	

}
