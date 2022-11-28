package net.thrymr.utils;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.thrymr.model.Team;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PageGetResponse {

    private Object content;
    private Integer totalPages;
    private Long count;

    public PageGetResponse(Object content, Object contentList, Integer totalPages) {
        this.content = content;
        this.content = contentList;
        this.totalPages=totalPages;
    }

    public PageGetResponse(Object content, Integer totalPages, Long count) {
        this.content = content;
        this.totalPages = totalPages;
        this.count = count;
    }

    public PageGetResponse(Object content,Integer totalPages){
        this.content=content;
        this.totalPages=totalPages;
    }

    public PageGetResponse(Object content) {
        this.content=content;
    }
}