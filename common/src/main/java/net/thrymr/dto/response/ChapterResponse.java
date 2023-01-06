package net.thrymr.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.thrymr.model.FileEntity;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ChapterResponse {
    private Long id;

    private String chapterName;

    private FileEntity profilePicture;

    private String description;

    private FileEntity video;

    private Integer sequence;

    private List<QuestionResponse> questionList=new ArrayList<>();
}