package com.example.templator.model.cv;

import com.example.templator.model.base.ReplacableElement;
import com.example.templator.modificator.model.Block;
import com.example.templator.modificator.model.Tag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Certification implements ReplacableElement {

    private String name;
    private String year;

    @Override
    public Map<String, String> getAttributeTagMap() {
        Map<String, String> attributeTagNameMap = new HashMap<>();
        attributeTagNameMap.put(name, "{{certificate_name_0}}");
        attributeTagNameMap.put(year, "{{certificate_year_0}}");
        return attributeTagNameMap;
    }

    @Override
    public Block getTemplateBlock() {
        return Block.builder()
                .headerTag(new Tag("{{certificate_name_0}}", null))
                .countOfRows(2)
                .build();
    }
}
