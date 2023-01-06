package net.thrymr.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.thrymr.model.Chapter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class UnitResponse {
    private Long id;
    private String unitName;
    private Integer sequence;
    private List<ChapterResponse> chapters =new ArrayList<>();
}
