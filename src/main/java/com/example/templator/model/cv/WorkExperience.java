package com.example.templator.model.cv;

import com.example.templator.model.base.ReplacableElement;
import com.example.templator.modificator.model.Block;
import com.example.templator.modificator.model.Tag;
import lombok.Builder;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Builder
public class WorkExperience implements ReplacableElement {

    private String projectName;
    private String projectDescription;
    private String customerName;
    private String involvementDuration;
    private String role;
    private String teamSize;
    private String toolsAndTech;
    private List<String> projectResponsibilities;

    @Override
    public Map<String, String> getAttributeTagMap() {
        Map<String, String> attributeTagMap = new HashMap<>();
        attributeTagMap.put(projectName, "{{project_name_0}}");
        attributeTagMap.put(projectDescription, "{{project_description_0}}");
        attributeTagMap.put(customerName, "{{project_customer_name_0}}");
        attributeTagMap.put(involvementDuration, "{{involvement_duration_0}}");
        attributeTagMap.put(role, "{{project_role_0}}");
        attributeTagMap.put(teamSize, "{{project_team_size_0}}");
        attributeTagMap.put(toolsAndTech, "{{project_tool_and_tech_0}}");
        return attributeTagMap;
    }

    @Override
    public Block getTemplateBlock() {
        return Block.builder()
                .headerTag(new Tag("Experience", null))
                .footerTag(new Tag("{{end_of_project_exp}}", null))
                .countOfRows(9)
                .extandableTags(List.of(new Tag("{{project_responsibilities_0}}", null)))
                .build();
    }
}
