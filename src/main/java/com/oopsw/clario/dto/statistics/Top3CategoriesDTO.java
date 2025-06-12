package com.oopsw.clario.dto.statistics;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Top3CategoriesDTO {
    private Long categoryId;
    private String categoryName;
    private int usageCount;
}

