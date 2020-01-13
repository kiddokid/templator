package com.example.templator.model.base;

import com.example.templator.modificator.model.Block;

import java.util.List;
import java.util.Map;

public interface ReplacableElement {

    Map<String, String> getAttributeTagMap();

    Block getTemplateBlock();
}
