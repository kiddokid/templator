package com.example.templator.modificator.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class Block {

    private Tag headerTag;
    private Tag footerTag;
    private List<Block> extandableBlocks;
    private Integer countOfRows;
    private Integer countOfCopies;
}
