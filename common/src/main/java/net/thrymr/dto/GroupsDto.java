package net.thrymr.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class GroupsDto {

    private Long id;

    private Long groupId;
    
    private String groupName;
    
    private String text;
    
    private Long miniSessionId;
    
    private Boolean isActive;
    
    private List<String> tags;
    
    private String fileId;
    
    private Boolean isImage;
    
    private Boolean isPdf;
    
    private Boolean isVideo;
    
    private Boolean isAudio;
    
    private Boolean isZif;
    
    private Boolean isText;

    private Boolean isEmoji;
}