package com.example.templator.modificator.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TextPosition {

    private Integer tableIndex;
    private Integer rowIndex;
    private Integer paragraphIndex;
}
