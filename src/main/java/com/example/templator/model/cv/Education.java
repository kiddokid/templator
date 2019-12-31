package com.example.templator.model.cv;

import com.example.templator.model.base.ReplacableElement;
import com.example.templator.modificator.model.Block;
import com.example.templator.modificator.model.Tag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Education implements ReplacableElement {

    private String degreeName;
    private String schoolName;
    private String schoolDepartment;
    private String graduatedYear;

    @Override
    public Map<String, String> getAttributeTagMap() {
        Map<String, String> attributeTagMap = new HashMap<>();
        attributeTagMap.put(degreeName, "{{school_degree_name_0}}");
        attributeTagMap.put(schoolName, "{{school_name_0}}");
        attributeTagMap.put(schoolDepartment, "{{school_department_0}}");
        attributeTagMap.put(graduatedYear, "{{school_grad_year_0}}");
        return attributeTagMap;
    }

    @Override
    public Block getTemplateBlock() {
        return Block.builder()
                .headerTag(new Tag("{{school_degree_name_0}}", null))
                .countOfRows(4)
                .build();
    }
}
