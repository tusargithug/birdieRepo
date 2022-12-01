package net.thrymr.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.thrymr.enums.TagType;

import javax.persistence.ElementCollection;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class MiniSessionDto {
    private Long id;

    private List<String> tags;

    private String miniSessionName;

    private Boolean isActive;

    private Integer pageNumber;

    private Integer pageSize;

    private Boolean sortMiniSessionName;

    private String searchKey;
}
