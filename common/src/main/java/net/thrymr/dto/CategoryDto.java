package net.thrymr.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.util.ArrayList;

import java.util.List;


@Setter
@Getter
@NoArgsConstructor
public class CategoryDto {

    private Long id;

    private String name;


    private String description;


    private List<Long> coursesIds = new ArrayList<>();

    private int sequence;

    private Boolean isActive;
}
