package com.vikram.resqliciousbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Builder
@AllArgsConstructor
public class CategoryDTO {
    private Long id;
    private String name;
    private Long menuId;
    private List<Long> dishIdList;
}
