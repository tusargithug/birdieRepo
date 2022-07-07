package net.thrymr.model.master;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;

import net.thrymr.model.BaseEntity;
import net.thrymr.enums.FileType;

import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class FileEntity extends BaseEntity{
	
	private String fileId;
	
	private String contentType;
	
	private String name;
	
	@Enumerated
	private FileType fileType = FileType.NONE;
	
	@ManyToMany(mappedBy = "content", fetch = FetchType.LAZY)
    private Set<Course> employees = new HashSet<Course>();
	

}
